package com.heli.providerapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;

import com.heli.providerapp.acticity.MyApplication;

import java.util.HashMap;
import java.util.Iterator;

public class DBUtils {
    private static  DBUtils dbUtils;
    private final static String TAG = "NoticeAccessHelper";
    private NoticeAccessSQLOpenHelper  sqlOpenHelper;
    private DBUtils() {
        sqlOpenHelper =NoticeAccessSQLOpenHelper.getInstance((MyApplication.getContext()));
    }

    public static DBUtils getInstance() {
        if (dbUtils==null) {
            dbUtils=new DBUtils();
        }
        return dbUtils;
    }

    /**
     *
     */
    public void addColumForSysTable(Context context, String field) {
        boolean columExist = isColumnExists(sqlOpenHelper.getWritableDatabase(), NoticeAccessSQLOpenHelper.TABLE_NAME_SYS_ACCESS, field);
        if (!columExist) {
        //todo
        }
    }

    /**
     * @param db
     * @param tableName  表名
     * @param columnName 列名
     * @return 布尔值结果
     */
    private boolean isColumnExists(SQLiteDatabase db, String tableName
            , String columnName) {
        boolean result = false;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{tableName, "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(TAG, "checkColumnExists2..." + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return result;
    }

    /**
     *
     * @param tableName
     * @param culNames
     * @param fieldDescription
     * @return
     */
    public boolean checkTableCul(String tableName, String [] culNames,String fieldDescription) {
        boolean result = false;
        Cursor cursor = null;
        try {
            HashMap<String, String> maps = new HashMap<String, String>();
            for (String culName : culNames) {
                maps.put("[" + culName + "]", fieldDescription);
            }

            cursor = getDataBase().rawQuery("select sql from sqlite_master where type='table' and " +
                    "name=?", new String[]{tableName});


            if (cursor != null && cursor.moveToFirst()) {
                String sql = cursor.getString(0);
                if (!TextUtils.isEmpty(sql)) {
                    Iterator<String> ite = maps.keySet().iterator();
                    while (ite.hasNext()) {
                        String st = ite.next() + " ";
                        if (sql.contains(st)) {
                            ite.remove();
                        }
                    }
                }
            }
            //if (!maps.keySet().contains("[" + culName + "]")) {
            //    result = true;
            //}
            Log.d("DbUtil",culNames.toString());
            if (!maps.isEmpty()) {
                result=true;
            }
            for (String st : maps.keySet()) {
                addCol(getDataBase(), tableName, st, maps.get(st));
                Log.d("DbUtil",st);
            }
        } catch (Exception e) {
            Log.e("oncreatedb", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return result;
    }

    private SQLiteDatabase getDataBase() {
       return sqlOpenHelper.getWritableDatabase();
    }

   private void addCol(SQLiteDatabase db, String tableName, String col, String type) {
        if (db != null) {
            db.execSQL(new StringBuffer().append("alter table ").append(tableName)
                    .append(" add ").append(col).append(" ").append(type).toString());
         Cursor cursor=   db.query(tableName,null,null,null,null,null,null);
            Log.d("DbUtil",cursor.getColumnCount()+"");
        }
    }




}
