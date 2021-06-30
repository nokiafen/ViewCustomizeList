package com.heli.providerapp;

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
public class AppNoticeAccess {
     public static final String AUTHORITY = "com.heli.noticeaccessprovider";

     public static final String PATH_INSERT_APP = "access/insert";
     public static final String PATH_DELETE_APP = "access/delete";
     public static final String PATH_UPDATE_APP = "access/update";
     public static final String PATH_QUERY_ALL_APP = "access/queryAll";
     public static final String PATH_QUERY_ITEM_APP = "access/query/#";

    public static final String PATH_INSERT_SYSTEM = "access/insert_s";
    public static final String PATH_DELETE_SYSTEM = "access/delete_s";
    public static final String PATH_UPDATE_SYSTEM = "access/update_s";
    public static final String PATH_QUERY_ALL_SYSTEM = "access/queryAll_s";
    public static final String PATH_QUERY_ITEM_SYSTEM = "access/query/#_s";
     //
     public static final Uri CONTENT_URI_INSERT_APP = Uri.parse("content://" + AUTHORITY + "/" + PATH_INSERT_APP);
     public static final Uri CONTENT_URI_DELETE_APP = Uri.parse("content://" + AUTHORITY + "/" + PATH_DELETE_APP);
     public static final Uri CONTENT_URI_UPDATE_APP = Uri.parse("content://" + AUTHORITY + "/" + PATH_UPDATE_APP);
     public static final Uri CONTENT_URI_QUERY_ALL_APP = Uri.parse("content://" + AUTHORITY + "/" + PATH_QUERY_ALL_APP);
     public static final Uri CONTENT_URI_QUERY_ITEM_APP = Uri.parse("content://" + AUTHORITY + "/" + PATH_QUERY_ITEM_APP);

    public static final Uri CONTENT_URI_INSERT_SYS = Uri.parse("content://" + AUTHORITY + "/" + PATH_INSERT_SYSTEM);
    public static final Uri CONTENT_URI_DELETE_SYS = Uri.parse("content://" + AUTHORITY + "/" + PATH_DELETE_SYSTEM);
    public static final Uri CONTENT_URI_UPDATE_SYS = Uri.parse("content://" + AUTHORITY + "/" + PATH_UPDATE_SYSTEM);
    public static final Uri CONTENT_URI_QUERY_ALL_SYS = Uri.parse("content://" + AUTHORITY + "/" + PATH_QUERY_ALL_SYSTEM);
    public static final Uri CONTENT_URI_QUERY_ITEM_SYS = Uri.parse("content://" + AUTHORITY + "/" + PATH_QUERY_ITEM_SYSTEM);


    //common filed
     public static final String KEY_ID = "_id";

     //app access  field
     public static final String KEY_PACKAGE_NAME = "appname";
     public static final String KEY_NOTICE= "channel0";
     public static final String KEY_NOTICE1= "channel1";
     public static final String KEY_NOTICE2= "channel2";
     public static final String KEY_NOTICE3= "channel3";
     public static final String KEY_NOTICE4= "channel4";
     public static final String KEY_APP_ICON= "channel_icon";
     public static final String KEY_APP_NAME= "channel_name";


     //system app  field
     public static final String KEY_ACCESS= "channel_sys";
     public static final String KEY_ACCESS_TITLE= "channel_sys_title";




}
