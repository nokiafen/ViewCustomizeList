package com.heli.alihomepage;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;
import java.lang.ref.WeakReference;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/08/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class scroll_behavior extends CoordinatorLayout.Behavior<NestedScrollView> {
    private WeakReference<View> dependy;
    private int maxFunctionCollaped = 0;
    private Scroller scroller;
    private Scroller scrollerRefresh;
    private Handler handler;
    private View anim_root;
    private View scroll_content;
    private ValueAnimator valueAnimator;
    private boolean isNestScrolling;
    public scroll_behavior() {
    }

    public scroll_behavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        scrollerRefresh = new Scroller(context);
        handler = new Handler();
    }

    /**
     * 指定依赖布局
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull NestedScrollView child,
                                   @NonNull View dependency) {
        if (dependency.getId() == R.id.function_area) {
            dependy = new WeakReference<View>(dependency);
            return true;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    /**
     * 依赖布局变了之后的回调
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent,
                                          @NonNull NestedScrollView child, @NonNull View dependency) {
        child.setTranslationY(dependency.getTranslationY()*2);

        float perchent = dependency.getTranslationY() / maxFunctionCollaped * 0.3f;
//        parent.findViewById(R.id.title_bar).setAlpha(1-perchent);
        if (dependency.getTranslationY() > -maxFunctionCollaped * 0.3f) {
            parent.findViewById(R.id.title_bar).setBackgroundResource(R.mipmap.top_search_back);
        } else {
            parent.findViewById(R.id.title_bar).setBackgroundResource(R.mipmap.search_bar_collapsing);
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    /**
     * 页面布局完的回调  在这里修改初始位置关系  毕竟 coordinateLayout 就是Framelayout 指定位置关系不能太具体
     * @param parent
     * @param child
     * @param layoutDirection
     * @return
     */
    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull NestedScrollView child,
                                 int layoutDirection) {
        View function_area = parent.findViewById(R.id.function_area);
        View tigle_bar = parent.findViewById(R.id.title_bar);
        View  bottomLayout = parent.findViewById(R.id.bottom_layout);
        child.layout(0, function_area.getBottom(), parent.getMeasuredWidth(),
                function_area.getMeasuredHeight() + parent.getMeasuredHeight()+bottomLayout.getMeasuredHeight());
        maxFunctionCollaped = (int) (function_area.getMeasuredHeight()*0.5f);
        anim_root = child.findViewById(R.id.anim_root);
        scroll_content = child.findViewById(R.id.scroll_content);
        return true;
    }

    /**
     * 是否允许嵌套滑动 每次都会走
     * @param coordinatorLayout
     * @param child
     * @param directTargetChild
     * @param target
     * @param axes
     * @param type
     * @return
     */
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull NestedScrollView child, @NonNull View directTargetChild, @NonNull View target,
                                       int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    /**
     * 接上面的 onStartNestedScroll 返回true 会到这里
     * @param coordinatorLayout
     * @param child
     * @param directTargetChild
     * @param target
     * @param axes
     * @param type
     */
    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull NestedScrollView child, @NonNull View directTargetChild, @NonNull View target,
                                       int axes, int type) {
        scroller.abortAnimation();
        scrollerRefresh.forceFinished(true);
        isNestScrolling=false;
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    /**
     * 开始嵌套滑动了 这里会把手指在屏幕上的滑动距离返回回来  dy>0向上滑动  dy<0向下滑动  translationY 下正 上负   ，这个方法滑动完了，才会滑动chid  也就是NestScrollerView  然后才是 onNestedScroll
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dx
     * @param dy
     * @param consumed  你的逻辑消耗了多少  0 x方向 1 y方向  正负有影响的 xd  / 如果consumed[1]=dy 说明你用完了 这里 child就不会动了
     * @param type
     */
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                  @NonNull NestedScrollView child, @NonNull View target, int dx, int dy,
                                  @NonNull int[] consumed, int type) {
        View dependView = dependy.get();
        if (dy > 0 && dependView.getTranslationY() <= 0 && (anim_root.getTranslationY() == 0||anim_root.getTranslationY()==anim_root.getMeasuredHeight())) {   //刷新布局未偏移 ， 向上滑动 且 上部分折叠区未折叠或正在折叠
            dy=(int)(Math.abs(dy));
            int currentTranslation = (int) dependView.getTranslationY();
            int targetDy = currentTranslation - dy;
            int concorrect = targetDy < -maxFunctionCollaped ? -maxFunctionCollaped : targetDy;
            dependView.setTranslationY(concorrect);
            consumed[1] = currentTranslation - concorrect;
        } else if (dy < 0 && dependView.getTranslationY() >= 0) {// 向下滑动 且上部分折叠区未折叠
            int currentTranslationY = (int) anim_root.getTranslationY();
            dy=-(int)(Math.abs(dy)*0.6f);
            float calculate = currentTranslationY - dy > anim_root.getMeasuredHeight() ? anim_root.getMeasuredHeight() : currentTranslationY - dy ;
           //calculate+=(calculate/anim_root.getMeasuredHeight())*dy; //阻尼效果
            anim_root.setTranslationY(calculate);
            scroll_content.setTranslationY(calculate);
            consumed[1] = (int) (calculate - currentTranslationY);
        } else if (dy > 0 && anim_root.getTranslationY() > 0) {  //向上滑动 ，刷新布局已经划出来
            int currentTranslationY = (int) anim_root.getTranslationY();
            int calculate = currentTranslationY - dy >= 0 ? currentTranslationY - dy : 0;
            anim_root.setTranslationY(calculate);
            scroll_content.setTranslationY(calculate);
            consumed[1] = dy;
        }

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    /**
     * childview 滑动结束后  dyUnconsumed ：还未用完的偏移量  向上为正,向下为负
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed
     * @param dyConsumed
     * @param dxUnconsumed
     * @param dyUnconsumed
     * @param type
     */
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull NestedScrollView child, @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        if (dyUnconsumed < 0 && dependy.get().getTranslationY() < 0) {
            View dependView = dependy.get();
            int currentTranslation = (int) dependView.getTranslationY();
            int targetDy = currentTranslation - dyUnconsumed;
            int concorrect = targetDy > 0 ? 0 : targetDy;
            dependView.setTranslationY(concorrect);
            dyConsumed = currentTranslation - concorrect;
            isNestScrolling=true;
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed, type);
    }

    /**
     * 滑动松手--- 惯性滑动
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout,
                                    @NonNull NestedScrollView child, @NonNull View target, float velocityX, float velocityY) {
        onRefreshEnd();
        Log.d("onNestedStroll","fling");
        return onDragEnd(velocityY);
    }

    /**
     * 嵌套滑动结束
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param type
     */
    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                   @NonNull NestedScrollView child, @NonNull View target, int type) {
        Log.d("onNestedStroll","onStopNestedScroll");
        onRefreshEnd();
        onDragEnd(600);
        isNestScrolling=false;
    }

    /**
     * 处理刷新控件拖动释放
     *
     * @param
     */
    private void onRefreshEnd() {
        if (anim_root.getTranslationY() <= 0&&!scrollerRefresh.isFinished()) { //控件拖出来了才处理
            return;
        }
        if (scrollerRefresh.computeScrollOffset()) {
            return;
        }
        if (valueAnimator != null && valueAnimator.isRunning()) {  //停止正在执行的动画
           return;
        }
        if (anim_root.getTranslationY() >= anim_root.getMeasuredHeight() * 0.5f) { //拖出一般则全部拖出
            //todo
            scrollerRefresh.startScroll(0, (int) anim_root.getTranslationY(), 0, anim_root.getMeasuredHeight() - (int) anim_root.getTranslationY(), 250);
        } else { //拖出部分则收回去
            scrollerRefresh.startScroll(0, (int) anim_root.getTranslationY(), 0, 0 - (int) anim_root.getTranslationY(), 250);
        }
        if (valueAnimator != null && valueAnimator.isRunning()) {  //停止正在执行的动画
            valueAnimator.cancel();
        }
        handler.post(refreshEndRunnalbe);
    }

    //刷新动画 Runnable
    private Runnable refreshEndRunnalbe = new Runnable() {

        @Override
        public void run() {
            if (scrollerRefresh.computeScrollOffset()) {  //滑动动画执行中
                anim_root.setTranslationY(scrollerRefresh.getCurrY());
                scroll_content.setTranslationY(scrollerRefresh.getCurrY());
                handler.post(this);
            } else { //滑动结束
                if (anim_root.getTranslationY() ==anim_root.getMeasuredHeight()) {//滑动结束且偏移量未正 则执行刷新动画
                    valueAnimator = ValueAnimator.ofFloat(0, (float) Math.PI * 2);
                    valueAnimator.setDuration(5000);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
//                            anim_root.findViewById(R.id.iv_img).setTranslationX((float) (Math.sin(value) * 200));
                            anim_root.findViewById(R.id.iv_img).setTranslationY((float) (Math.sin(value) * 25));
                            anim_root.findViewById(R.id.iv_img).setScaleX((float) (Math.cos(value)));
                            anim_root.findViewById(R.id.iv_img).setScaleY((float) (Math.cos(value)));
                            if (value == (float) Math.PI * 2) {
                                scrollerRefresh.startScroll(0, (int) anim_root.getTranslationY(), 0, 0 - (int) anim_root.getTranslationY(), 250);
                                handler.post(refreshEndRunnalbe);
                            }
                        }
                    });
                    valueAnimator.start();
                }
            }
        }
    };

    /**
     * 嵌套滑动 结束   弹性动画  runnable
     */
    private Runnable scrollerRun = new Runnable() {
        @Override
        public void run() {
            boolean compute = scroller.computeScrollOffset();
            Log.d("compute", ": " + compute);
            if (compute) {
                dependy.get().setTranslationY(scroller.getCurrY());
                Log.d("compute", ":getCurrY " + scroller.getCurrY());
                handler.post(this);
            }
        }
    };

    /**
     * //嵌套滑动结束处理
     * @param velocityY
     * @return
     */
    private boolean onDragEnd(float velocityY) {
        View dependView = dependy.get();
        int currentTranslation = (int) dependView.getTranslationY();
        if (currentTranslation == 0 || Math.abs(currentTranslation) >= maxFunctionCollaped) {
            return false;
        }

        if (Math.abs(velocityY) < 800) {//速度小 则以当前位置判断
            if (Math.abs(currentTranslation) > maxFunctionCollaped * 0.5f) {
                scroller.startScroll(0, currentTranslation, 0, -maxFunctionCollaped - currentTranslation,
                        (1000000 / Math.abs(800)));
            } else {
                scroller.startScroll(0, currentTranslation, 0, -0 - currentTranslation,
                        (1000000 / Math.abs(800)));
            }
        } else { //速度够大 以速度值做依据
            if (velocityY > 0) {
                scroller.startScroll(0, currentTranslation, 0, -maxFunctionCollaped - currentTranslation,
                        (int) (1000000 / Math.abs(velocityY)));
            } else {
                scroller.startScroll(0, currentTranslation, 0, -0 - currentTranslation,
                        (int) (1000000 / Math.abs(velocityY)));
            }
        }

        handler.post(scrollerRun);
        return true;
    }
}
