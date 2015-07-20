package com.lx.market.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class PhoneInfoUtils {
  /**
   * 获取总内存
   *
   * @return
   */
  public static int getTotalMemory () {
	String str1 = "/proc/meminfo";
	String str2;
	String[] arrayOfString;
	int initial_memory = 0;

	try {
	  FileReader localFileReader = new FileReader(str1);
	  BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
	  str2 = localBufferedReader.readLine();
	  arrayOfString = str2.split("\\s+");
	  initial_memory = Integer.valueOf(arrayOfString[1]).intValue() / 1024;
	  localBufferedReader.close();
	} catch (Exception e) {
	}
	return initial_memory;
  }

  /**
   * 获取android当前可用内存大小
   */
  public static int getAvailMemory (Context context) {
	ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
	am.getMemoryInfo(mi);

	return (int) (mi.availMem / 1024);
  }

  /**
   * : isSDExists
   */
  public static boolean isSDExists () {
	return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  }

  /**
   * 获取SD卡容量
   */
  public static long getSDcardRoom () {
	String state = Environment.getExternalStorageState();
	if (Environment.MEDIA_MOUNTED.equals(state)) {
	  File sdcardDir = Environment.getExternalStorageDirectory();
	  StatFs sf = new StatFs(sdcardDir.getPath());
	  long blockSize = sf.getBlockSize();
	  long blockCount = sf.getBlockCount();
	  return blockSize * blockCount;
	}
	return 0;
  }

  /**
   * 获取可用SD卡容量
   */
  public static long getAvailableSDcardRoom () {
	String state = Environment.getExternalStorageState();
	if (Environment.MEDIA_MOUNTED.equals(state)) {
	  File sdcardDir = Environment.getExternalStorageDirectory();
	  StatFs sf = new StatFs(sdcardDir.getPath());
	  long blockSize = sf.getBlockSize();
	  long availCount = sf.getAvailableBlocks();
	  return blockSize * availCount;
	}
	return 0;
  }

  /**
   * 获取ROM大小
   */
  public static long getMobileRomRoom () {
	File sdcardDir = Environment.getDataDirectory();
	StatFs sf = new StatFs(sdcardDir.getPath());
	long blockSize = sf.getBlockSize();
	long blockCount = sf.getBlockCount();
	return blockSize * blockCount;
  }

  /**
   * 获取剩余ROM大小
   */
  public static long getAvailableMobileRoom () {
	File sdcardDir = Environment.getDataDirectory();
	StatFs sf = new StatFs(sdcardDir.getPath());
	long blockSize = sf.getBlockSize();
	long availCount = sf.getAvailableBlocks();
	return blockSize * availCount;
  }

  /**
   * 获取剩余内存（RAM）大小
   */
  public long getAvailableMemory (Context context) {
	ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
	am.getMemoryInfo(mi);
	return mi.availMem;
  }

  /**
   * 获取手机密度
   */
  public static float getDensity (Context context) {
	DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	return metrics.density;
  }

  /**
   * Role:获取当前设置的电话号码
   */
  public static String getNativePhoneNumber (Context context) {
	String nativePhoneNumber = null;
	try {
	  TelephonyManager mTelephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
	  nativePhoneNumber = mTelephonyManager.getLine1Number();
	} catch (Exception e) {
	}
	return nativePhoneNumber;
  }

  public static short getPhoneScreenHeight (Context context) {
	DisplayMetrics dm = context.getResources().getDisplayMetrics();
	return (short) dm.heightPixels;
  }

  public static short getPhoneScreenWidth (Context context) {
	DisplayMetrics dm = context.getResources().getDisplayMetrics();
	return (short) dm.widthPixels;
  }

}
