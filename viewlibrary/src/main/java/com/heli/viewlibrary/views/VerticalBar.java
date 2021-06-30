package com.heli.viewlibrary.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.CycleInterpolator;
import com.heli.viewlibrary.utils.Util;
import com.heli.viewlibrary.R;
import java.util.Random;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/08/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class VerticalBar extends View {
  private int backColor;
  private int drawColor;

  private int viewWith;
  private int viewHeight;

  private int defaultWith;
  private int defaultHeight;

  private Paint backPaint;
  private Paint drawPaint;
  private int progress;
  private final static int max_progress = 100;
  private Handler handler = new Handler();
  private Random random = new Random();
  private int startGradientColor;

  public VerticalBar(Context context) {
    super(context);
  }

  public VerticalBar(Context context,
      AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  private void init(AttributeSet attrs) {

    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.VerticalBar);
    drawColor = typedArray.getColor(R.styleable.VerticalBar_bar_color, Color.WHITE);
    backColor = typedArray.getColor(R.styleable.VerticalBar_back_color, Color.GRAY);
    startGradientColor = typedArray.getColor(R.styleable.VerticalBar_bar_start_color, Color.GRAY);
    progress = typedArray.getInt(R.styleable.VerticalBar_progress, 0);

    defaultWith = Util.dp2px(getContext(), 30);
    defaultHeight = Util.dp2px(getContext(), 200);
    backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    backPaint.setColor(backColor);
    backPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    drawPaint.setColor(drawColor);
    drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    typedArray.recycle();
    //handler.postDelayed(runnable, 300);
  }

  private Runnable runnable = new Runnable() {
    @Override public void run() {
        int value=random.nextInt(100);
        setProgress(value);
      postDelayed(runnable, 200);
      new CycleInterpolator(9){
        @Override public float getInterpolation(float input) {
          return super.getInterpolation(input);
        }
      };
    }
  };



  public VerticalBar(Context context,
      AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    viewWith = measureSelf(widthMeasureSpec, defaultWith);
    viewHeight = measureSelf(heightMeasureSpec, defaultHeight);
    setMeasuredDimension(viewWith, viewHeight);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawBackGround(canvas);
    drawBar(canvas);
  }

  private void drawBar(Canvas canvas) {
    drawPaint.setShader(new LinearGradient(0, viewHeight * (1 - progress / (float) max_progress),
        0, viewHeight, drawColor, startGradientColor, Shader.TileMode.CLAMP));
    canvas.drawRoundRect(0, viewHeight * (1 - progress / (float) max_progress), viewWith,
        viewHeight, 0, 0, drawPaint);
  }

  private void drawBackGround(Canvas canvas) {
    //canvas.rotate(-90);
    canvas.drawRoundRect(0, 0, viewWith, viewHeight, 0, 0, backPaint);
    //canvas.save();
  }

  private int measureSelf(int measureSpec, int defaultValue) {
    int resultSize = defaultValue;
    int mode = MeasureSpec.getMode(measureSpec);
    int measureSize = MeasureSpec.getSize(measureSpec);
    switch (mode) {
      case MeasureSpec.EXACTLY:
        resultSize = measureSize;
        break;
      case MeasureSpec.AT_MOST:
        resultSize = Math.min(measureSize, defaultValue);
        break;
    }
    return resultSize;
  }

  private float downY = 0;
  private boolean veticalDrad = false;

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    float currentY = event.getY();
    float progress = (viewHeight - currentY) / (float) viewHeight * 100;
    progress = progress >= 100 ? 100 : progress;

    switch (event.getAction()) {
      case MotionEvent.ACTION_UP:
        setProgress((int) progress);
        getParent().requestDisallowInterceptTouchEvent(false);
        veticalDrad = false;
      case MotionEvent.ACTION_CANCEL:
        veticalDrad = false;
        break;
      case MotionEvent.ACTION_DOWN:

        downY = event.getY();
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
      case MotionEvent.ACTION_MOVE:
        if (veticalDrad) {
          setProgress((int) progress);
          getParent().requestDisallowInterceptTouchEvent(true);
        } else {
          if (Math.abs(event.getY() - downY)
              >= ViewConfiguration.get(getContext()).getScaledTouchSlop() * 2) {
            setProgress((int) progress);
            getParent().requestDisallowInterceptTouchEvent(true);
            veticalDrad = true;
          } else {
            getParent().requestDisallowInterceptTouchEvent(false);
            veticalDrad = false;
          }
        }
        break;

      //
    }

    return super.onTouchEvent(event);
  }

  public void setProgress(int progress) {
    this.progress = progress;
    postInvalidate();
  }




  // Animation
  public void  startAim(int progress){
    ValueAnimator valueAnimator=ValueAnimator.ofFloat(progress/(float)max_progress*3.14f,3.14f);
    valueAnimator.setDuration(800);
    valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    valueAnimator.setRepeatMode(ValueAnimator.RESTART);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float value= (float) animation.getAnimatedValue();
        Log.d("value",value+"");

       int valueProgress= (int) (Math.sin(value)*100);
        Log.d("valueProgress",valueProgress+"");
        setProgress(valueProgress);
      }
    });
    valueAnimator.start();
  }



}
