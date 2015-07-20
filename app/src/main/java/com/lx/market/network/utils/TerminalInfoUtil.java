package com.lx.market.network.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.lx.market.network.model.TerminalInfo;
import com.lx.market.utils.Logger;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

public class TerminalInfoUtil {
  private static TerminalInfo m_TerminalInfoForZone;
  private static TerminalInfo mTerminalInfo;
  private static String isInitWithKeyStr;
  /**
   * Network type is unknown
   */
  public static final int NETWORK_TYPE_UNKNOWN = 0;
  /**
   * Current network is GPRS
   */
  public static final int NETWORK_TYPE_GPRS = 1;
  /**
   * Current network is EDGE
   */
  public static final int NETWORK_TYPE_EDGE = 2;
  /**
   * Current network is UMTS
   */
  public static final int NETWORK_TYPE_UMTS = 3;
  /**
   * Current network is CDMA: Either IS95A or IS95B
   */
  public static final int NETWORK_TYPE_CDMA = 4;
  /**
   * Current network is EVDO revision 0
   */
  public static final int NETWORK_TYPE_EVDO_0 = 5;
  /**
   * Current network is EVDO revision A
   */
  public static final int NETWORK_TYPE_EVDO_A = 6;
  /**
   * Current network is 1xRTT
   */
  public static final int NETWORK_TYPE_1xRTT = 7;
  /**
   * Current network is HSDPA
   */
  public static final int NETWORK_TYPE_HSDPA = 8;
  /**
   * Current network is HSUPA
   */
  public static final int NETWORK_TYPE_HSUPA = 9;
  /**
   * Current network is HSPA
   */
  public static final int NETWORK_TYPE_HSPA = 10;
  /**
   * Current network is iDen
   */
  public static final int NETWORK_TYPE_IDEN = 11;
  /**
   * Current network is EVDO revision B
   */
  public static final int NETWORK_TYPE_EVDO_B = 12;
  /**
   * Current network is LTE
   */
  public static final int NETWORK_TYPE_LTE = 13;
  /**
   * Current network is eHRPD
   */
  public static final int NETWORK_TYPE_EHRPD = 14;
  /**
   * Current network is HSPA+
   */
  public static final int NETWORK_TYPE_HSPAP = 15;

  private static void initTerminalInfo (Context c) {
	m_TerminalInfoForZone = new TerminalInfo();
	try {
	  m_TerminalInfoForZone.setHsman(android.os.Build.PRODUCT);
	  m_TerminalInfoForZone.setHstype(android.os.Build.MODEL);
	  m_TerminalInfoForZone.setOsVer(android.os.Build.VERSION.RELEASE);
	} catch (Exception e) {
	  m_TerminalInfoForZone.setHsman("");
	  m_TerminalInfoForZone.setHstype("");
	  m_TerminalInfoForZone.setOsVer("android");
	}
	try {
	  DisplayMetrics dm = c.getResources().getDisplayMetrics();
	  m_TerminalInfoForZone.setScreenWidth((short) dm.widthPixels);
	  m_TerminalInfoForZone.setScreenHeight((short) dm.heightPixels);
	} catch (Exception e) {
	  m_TerminalInfoForZone.setScreenWidth((short) 0);
	  m_TerminalInfoForZone.setScreenHeight((short) 0);
	}
	try {
	  m_TerminalInfoForZone.setRamSize((short) getTotalMemory());
	} catch (Exception e) {
	  m_TerminalInfoForZone.setRamSize((short) 0);
	}
	try {
	  TelephonyManager telMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
	  m_TerminalInfoForZone.setImei(telMgr.getDeviceId());
	} catch (Exception e) {
	  m_TerminalInfoForZone.setImei("");
	}
	try {
	  TelephonyManager telMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
	  List<NeighboringCellInfo> infos = telMgr.getNeighboringCellInfo();
	  if (infos.size() > 0) {
		m_TerminalInfoForZone.setLac((short) infos.get(0).getLac());
	  }
	} catch (Exception e) {
	  m_TerminalInfoForZone.setLac((short) 0);
	}

	String IMSI = getPhoneImsi(c);
	if (null == IMSI) {
	  IMSI = "";
	}
	m_TerminalInfoForZone.setImsi(IMSI);
	m_TerminalInfoForZone.setNetworkType(getNetworkType(c));
	m_TerminalInfoForZone.setIp(getLocalIpAddress());
	int verCode = 0;
	try {
	  verCode = getAppVersionCode(c);
	} catch (Exception e) {
	  e.printStackTrace();
	}
	String verName = getAppVersionName(c);
	try {
	  m_TerminalInfoForZone.setChannelId("oz_test");
	} catch (Exception e) {
	  m_TerminalInfoForZone.setChannelId("notfound");
	}
	m_TerminalInfoForZone.setAppId("oz_001");
	try {
	  mTerminalInfo = (TerminalInfo) m_TerminalInfoForZone.clone();
	} catch (Exception e) {
	  mTerminalInfo = m_TerminalInfoForZone;
	}
	try {
	  mTerminalInfo.setAppId(getAppId(c));
	} catch (Exception e) {
	  mTerminalInfo.setAppId("not found");
	}
  }

  public static TerminalInfo getTerminalInfo (Context c) {
	if (mTerminalInfo == null) {
	  initTerminalInfo(c);
	}
	try {
	  mTerminalInfo.setAppId(getAppId(c));
	} catch (Exception e) {
	  mTerminalInfo.setAppId("not found");
	}
	Logger.debug("mTerminalInfo=" + mTerminalInfo.toString());
	return mTerminalInfo;
  }

