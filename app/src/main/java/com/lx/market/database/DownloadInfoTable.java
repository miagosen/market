package com.lx.market.database;

import android.provider.BaseColumns;

/**
 * Created by Antikvo.Miao on 2014/10/17.
 */
public class DownloadInfoTable {
  /* download info 表名 */
  public static final String TABLE_NAME_DOWNLOAD_INFO = "table_download_info";
  public static final String ID = BaseColumns._ID;
  public static final String DOWNLOAD_INFO_1_REF_ID = "ref_id";
  public static final String DOWNLOAD_INFO_2_PACKAGE_NAME = "pkg_name";
  public static final String DOWNLOAD_INFO_3_FILE_NAME = "file_name";
  public static final String DOWNLOAD_INFO_4_VERSION_CODE = "version_code";
  public static final String DOWNLOAD_INFO_5_VERSION_NAME = "version_name";
  public static final String DOWNLOAD_INFO_6_FILE_SIZE = "apk_size";
  public static final String DOWNLOAD_INFO_7_LOADED_SIZE = "loaded_size"; // 已经下载的大小
  public static final String DOWNLOAD_INFO_8_APK_DOWNLOAD_URL = "apk_url";
  public static final String DOWNLOAD_INFO_9_DOWNLOAD_STATE = "download_state";
  public static final String DOWNLOAD_INFO_10_TASK_FLAG = "task_flag"; // 任务标示
  public static final String DOWNLOAD_INFO_11_POSITION = "position"; // 来源
  public static final String DOWNLOAD_INFO_12_MD5 = "md5";
  public static final String DOWNLOAD_INFO_13_ICON_URL = "icon_url";

  public static final String SQL_DELETE_DOWNLOAD_INFO_TABLE = DatabaseHelper.SQL_DELETE_TABLE
	+ TABLE_NAME_DOWNLOAD_INFO;
  public static final String SQL_CREATE_DOWNLOAD_INFO_TABLE = DatabaseHelper.SQL_CREATE_TABLE
	+ TABLE_NAME_DOWNLOAD_INFO + "(" + ID + " INTEGER primary key AUTOINCREMENT,"
	+ DOWNLOAD_INFO_1_REF_ID + " INTEGER,"
	+ DOWNLOAD_INFO_2_PACKAGE_NAME + " TEXT,"
	+ DOWNLOAD_INFO_3_FILE_NAME + " TEXT,"
	+ DOWNLOAD_INFO_4_VERSION_CODE + " INTEGER,"
	+ DOWNLOAD_INFO_5_VERSION_NAME + " TEXT,"
	+ DOWNLOAD_INFO_6_FILE_SIZE + " INTEGER,"
	+ DOWNLOAD_INFO_7_LOADED_SIZE + " INTEGER,"
	+ DOWNLOAD_INFO_8_APK_DOWNLOAD_URL + " TEXT,"
	+ DOWNLOAD_INFO_9_DOWNLOAD_STATE + " INTEGER,"
	+ DOWNLOAD_INFO_10_TASK_FLAG + " INTEGER,"
	+ DOWNLOAD_INFO_11_POSITION + " INTEGER,"
	+ DOWNLOAD_INFO_12_MD5 + " TEXT,"
	+ DOWNLOAD_INFO_13_ICON_URL + " TEXT"
	+ ")";
}
