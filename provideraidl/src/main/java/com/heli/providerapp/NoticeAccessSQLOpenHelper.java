package com.heli.providerapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class NoticeAccessSQLOpenHelper extends SQLiteOpenHelper {

  //数据库名称
  private static final String DB_NAME = "app_notice.db";
  //表名称
  public static final String TABLE_NAME = "app_info";

  //表名称
  public static final String TABLE_NAME_SYS_ACCESS = "system_info";

  private static final int DB_VERSION = 1;

  /**
   * 数据库的构造函数
   *
   * @param context name 数据库名称 factory 游标工程 version 数据库的版本号 不可以小于1
   */
  private NoticeAccessSQLOpenHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  private static NoticeAccessSQLOpenHelper noticeAccessSQLOpenHelper;

  public static NoticeAccessSQLOpenHelper getInstance(Context context) {
    synchronized (NoticeAccessSQLOpenHelper.class) {
      if (noticeAccessSQLOpenHelper == null) {
        noticeAccessSQLOpenHelper = new NoticeAccessSQLOpenHelper(context);
      }
    }
    return noticeAccessSQLOpenHelper;
  }

  /**
   * 数据库第一次创建时回调此方法. 初始化一些表
   */
  @Override
  public void onCreate(SQLiteDatabase db) {

    // 操作数据库
    createAppInfoTab(db);
    createSysInfoTab(db);
  }

  /**
   * 数据库的版本号更新时回调此方法, 更新数据库的内容(删除表, 添加表, 修改表)
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SYS_ACCESS);
    onCreate(db);
  }

  private void createAppInfoTab(SQLiteDatabase db) {
    String sql = "create table " + TABLE_NAME + "("
        + AppNoticeAccess.KEY_ID + " integer primary key autoincrement, "
        + "" + AppNoticeAccess.KEY_PACKAGE_NAME + " varchar(256), "
        + "" + AppNoticeAccess.KEY_APP_NAME + " varchar(256), "
        + "" + AppNoticeAccess.KEY_APP_ICON + " integer default 0, "
        + "" + AppNoticeAccess.KEY_NOTICE + " integer default 1);";
    db.execSQL(sql); // 创建person表
    Log.i(getClass().getSimpleName(), sql);
  }

  private void createSysInfoTab(SQLiteDatabase db) {
    String sql = "create table " + TABLE_NAME_SYS_ACCESS + "("
        + AppNoticeAccess.KEY_ID + " integer primary key autoincrement, "
        + "" + AppNoticeAccess.KEY_ACCESS_TITLE + " varchar(256), "
        + "" + AppNoticeAccess.KEY_ACCESS + "  integer default 1 );";
    db.execSQL(sql); // 创建person表
    Log.i(getClass().getSimpleName(), sql);
  }
}
