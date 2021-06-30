package com.heli.providerapp.acticity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.heli.providerapp.AppNoticeAccess;
import com.heli.providerapp.R;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TestActivity  extends AppCompatActivity {
private  static final  String TAG="TestActivity";
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_test);
    findViewById(R.id.root_view).setScaleX(-1);
    findViewById(R.id.querrysingle).setRotationY(180);
    //findViewById(R.id.root_view).setScaleY(-1);
  }

  public void add(View view) {
    // 内容提供者访问对象
    ContentResolver resolver = this.getContentResolver();

    for (int i = 0; i < 10; i++) {
      //
      ContentValues values = new ContentValues();
      values.put(AppNoticeAccess.KEY_PACKAGE_NAME, "wanglei"+i);
      values.put(AppNoticeAccess.KEY_NOTICE, 1);
      values.put(AppNoticeAccess.KEY_NOTICE1, 1);
      values.put(AppNoticeAccess.KEY_NOTICE2, 1);
      Uri uri = resolver.insert(AppNoticeAccess.CONTENT_URI_INSERT_APP, values);
      Log.i(TAG, "uri: " + uri);
      long id = ContentUris.parseId(uri);
      Log.i(TAG, "添加到: " + id);
    }
  }

  public void delete(View view) {
    // 内容提供者访问对象
    ContentResolver resolver = this.getContentResolver();
    String where = AppNoticeAccess.KEY_ID + " = ?";
    String[] selectionArgs = { "3" };
    int count = resolver.delete(AppNoticeAccess.CONTENT_URI_DELETE_APP, where,
        selectionArgs);
    Log.i(TAG, "删除行: " + count);
  }

  public void querry(View view) {
    // 内容提供者访问对象
    ContentResolver resolver = this.getContentResolver();

    Cursor cursor = resolver
        .query(AppNoticeAccess.CONTENT_URI_QUERY_ALL_APP, null, null,
            null, "_id desc");

    if (cursor != null && cursor.getCount() > 0) {

      int id;
      String name;
      int age;
      while (cursor.moveToNext()) {
        id = cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_ID));
        name = cursor.getString(cursor.getColumnIndex(AppNoticeAccess.KEY_PACKAGE_NAME));
        int  notice1 = cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_NOTICE));
        int  notice2= cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_NOTICE1));
        int  notice3= cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_NOTICE2));
        int  notice4= cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_NOTICE3));
        int  notice5 = cursor.getInt(cursor.getColumnIndex(AppNoticeAccess.KEY_NOTICE4));
        Log.i(TAG, "id: " + id + ", name: " + name + ", notice1: " + notice1+",notice2: "+notice2+",notice3: "+notice3+",notice4 "+notice4+",notice5 "+ notice5);
      }
      cursor.close();
    }
  }


  public void update(View view) {
    // 内容提供者访问对象
    ContentResolver resolver = this.getContentResolver();

    ContentValues values = new ContentValues();
    values.put(AppNoticeAccess.KEY_PACKAGE_NAME, "lisi");

    int count = resolver.update(AppNoticeAccess.CONTENT_URI_UPDATE_APP, values,
        AppNoticeAccess.KEY_ID + " = ?", new String[] { "1" });
    Log.i(TAG, "更新行: " + count);
  }

  public void querrySingle(View view) {
    // 在uri的末尾添加一个id
    Uri uri = ContentUris.withAppendedId(AppNoticeAccess.CONTENT_URI_QUERY_ITEM_APP, 1);

    // 内容提供者访问对象
    ContentResolver resolver = getContentResolver();

    Cursor cursor = resolver.query(uri, new String[] { AppNoticeAccess.KEY_ID,
        AppNoticeAccess.KEY_PACKAGE_NAME, AppNoticeAccess.KEY_NOTICE }, null, null, null);

    if (cursor != null && cursor.moveToFirst()) {
      int id = cursor.getInt(0);
      String name = cursor.getString(1);
      int age = cursor.getInt(2);
      cursor.close();
      Log.i(TAG, "id: " + id + ", name: " + name + ", age: " + age);
    }
  }
}