  public static TerminalInfo getTerminalInfoForZone (Context c) {
	if (m_TerminalInfoForZone == null) {
	  initTerminalInfo(c);
	}
	m_TerminalInfoForZone.setAppId("oz_001");
	return m_TerminalInfoForZone;
  }

  /**
   * 获取应用版本号
   *
   * @param context
   * @return
   * @throws Exception
   */
  public static int getAppVersionCode (Context context) {
	try {
	  PackageManager pm = context.getPackageManager();
	  PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
	  return pi.versionCode;
	} catch (Exception e) {
	}
	return 0;
  }

  /**
   * 获取应用版本名称
   *
   * @param context
   * @return
   * @throws Exception
   */
  public static String getAppVersionName (Context context) {
	try {
	  PackageManager pm = context.getPackageManager();
	  PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
	  return pi.versionName;
	} catch (Exception e) {
	}
	return "";
  }

  /**
   * 获取包名
   *
   * @param context
   * @return
   */
  public static String getPackageName (Context context) {
	try {
	  return context.getPackageName();
	} catch (Exception e) {
	  Logger.error("get package name error.");
	}
	return "";
  }

  /**
   * 获取本地配置的APPID
   *
   * @param context
   * @return
   * @throws Exception
   */
  public static String getAppId (Context context) {
	String appId = "oceam_market_10000";
	return appId;
  }

  /**
   * 获取总内存
   *
   * @return
   */
  private static int getTotalMemory () {
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
	  initial_memory = 0;
	}
	return initial_memory;
  }

  /**
   * 获取本地网络IP地址
   *
   * @return
   */
  public static String getLocalIpAddress () {
	try {
	  for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
		NetworkInterface intf = en.nextElement();
		for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
		  InetAddress inetAddress = enumIpAddr.nextElement();
		  // for getting IPV4 format
		  String ipv4 = null;
		  if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {
			return ipv4;
		  }
		}
	  }
	} catch (Exception ex) {
	}
	return null;
  }

  /**
   * 获取MAC地址
   *
   * @param context
   * @return
   */
  public static String getLocalMacAddress (Context context) {
	String mac = "";
	try {
	  WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	  WifiInfo info = wifi.getConnectionInfo();

	  if (null == info) {
		return "";
	  }
	  mac = info.getMacAddress();
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return mac;
  }

  private static Bundle getMetaData (Context context) throws Exception {
	ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
	Bundle bundle = ai.metaData;
	if (bundle == null) {
	  bundle = new Bundle();
	}
	return bundle;
  }

  /**
   * 获取手机IMSI号
   *
   * @param context
   * @return
   */
  public static String getPhoneImsi (Context context) {
	String imsi = null;
	TelephonyManager mTelephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
	try {
	  imsi = mTelephonyManager.getSubscriberId();
	} catch (Exception e) {
	}
	if (null == imsi) {
	  Class<? extends TelephonyManager> tmClass = mTelephonyManager.getClass();
	  try {
		Method getImsiMethod = tmClass.getMethod("getSubscriberIdGemini", Integer.TYPE);
		if (null != getImsiMethod) {
		  // 先取SIM2
		  imsi = (String) getImsiMethod.invoke(mTelephonyManager, 1);
		  if (null == imsi) {
			imsi = (String) getImsiMethod.invoke(mTelephonyManager, 0);
		  }
		}
	  } catch (Throwable e) {
	  }
	}
	return imsi;
  }

  /**
   * 获取连接类型
   *
   * @param context
   * @return
   */
  private static byte getNetworkType (Context context) {
	byte result = 0;
	ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	if (connectivity == null) {
	  result = NetworkConstants.NERWORK_TYPE_FAIL;
	} else {
	  NetworkInfo info = connectivity.getActiveNetworkInfo();
	  if (info == null) {
		result = NetworkConstants.NERWORK_TYPE_FAIL;
	  } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int type = tm.getNetworkType();
		switch (type) {
		  case NETWORK_TYPE_GPRS:
		  case NETWORK_TYPE_EDGE:
		  case NETWORK_TYPE_CDMA:
		  case NETWORK_TYPE_1xRTT:
		  case NETWORK_TYPE_IDEN:
			result = NetworkConstants.NERWORK_TYPE_2G;
			break;
		  case NETWORK_TYPE_UMTS:
		  case NETWORK_TYPE_EVDO_0:
		  case NETWORK_TYPE_EVDO_A:
		  case NETWORK_TYPE_HSDPA:
		  case NETWORK_TYPE_HSUPA:
		  case NETWORK_TYPE_HSPA:
		  case NETWORK_TYPE_EVDO_B:
		  case NETWORK_TYPE_EHRPD:
		  case NETWORK_TYPE_HSPAP:
			result = NetworkConstants.NERWORK_TYPE_3G;
			break;
		  case NETWORK_TYPE_LTE:
			result = NetworkConstants.NERWORK_TYPE_4G;
			break;
		  case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			result = NetworkConstants.NERWORK_TYPE_UNKNOWN;
			break;
		  default:
			result = (byte) type;
			break;
		}
	  } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
		result = NetworkConstants.NERWORK_TYPE_WIFI;
	  }
	}
	return result;
  }
}
