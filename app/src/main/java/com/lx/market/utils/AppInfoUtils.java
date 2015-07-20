package com.lx.market.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.lidroid.xutils.exception.DbException;
import com.lx.market.MarketApplication;
import com.lx.market.model.InstalledAppInfo;
import com.lx.market.model.MyPackageInfo;
import com.lx.market.model.UninstallAppInfo;
import com.lx.market.network.model.PackageInfoBto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class AppInfoUtils {
  public static List<PackageInfo> packageInfoList = null;

  public static List<MyPackageInfo> installedPackageInfoList = null;

  public static List<MyPackageInfo> removedPackageInfoList = null;

  private static List<PackageInfoBto> installedPageInfos = new ArrayList<PackageInfoBto>();

  private static List<PackageInfoBto> uninstalledPageInfos = new ArrayList<PackageInfoBto>();

  private static final String SCHEME = "package";

  public static List<PackageInfoBto> getInstalledPageInfos (Context context) {
	initInstalled(context);
	return installedPageInfos;
  }

  private static void initInstalled (Context context) {
	try {
	  if (installedPageInfos.size() == 0) {
		List<InstalledAppInfo> installedAppInfos = MarketApplication.getInstance().dbUtils.findAll(InstalledAppInfo.class);
		if (installedAppInfos != null) {
		  for (InstalledAppInfo info : installedAppInfos) {
			PackageInfoBto pib = new PackageInfoBto();
			pib.setPackageName(info.getPackageName());
			installedPageInfos.add(pib);
		  }
		}
	  }
	} catch (Exception e) {
	  Logger.p(e);
	}
  }

  public static List<PackageInfoBto> getUninstalledPageInfos () {
	initUnstalledInfos();
	return uninstalledPageInfos;
  }

  private static void initUnstalledInfos () {
	try {
	  if (uninstalledPageInfos.size() == 0) {
		List<UninstallAppInfo> uninstallAppInfos = MarketApplication.getInstance().dbUtils.findAll(UninstallAppInfo.class);
		if (uninstallAppInfos != null) {
		  for (UninstallAppInfo info : uninstallAppInfos) {
			PackageInfoBto pib = new PackageInfoBto();
			pib.setPackageName(info.getPackageName());
			uninstalledPageInfos.add(pib);
		  }
		}
	  }
	} catch (DbException e) {
	  Logger.p(e);
	}
  }

  /**
   * 获取应用版本号
   *
   * @param context
   * @return
   * @throws android.content.pm.PackageManager.NameNotFoundException 没有找到包名
   */
  public static int getPackageVersionCode (Context context) {
	int version = 0;
	try {
	  PackageManager pm = context.getPackageManager();
	  PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
	  version = pi.versionCode;
	} catch (Exception e) {
	  Logger.p(e);
	}
	return version;
  }

  /**
   * 获取应用版本名
   *
   * @param context
   * @return
   * @throws android.content.pm.PackageManager.NameNotFoundException 没有找到包名
   */
  public static String getPackageVersionName (Context context) throws PackageManager.NameNotFoundException {
	PackageManager pm = context.getPackageManager();
	PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
	return pi.versionName;
  }

  /**
   * 判断SD卡是否存在
   *
   * @return
   */
  public static boolean isSDCardAvailable () {
	return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
  }

  /**
   * 判断APK是否被安装
   *
   * @param context
   * @param packageName
   * @return
   */
  public static boolean isApkExist (Context context, String packageName) {
	if (packageName == null || "".equals(packageName))
	  return false;
	return getPackageInfoByPackageName(context, packageName) != null;
  }

  /**
   * 根据包名获取包信息
   *
   * @param context
   * @param packageName
   * @return
   */
  public static PackageInfo getPackageInfoByPackageName (Context context, String packageName) {
	if (packageInfoList == null) {
	  packageInfoList = context.getPackageManager().getInstalledPackages(0);
	}
	for (int i = 0; i < packageInfoList.size(); i++) {
	  PackageInfo p = packageInfoList.get(i);
	  if (p.packageName.equals(packageName)) {
		return p;
	  }
	}
	return null;
  }

  public static String getMd5FromFile (Object object) throws Exception {
	if (object instanceof File) {
	  File file = (File) object;
	  return getMd5FromFile(file.getAbsolutePath());
	}
	return "";
  }

  /**
   * 获取文件的MD5值
   *
   * @param filepath
   * @return
   * @throws java.security.NoSuchAlgorithmException
   * @throws java.io.FileNotFoundException
   */
  public static String getMd5FromFile (String filepath) throws NoSuchAlgorithmException, FileNotFoundException {
	MessageDigest digest = MessageDigest.getInstance("MD5");
	File f = new File(filepath);
	String output = "";
	InputStream is = new FileInputStream(f);
	byte[] buffer = new byte[8192];
	int read = 0;
	try {
	  while ((read = is.read(buffer)) > 0) {
		digest.update(buffer, 0, read);
	  }
	  byte[] md5sum = digest.digest();
	  BigInteger bigInt = new BigInteger(1, md5sum);
	  output = bigInt.toString(16);
	  for (; output.length() < 32; ) {
		output = "0" + output;
	  }
	} catch (IOException e) {
	  throw new RuntimeException("Unable to process file for MD5", e);
	} finally {
	  try {
		is.close();
	  } catch (IOException e) {
		throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
	  }
	}
	return output;
  }

  /**
   * 根据apk文件获取应用信息
   *
   * @param packageManager
   * @param apkFile
   * @return
   */
  public static PackageInfo getPackageInfoFromAPKFile (PackageManager packageManager, File apkFile) {
	return packageManager.getPackageArchiveInfo(apkFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA);
  }

  /**
   * 启动其他应用
   *
   * @param context
   * @param packageName
   */
  public static void launchOtherActivity (Context context, String packageName, Bundle b) {
	Intent i = context.getPackageManager().getLaunchIntentForPackage(packageName);
	if (i == null) {
	  i = new Intent(packageName);
	  i.setAction(Intent.ACTION_MAIN);
	  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}
	if (b != null) {
	  i.putExtras(b);
	}
	if (i != null) {
	  try {
		context.startActivity(i);
	  } catch (Exception e) {
		Logger.p(e);
	  }
	}
  }

  public static void removeApk (MyPackageInfo pInfo) {
	if (pInfo != null && pInfo.getApkPath() != null) {
	  File apkFile = new File(pInfo.getApkPath());
	  if (apkFile.exists()) {
		apkFile.delete();
	  }
	}
  }

  public static void clearCache (Context context) {
	installedPageInfos.clear();
	uninstalledPageInfos.clear();
	initInstalled(context);
	initUnstalledInfos();
  }

}
