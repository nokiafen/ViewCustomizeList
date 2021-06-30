package com.heli.elemehomepage;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * jbl_jm
 */
public class MyApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();
    resetDensity();
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    resetDensity();
  }

  private void resetDensity() {
    Point size = new Point();
    float withInDp=getResources().getDisplayMetrics().widthPixels/getResources().getDisplayMetrics().density;  //当前设备宽度   换算成dp为单位
    int designWithDp=360; //设计图 尺寸 dp单位
    float scaleFactor=withInDp/(float)designWithDp;  //缩放因子
    ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(size);
    getResources().getDisplayMetrics().density = getResources().getDisplayMetrics().density*scaleFactor ;
    getResources().getDisplayMetrics().scaledDensity = getResources().getDisplayMetrics().scaledDensity*scaleFactor;
    // hdpi->mdpi dension
    //前面这个1.5f  >>>>前期布局文件标注 是以 1920x1080  hdpi 标准的  乘以1.5 用来换算成 mdpi 标准下的尺寸 ;不换算的话designWidth=1920/1.5;


  }
}
