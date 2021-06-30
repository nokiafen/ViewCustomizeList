package com.heli.providerapp.acticity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import com.heli.providerapp.NoticeAccessService;
import com.facebook.stetho.Stetho;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MyApplication extends Application {

  private static Context mContext;
  public static Context getContext() {
    return  mContext;
  }

  @Override public void onCreate() {
    super.onCreate();
    mContext=this;
    Stetho.initializeWithDefaults(this);
    startService(new Intent(getApplicationContext(), NoticeAccessService.class));
  }
}
