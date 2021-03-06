package com.heli.providerapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AppAccessInfoProvider extends ContentProvider {
  private static  final String TAG="AppAccessInfoProvider";
  private static final int APP_ACCESS_INSERT_CODE = 0;
  private static final int APP_ACCESS_DELETE_CODE = 1;
  private static final int APP_ACCESS_UPDATE_CODE = 2;
  private static final int APP_ACCESS_QUERY_ALL_CODE = 3;
  private static final int APP_ACCESS_QUERY_ITEM_CODE = 4;

  private static final int SYS_ACCESS_INSERT_CODE = 5;
  private static final int SYS_ACCESS_DELETE_CODE = 6;
  private static final int SYS_ACCESS_UPDATE_CODE = 7;
  private static final int SYS_ACCESS_QUERY_ALL_CODE = 8;
  private static final int SYS_ACCESS_QUERY_ITEM_CODE = 9;
  //
  private static UriMatcher uriMatcher;
  private NoticeAccessSQLOpenHelper mOpenHelper;

  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    uriMatcher.addURI(AppNoticeAccess.AUTHORITY, AppNoticeAccess.PATH_INSERT_APP,
        APP_ACCESS_INSERT_CODE);
    uriMatcher.addURI(AppNoticeAccess.AUTHORITY, AppNoticeAccess.PATH_DELETE_APP,
        APP_ACCESS_DELETE_CODE);
    uriMatcher.addURI(AppNoticeAccess.AUTHORITY, AppNoticeAccess.PATH_UPDATE_APP,
        APP_ACCESS_UPDATE_CODE);
    uriMatcher.addURI(AppNoticeAccess.AUTHORITY, AppNoticeAccess.PATH_QUERY_ALL_APP,
        APP_ACCESS_QUERY_ALL_CODE);
    uriMatcher.addURI(AppNoticeAccess.AUTHORITY, AppNoticeAccess.PATH_QUERY_ITEM_APP,
        APP_ACCESS_QUERY_ITEM_CODE);

    uriMatcher.addURI(AppNoticeAccess.AUTHORITY, AppNoticeAccess.PATH_INSERT_SYSTEM,
        SYS_ACCESS_INSERT_CODE);
    uriMatcher.addURI(AppNoticeAccess.AUTHORITY, AppNoticeAccess.PATH_DELETE_SYSTEM,
        SYS_ACCESS_DELETE_CODE);
    uriMatcher.addURI(AppNoticeAccess.AUTHORITY, AppNoticeAccess.PATH_UPDATE_SYSTEM,
        SYS_ACCESS_UPDATE_CODE);
    uriMatcher.addURI(AppNoticeAccess.AUTHORITY, AppNoticeAccess.PATH_QUERY_ALL_SYSTEM,
        SYS_ACCESS_QUERY_ALL_CODE);
    uriMatcher.addURI(AppNoticeAccess.AUTHORITY, AppNoticeAccess.PATH_QUERY_ITEM_SYSTEM,
        SYS_ACCESS_QUERY_ITEM_CODE);
  }

  @Override
  public boolean onCreate() {
    mOpenHelper = NoticeAccessSQLOpenHelper.getInstance(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    SQLiteDatabase db = mOpenHelper.getReadableDatabase();
    switch (uriMatcher.match(uri)) {
      case APP_ACCESS_QUERY_ALL_CODE: // ??????????????????uri
        if (db.isOpen()) {
          Cursor cursor = db.query(NoticeAccessSQLOpenHelper.TABLE_NAME,
              projection, selection, selectionArgs, null, null,
              sortOrder);
          cursor.setNotificationUri(getContext().getContentResolver(), uri);
          return cursor;
          // db.close(); ??????cursor????????????, ????????????????????????
        }
        break;
      case APP_ACCESS_QUERY_ITEM_CODE: // ????????????????????????, uri??????????????????id
        if (db.isOpen()) {

          long id = ContentUris.parseId(uri);

          Cursor cursor = db.query(NoticeAccessSQLOpenHelper.TABLE_NAME,
              projection, AppNoticeAccess.KEY_ID + " = ?", new String[] {
                  id
                      + ""
              }, null, null, sortOrder);
          cursor.setNotificationUri(getContext().getContentResolver(), uri);
          return cursor;
        }
        break;
      case SYS_ACCESS_QUERY_ALL_CODE: // ???????????? uri
        if (db.isOpen()) {
          Cursor cursor = db.query(NoticeAccessSQLOpenHelper.TABLE_NAME_SYS_ACCESS,
              projection, selection, selectionArgs, null, null,
              sortOrder);
          cursor.setNotificationUri(getContext().getContentResolver(), uri);
          return cursor;
          // db.close(); ??????cursor????????????, ????????????????????????
        }
        break;
      case SYS_ACCESS_QUERY_ITEM_CODE: // ????????????????????????, uri??????????????????id
        if (db.isOpen()) {

          long id = ContentUris.parseId(uri);

          Cursor cursor = db.query(NoticeAccessSQLOpenHelper.TABLE_NAME_SYS_ACCESS,
              projection, AppNoticeAccess.KEY_ID + " = ?", new String[] {
                  id
                      + ""
              }, null, null, sortOrder);
          cursor.setNotificationUri(getContext().getContentResolver(), uri);
          return cursor;
        }
        break;
      default:
        throw new IllegalArgumentException("uri?????????: " + uri);
    }
    return null;
  }

  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      case APP_ACCESS_QUERY_ALL_CODE: // ???????????????MIME-type
        return "vnd.android.cursor.dir/person";
      case APP_ACCESS_QUERY_ITEM_CODE: // ???????????????MIME-TYPE
        return "vnd.android.cursor.item/person";
      case SYS_ACCESS_QUERY_ALL_CODE: // ???????????????MIME-type
        return "vnd.android.cursor.dir/person";
      case SYS_ACCESS_QUERY_ITEM_CODE: // ???????????????MIME-TYPE
        return "vnd.android.cursor.item/person";
      default:
        break;
    }
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {

    switch (uriMatcher.match(uri)) {
      case APP_ACCESS_INSERT_CODE: // ????????????access_app??????
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        if (db.isOpen()) {

          long id = db.insert(NoticeAccessSQLOpenHelper.TABLE_NAME, null,
              values);

          db.close();
          Uri newUri = ContentUris.withAppendedId(uri, id);
          //???????????????????????????????????????
          getContext().getContentResolver().notifyChange(newUri, null);
          return newUri;
        }
        break;
      case SYS_ACCESS_INSERT_CODE: // ????????????access_app??????
        SQLiteDatabase dbs = mOpenHelper.getWritableDatabase();

        if (dbs.isOpen()) {

          long id = dbs.insert(NoticeAccessSQLOpenHelper.TABLE_NAME_SYS_ACCESS, null,
              values);

          dbs.close();
          Uri newUri = ContentUris.withAppendedId(uri, id);
          //???????????????????????????????????????
          getContext().getContentResolver().notifyChange(newUri, null);
          return newUri;
        }
        break;
      default:
        throw new IllegalArgumentException("uri?????????: " + uri);
    }
    return null;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    switch (uriMatcher.match(uri)) {
      case APP_ACCESS_DELETE_CODE: // ???person???????????????????????????
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
          int count = db.delete(NoticeAccessSQLOpenHelper.TABLE_NAME,
              selection, selectionArgs);
          db.close();
          //???????????????????????????????????????
          getContext().getContentResolver().notifyChange(uri, null);
          return count;
        }
        break;
      case SYS_ACCESS_DELETE_CODE: // ???person???????????????????????????
        SQLiteDatabase dbs = mOpenHelper.getWritableDatabase();
        if (dbs.isOpen()) {
          int count = dbs.delete(NoticeAccessSQLOpenHelper.TABLE_NAME_SYS_ACCESS,
              selection, selectionArgs);
          dbs.close();
          //???????????????????????????????????????
          getContext().getContentResolver().notifyChange(uri, null);
          return count;
        }
        break;
      default:
        throw new IllegalArgumentException("uri?????????: " + uri);
    }
    return 0;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {
    switch (uriMatcher.match(uri)) {
      case APP_ACCESS_UPDATE_CODE: // ??????person????????????
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
          int count = db.update(NoticeAccessSQLOpenHelper.TABLE_NAME,
              values, selection, selectionArgs);
          db.close();
          //???????????????????????????????????????
          uri=Uri.parse(uri.toString()+"/"+values.get(AppNoticeAccess.KEY_PACKAGE_NAME)+"/ms");
          getContext().getContentResolver().notifyChange(uri, null);
          return count;
        }
        break;
      case SYS_ACCESS_UPDATE_CODE: // ??????person????????????
        SQLiteDatabase dbs = mOpenHelper.getWritableDatabase();
        if (dbs.isOpen()) {
          int count = dbs.update(NoticeAccessSQLOpenHelper.TABLE_NAME_SYS_ACCESS,
              values, selection, selectionArgs);
          dbs.close();
          //???????????????????????????????????????
          getContext().getContentResolver().notifyChange(uri, null);
          return count;
        }
        break;
      default:
        throw new IllegalArgumentException("uri?????????: " + uri);
    }
    return 0;
  }
}
