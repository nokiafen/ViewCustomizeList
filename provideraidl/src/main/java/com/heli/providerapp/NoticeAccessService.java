package com.heli.providerapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.heli.providerapp.IAccessAidlInterface;
import com.heli.providerapp.IAccessSetInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class NoticeAccessService extends Service {
  private  static  final  String TAG="NoticeAccessService";
  private PackageManager pm;
  public static boolean isInit = false;
  private String[] arrayAppAcDes;

  @Override
  public IBinder onBind(Intent intent) {
    return mIBinder;
  }

  @Override public void onCreate() {
    super.onCreate();
    init();
  }

  private void init() {
    pm = this.getPackageManager();
    checkSystemAccessOrCreate();
    checkAppAccessFields();
    checkAppAccessorCreate();
    registerPackageReceiver();
    isInit = true;
  }

  /**
   * add columns for  new appAccess fields
   */
  private void checkAppAccessFields() {
    arrayAppAcDes = getResources().getStringArray(R.array.app_access_config);
    DBUtils.getInstance()
        .checkTableCul(NoticeAccessSQLOpenHelper.TABLE_NAME, arrayAppAcDes, "integer default 1");
  }

  private void registerPackageReceiver() {
    IntentFilter filter = new IntentFilter();
    filter.addAction("android.intent.action.PACKAGE_ADDED");
    filter.addAction("android.intent.action.PACKAGE_REMOVED");
    filter.addDataScheme("package");
    this.registerReceiver(packageChangedReceiver, filter);
  }

  private void checkAppAccessorCreate() {
    List<ApplicationInfo> appInfos = getRecentAppInfo();
    List<AppAceessBean> appAceessBeanlist = getAppAccessInfo();
    if (appAceessBeanlist == null) {
      createRecentAppAccess(appInfos);
    } else {
      tryToSyncRecentAppState(appInfos, appAceessBeanlist);
    }
  }

  private void createRecentAppAccess(List<ApplicationInfo> appInfos) {
    for (ApplicationInfo appInfo : appInfos) {
      NoticeAccessHelper.getInstance()
          .addAppInfo(this, appInfo.packageName, appInfo.loadLabel(pm).toString(), appInfo.icon);
    }
  }

  private Map checkSystemAccessOrCreate() {
    Map systemAccessess = NoticeAccessHelper.getInstance().querryAllSystemAccess(this);
    String[] array = getResources().getStringArray(R.array.system_access_config);
    if (systemAccessess == null) { //first time insert
      for (int i = 0; i < array.length; i++) {  //insert initiatal system access data
        NoticeAccessHelper.getInstance().addSystemInfo(this, array[i]);
      }
    } else {  // find new row to add
      for (String s : array) {
        if (!systemAccessess.containsKey(s)) {
          NoticeAccessHelper.getInstance().addSystemInfo(this, s);
        }
      }
    }

    if (systemAccessess == null) {
      systemAccessess = NoticeAccessHelper.getInstance().querryAllSystemAccess(this);
    }
    return systemAccessess;
  }

  private List<ApplicationInfo> getRecentAppInfo() {
    List<ApplicationInfo> appInfos = pm.getInstalledApplications(
        PackageManager.GET_UNINSTALLED_PACKAGES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
    List<ApplicationInfo> applicationInfos = new ArrayList<>();

    // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
    Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
    resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);

    // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
    List<ResolveInfo> resolveinfoList = getPackageManager()
        .queryIntentActivities(resolveIntent, 0);
    Set<String> allowPackages = new HashSet();
    for (ResolveInfo resolveInfo : resolveinfoList) {
      allowPackages.add(resolveInfo.activityInfo.packageName);
    }

    for (ApplicationInfo app : appInfos) {
      if (allowPackages.contains(app.packageName)) {
        applicationInfos.add(app);
      }
    }
    return applicationInfos;
  }

  private List<AppAceessBean> getAppAccessInfo() {
    List<AppAceessBean> appAceessBeanList =
        NoticeAccessHelper.getInstance().appQuerryAll(this, arrayAppAcDes);
    return appAceessBeanList;
  }

  private void tryToSyncRecentAppState(List<ApplicationInfo> appInfos,
      List<AppAceessBean> appAceessBeanlist) {
    //todo
    for (ApplicationInfo appInfo : appInfos) {   // app installed then insert row to  appAccesstable
      AppAceessBean appAceessBean = NoticeAccessHelper.getInstance()
          .qppQuerrySingle(this, appInfo.packageName, arrayAppAcDes);
      if (appAceessBean == null) { //table without record -->add
        NoticeAccessHelper.getInstance()
            .addAppInfo(this, appInfo.packageName,  appInfo.loadLabel(pm).toString(), appInfo.icon);
      }
    }

    for (AppAceessBean appAceessBean : appAceessBeanlist) {  // app loss then delete this access info
      boolean appAccessContained = false;
      for (ApplicationInfo appInfo : appInfos) {
        if (appInfo.packageName.equals(appAceessBean.packageName)) {
          appAccessContained = true;
        }
      }
      if (!appAccessContained) {
        NoticeAccessHelper.getInstance().deleteAppInfo(this, appAceessBean);
      }
    }
  }

  private BroadcastReceiver packageChangedReceiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
        String packageName = intent.getData().getSchemeSpecificPart();
        ApplicationInfo applicationInfo=null;
        try {
          applicationInfo=pm.getPackageInfo(packageName, 0).applicationInfo;
        } catch (PackageManager.NameNotFoundException e) {
          e.printStackTrace();
        }
        if (applicationInfo!=null&&isPackageCanOpened(packageName)) {
          AppAceessBean appAceessBean = NoticeAccessHelper.getInstance()
              .qppQuerrySingle(NoticeAccessService.this, packageName, arrayAppAcDes);
          if (appAceessBean == null) { //table without record -->add
            NoticeAccessHelper.getInstance()
                .addAppInfo(NoticeAccessService.this, packageName,
                    applicationInfo.loadLabel(pm).toString(),applicationInfo.icon);
          }
        }
      }
      if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
        String packageName = intent.getData().getSchemeSpecificPart();
        AppAceessBean appAceessBean = NoticeAccessHelper.getInstance()
            .qppQuerrySingle(NoticeAccessService.this, packageName, arrayAppAcDes);
        if (appAceessBean != null) { //table without record -->add
          NoticeAccessHelper.getInstance().deleteAppInfo(NoticeAccessService.this, appAceessBean);
        }
      }
      if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
        String packageName = intent.getData().getSchemeSpecificPart();
      }
    }
  };

  @Override public void onDestroy() {
    this.unregisterReceiver(packageChangedReceiver);
    mAccessSetInterface=null;
    super.onDestroy();
  }

  private boolean isPackageCanOpened(String packageName) {
    Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
    resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
    List<ResolveInfo> resolveinfoList = getPackageManager()
        .queryIntentActivities(resolveIntent, 0);
    Set<String> allowPackages = new HashSet();
    for (ResolveInfo resolveInfo : resolveinfoList) {
      allowPackages.add(resolveInfo.activityInfo.packageName);
    }
    return allowPackages.contains(packageName);
  }

  //BinderCreate
  IAccessAidlInterface.Stub  mIBinder =new IAccessAidlInterface.Stub() {
    @Override public void registerIAccessCallBack(IAccessSetInterface iAccessSetInterface)
        throws RemoteException {
        //todo
      if (iAccessSetInterface!=null) {
        Log.d(TAG,"iAccessSetInterface has registered");
        mAccessSetInterface=iAccessSetInterface;
      }
      //iAccessSetInterface.appAccessChanged("gms","total",true);
    }
  };

  private  static   IAccessSetInterface mAccessSetInterface;

  public static void notifyCommonAcChanged(String accessName ,boolean accessON){
    if (mAccessSetInterface==null) {
      Log.d(TAG,"iAccessSetInterface has been null");
      return;
    }
    try {
      mAccessSetInterface.commonAccessChanged(accessName,accessON);
      Log.d(TAG,"commonAccessChanged notified :"+ "accessName :"+ accessName+" accessON: "+accessON);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public static void notifyAPPAcChanged(String packageName,String accessName,boolean accessON){
    if (mAccessSetInterface==null) {
      Log.d(TAG,"iAccessSetInterface has been null");
      return;
    }
    try {
      mAccessSetInterface.appAccessChanged(packageName,accessName,accessON);
      Log.d(TAG,"notifyAPPAcChanged notified :"+ "packageName :"+ packageName+" accessName: "+accessName+ " accessON: "+accessON);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public  static  void resetAccesses(Context mContext){
    NoticeAccessHelper.getInstance().resetAccessData(mContext);
  }


}
