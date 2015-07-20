package com.lx.market.utils;

import android.util.Log;

import com.lx.market.config.OcMarketConfig;
import com.lx.market.constants.FileConstants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
  public static final String OpenSns = "OcMarketLog";

  /**
   * 输出错误信息
   *
   * @param e
   * @param msg
   */
  public static void error (String TAG, String msg, Throwable e) {
	// 根据配置来判断是否打印日志
	if (!OcMarketConfig.getInstance().isOpenLog())
	  return;
	Log.e(TAG, msg, e);
  }

  /**
   * 调试信息
   *
   * @param msg
   */
  public static void error (String TAG, String msg) {
	if (!OcMarketConfig.getInstance().isOpenLog())
	  return;
	Log.e(TAG, msg);
  }

  /**
   * 调试信息
   *
   * @param msg
   */
  public static void error (String msg) {
	if (!OcMarketConfig.getInstance().isOpenLog())
	  return;
	Log.e(OpenSns, msg);
  }

  /**
   * 调试信息
   */
  public static void debug (String TAG, String msg) {
	if (!OcMarketConfig.getInstance().isOpenLog())
	  return;
	Log.d(TAG, msg);
  }

  /**
   * 调试信息
   */
  public static void debug (String msg) {
	if (!OcMarketConfig.getInstance().isOpenLog())
	  return;
	Log.d(OpenSns, msg);
  }

  public static void logD (String TAG, String str) {
	if (!OcMarketConfig.getInstance().isOpenLog())
	  return;
	Log.d(TAG, str);
  }

  public static void i (String TAG, String str) {
	if (!OcMarketConfig.getInstance().isOpenLog())
	  return;
	Log.i(TAG, str);
  }

  public static void d (String TAG, String str) {
	if (!OcMarketConfig.getInstance().isOpenLog())
	  return;
	Log.d(TAG, str);
  }

  public static void w (String TAG, String str) {
	if (!OcMarketConfig.getInstance().isOpenLog())
	  return;
	Log.w(TAG, str);
  }

  public static void e (String TAG, String str) {
	if (!OcMarketConfig.getInstance().isOpenLog())
	  return;
	Log.e(TAG, str);
  }

  /**
   * 打印log到指定文件
   *
   * @param tag 文件名
   * @param str 内容 + 时间（自动生成）
   */
  public static void printLogToFile (String tag, String str) {
	if (!OcMarketConfig.getInstance().isOpenLog())
	  return;
	File dir = new File(FileConstants.SDCARD + FileConstants.FILE_ROOT + "/log");// 路径
	if (!dir.exists()) {
	  dir.mkdirs();
	}
	File file = new File(dir, tag);
	if (!file.exists()) {
	  try {
		file.createNewFile();
	  } catch (IOException e) {
		e.printStackTrace();
	  }
	}
	long time = System.currentTimeMillis();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	try {
	  FileUtils.writeFile(file, str + " -- " + sdf.format(new Date(time)) + "\n", true);
	} catch (IOException e) {
	  e.printStackTrace();
	}
  }

  /**
   * 删除指定log文件
   *
   * @param tag 文件名
   */
  public static void deleteLogFile (String tag) {
	File f = new File("/sdcard/" + FileConstants.FILE_ROOT + "/log/" + tag);
	if (f.exists()) {
	  f.delete();
	}
  }

  /**
   * 打印异常
   *
   * @param e
   */
  public static void p (Throwable e) {
	if (OcMarketConfig.getInstance().isOpenLog()) {
	  e.printStackTrace();
	}
  }

}
