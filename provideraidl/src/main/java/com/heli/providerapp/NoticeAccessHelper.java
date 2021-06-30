package com.heli.providerapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class NoticeAccessHelper {
  private final static String TAG = "NoticeAccessHelper";
  private static NoticeAccessHelper noticeAccessHelper;

  public static NoticeAccessHelper getInstance() {
    synchronized (NoticeAccessHelper.class) {
      if (noticeAccessHelper == null) {
        noticeAccessHelper = new NoticeAccessHelper();
      }
      return noticeAccessHelper;
    }
  }

  // APP ACCESS START HERE

  /**
   * addAppInfo
   */
  public void addAppInfo(Context context, String packageName, String channelName, int channelIcon) {
    ContentResolver resolver = context.getContentResolver();
    ContentValues values = new ContentValues();
    values.put(AppNoticeAccess.KEY_PACKAGE_NAME, packageName);
    values.put(AppNoticeAccess.KEY_APP_NAME, channelName);
    values.put(AppNoticeAccess.KEY_APP_ICON, channelIcon);
    Uri uri = resolver.insert(AppNoticeAccess.CONTENT_URI_INSERT_APP, values);
    Log.i(TAG, "uri: " + uri);
    long id = ContentUris.parseId(uri);
    Log.i(TAG, "添加到: " + id);
  }

  /**
   * deleteAppInfo
   */
  public void deleteAppInfo(Context context, AppAceessBean appAceessBean) {
    ContentResolver resolver = context.getContentResolver();
    String where = AppNoticeAccess.KEY_PACKAGE_NAME + " = ?";
    String[] selectionArgs = { appAceessBean.packageName };
    int count = resolver.delete(AppNoticeAccess.CONTENT_URI_DELETE_APP, where,
        selectionArgs);
    Log.i(TAG, "删除行: " + count);
  }

  /**
   * Querry Single one App Access
   */
  public AppAceessBean qppQuerrySingle(Context context, String packageNameHere,
      String[] accesFields) {
    AppAceessBean result = null;
    // 内容提供者访问对象
    ContentResolver resolver = context.getContentResolver();

    Cursor cursor = resolver
        .query(AppNoticeAccess.CONTENT_URI_QUERY_ALL_APP, null,
            AppNoticeAccess.KEY_PACKAGE_NAME + " = ?",
            new String[] { packageNameHere }, "_id asc");
    Map<String, Integer> map;
    if (cursor != null && cursor.moveToFirst()) {
      map = new HashMap<>();
      int id = cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_ID));
      String packageName =
          cursor.getString(cursor.getColumnIndex(AppNoticeAccess.KEY_PACKAGE_NAME));
      int icon = cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_APP_ICON));
      String name = cursor.getString(cursor.getColumnIndex(AppNoticeAccess.KEY_APP_NAME));
      for (String accesField : accesFields) {
        map.put(accesField, cursor.getInt(cursor.getColumnIndex(accesField)));
      }
      result = new AppAceessBean(id, packageName, map, icon, name);

      Log.i(TAG, "id: " + id + ", packageName: " + packageName + ", on: ");
    }
    return result;
  }

  /**
   * Querry All app access
   */
  public List<AppAceessBean> appQuerryAll(Context context, String[] appAccessFields) {
    Log.d(TAG, appAccessFields.toString());
    // 内容提供者访问对象
    List<AppAceessBean> result = null;
    ContentResolver resolver = context.getContentResolver();
    Cursor cursor = resolver
        .query(AppNoticeAccess.CONTENT_URI_QUERY_ALL_APP, null, null,
            null, "_id asc");
    if (cursor != null && cursor.getCount() > 0) {
      result = new ArrayList<>();
      AppAceessBean appAceessBean;
      Map<String, Integer> map;
      while (cursor.moveToNext()) {
        map = new HashMap<>();
        int id = cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_ID));
        String packageName =
            cursor.getString(cursor.getColumnIndex(AppNoticeAccess.KEY_PACKAGE_NAME));
        int icon = cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_APP_ICON));
        String name = cursor.getString(cursor.getColumnIndex(AppNoticeAccess.KEY_APP_NAME));
        for (String accesField : appAccessFields) {
          Log.d(TAG, accesField);
          map.put(accesField, cursor.getInt(cursor.getColumnIndex(accesField)));
        }
        appAceessBean = new AppAceessBean(id, packageName, map, icon, name);
        result.add(appAceessBean);
      }
      cursor.close();
    }
    return result;
  }

  /**
   * @param value content of fieldArray  in contentValue
   */
  public void appInfoUpdate(Context context, String packageName, String[] fieldArray, int[] value) {
    // 内容提供者访问对象
    ContentResolver resolver = context.getContentResolver();
    ContentValues values = new ContentValues();
    values.put(AppNoticeAccess.KEY_PACKAGE_NAME,packageName);
    for (int i = 0; i < fieldArray.length; i++) {
      values.put(fieldArray[i], value[i]);
    }

    int count = resolver.update(AppNoticeAccess.CONTENT_URI_UPDATE_APP, values,
        AppNoticeAccess.KEY_PACKAGE_NAME + " = ?", new String[] { packageName });
    Log.i(TAG, "更新行app_access: " + count);
  }

  // SYSTEM_ACCESS       START HEER

  /**
   * Add  one  sys accesses in table
   */
  public void addSystemInfo(Context context, String title) {
    ContentResolver resolver = context.getContentResolver();
    ContentValues values = new ContentValues();
    values.put(AppNoticeAccess.KEY_ACCESS, 1);
    values.put(AppNoticeAccess.KEY_ACCESS_TITLE, title);
    Uri uri = resolver.insert(AppNoticeAccess.CONTENT_URI_INSERT_SYS, values);
    Log.i(TAG, "uri: " + uri);
    long id = ContentUris.parseId(uri);
    Log.i(TAG, "添加sys_access到: " + id);
  }

  /**
   * UpdateSystemAccess
   *
   * @param key reffer to specific system_access
   * @param value 1:on / 0:off
   */
  public void updateSystemAccess(Context context, String key, int value) {
    ContentResolver resolver = context.getContentResolver();

    ContentValues values = new ContentValues();
    values.put(AppNoticeAccess.KEY_ACCESS, value);

    int count = resolver.update(AppNoticeAccess.CONTENT_URI_UPDATE_SYS, values,
        AppNoticeAccess.KEY_ACCESS_TITLE + " = ?", new String[] { key + "" });
    Log.i(TAG, "update app_access: index: " + count);
  }

  /**
   * Delete system_access
   */
  public void deleteSystemAccessPer(Context context, int idIndex) {
    ContentResolver resolver = context.getContentResolver();
    String where = AppNoticeAccess.KEY_ID + " = ?";
    String[] selectionArgs = { idIndex + "" };
    int count = resolver.delete(AppNoticeAccess.CONTENT_URI_DELETE_SYS, where,
        selectionArgs);
    Log.i(TAG, "sys_access_删除行: " + count);
  }

  /**
   * Querry All SystemAccess
   */
  public Map querryAllSystemAccess(Context context) {
    HashMap<String, Integer> maps = null;
    ContentResolver resolver = context.getContentResolver();
    Cursor cursor = resolver
        .query(AppNoticeAccess.CONTENT_URI_QUERY_ALL_SYS, null, null,
            null, "_id asc");
    if (cursor != null && cursor.getCount() > 0) {
      maps = new HashMap<>();
      int id;
      int access;
      String title;
      while (cursor.moveToNext()) {
        id = cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_ID));
        access = cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_ACCESS));
        title = cursor.getString(cursor.getColumnIndex(AppNoticeAccess.KEY_ACCESS_TITLE));
        Log.i(TAG, "id: " + id + ", access: " + access);
        maps.put(title, access);
      }
      cursor.close();
    }
    return maps;
  }

  /**
   * qurrry  one-specific system_access
   */
  public SystemAccessBean.SystemAccessSingle querrySystemAccessSingleItem(Context context,
      String key) {
    SystemAccessBean.SystemAccessSingle systemAccessSingle = null;
    ContentResolver resolver = context.getContentResolver();
    Cursor cursor = resolver
        .query(AppNoticeAccess.CONTENT_URI_QUERY_ALL_SYS, null,
            AppNoticeAccess.KEY_ACCESS_TITLE + "= ?",
            new String[] { key + "", }, "_id asc");
    if (cursor != null && cursor.moveToFirst()) {

      int id = cursor.getInt(0);
      int access = cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_ACCESS));
      systemAccessSingle = new SystemAccessBean.SystemAccessSingle(access == 1);
      cursor.close();
      Log.i(TAG, "id: " + id + ", access: " + access);
    }
    return systemAccessSingle;
  }

  /**
   * desc rest all accesses
   *
   * @param context background
   */
  public void resetAccessData(Context context) {
    ContentResolver resolver = context.getContentResolver();
    ContentValues values = new ContentValues();
    values.put(AppNoticeAccess.KEY_ACCESS, 1);
    int count = resolver.update(AppNoticeAccess.CONTENT_URI_UPDATE_SYS, values,
        AppNoticeAccess.KEY_ID + " >= ?", new String[] { 0 + "" });
    Log.i(TAG, "resetCommonAccessData: " + count);
    // reset AppAccesses
    ContentValues valueAppAc = new ContentValues();
    Cursor cursor = NoticeAccessSQLOpenHelper.getInstance(context)
        .getWritableDatabase()
        .rawQuery("select sql from sqlite_master where type='table' and " +
            "name=?", new String[] { NoticeAccessSQLOpenHelper.TABLE_NAME });
    if (cursor != null && cursor.moveToFirst()) {
      String sql = cursor.getString(0);
      String[] infoLayer = sql.split(",");
      for (int i = 0; i < infoLayer.length; i++) {
        if (i == 0) {
          continue;
        }
        if (infoLayer[i].contains(AppNoticeAccess.KEY_PACKAGE_NAME) ||
            infoLayer[i].contains(AppNoticeAccess.KEY_APP_ICON) ||
            infoLayer[i].contains(AppNoticeAccess.KEY_APP_NAME) ||
            infoLayer[i].contains(AppNoticeAccess.KEY_ID)) {
          continue;
        }
        String columnName = infoLayer[i].split(" ")[1];
        columnName = columnName.replace("[", "");
        columnName = columnName.replace("]", "");
        valueAppAc.put(columnName, 1);
      }
      int appAccessResetcount = resolver.update(AppNoticeAccess.CONTENT_URI_UPDATE_APP, valueAppAc,
          AppNoticeAccess.KEY_ID + " >=?", new String[] { 0 + "" });
      Log.i(TAG, "appAccessResetCount: " + appAccessResetcount);
      cursor.close();
    }
  }
}
