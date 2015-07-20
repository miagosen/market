package com.lx.market.constants;

import android.os.Environment;

import java.io.File;

public class FileConstants {
  // sd卡上的存放目录，以.开头为隐藏目录
  public static final String FILE_ROOT = ".com.oz.market";
  //SD卡根目录
  public static final String SDCARD = Environment.getExternalStorageDirectory().getPath();
  //图片缓存地址
  public static final String IMAGE_PATH = FILE_ROOT + File.separator + "image" + File.separator;
  //APK 存储目录
  public static final String APK_DIR_PATH = SDCARD + File.separator + FILE_ROOT + File.separator + "apks" + File.separator;
  //自更新包存储目录
  public static final String UPDATE_APK_DIR_PATH = SDCARD + File.separator + FILE_ROOT + File.separator + "update" + File.separator;
  //数据库名称
  public static final String DB_NAME = "oc_market.db";
  public static final int DISK_CACHE_SIZE = 1024 * 1024 * 20;//20m
}
