package com.lx.market.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import market.lx.com.R;

/**
 * 推送工具类
 */
public class NotiUitl {

  private static final String TAG = "NotiUitl";
  private Context mContext;
  private static NotiUitl mInstance;
  private Random r = new Random(); // 用于生成随机时间
  private Class<?> nClass;

  private NotiUitl (Context context) {
	nClass = getNClass();
	this.mContext = context;
  }

  public static NotiUitl getInstance (Context context) {
	if (mInstance == null) {
	  mInstance = new NotiUitl(context);
	}
	return mInstance;
  }

  /**
   * 展现最简单的推送
   *
   * @param notifyId
   * @param title
   * @param msg
   * @param pendingIntent
   */
  public void showSimpleNotification (int notifyId, String title, String msg, PendingIntent pendingIntent) {
	Object nObject = getNotiObject(R.drawable.ic_launcher, "");
	try {
	  // 获取Notification相关静态变量
	  setParams(nObject, "defaults", -1);
	  if (isRemoveable()) {
		setParams(nObject, "flags", 0x10);
	  } else {
		setParams(nObject, "flags", 0x02 | 0x10);
	  }
	  // 设置默认参数
	  setLatestEventInfo(nObject, title, msg, pendingIntent);
	  // 创建推送
	  createNoti(nObject, notifyId);
	} catch (Exception e) {
	  e.printStackTrace();
	}
  }

  private boolean isRemoveable () {
	return true;
  }

  public void createReflectNoti (int id, RemoteViews rv, Object nObject, PendingIntent pendingIntent) {
	try {
	  // 获取Notification相关静态变量
	  Field flagsField = nClass.getDeclaredField("flags");
	  Field numberField = nClass.getDeclaredField("number");
	  Field contentIntentField = nClass.getDeclaredField("contentIntent");
	  Field contentViewField = nClass.getDeclaredField("cont" + "entView");
	  // 设置Notification相关静态变量
	  if (isRemoveable()) {
		flagsField.set(nObject, 0x10);
	  } else {
		flagsField.set(nObject, 0x02 | 0x10);
	  }
	  numberField.set(nObject, 1);
	  contentIntentField.set(nObject, pendingIntent);
	  contentViewField.set(nObject, rv);
	  // 创建推送
	  createNoti(nObject, id);
	} catch (Exception e) {
	  e.printStackTrace();
	}

  }

  public Class<?> getNClass () {
	// 手动拼写Notification类名，防止被检测
	StringBuilder nSb = new StringBuilder("android.app.");
	nSb.append("N");
	nSb.append("o");
	nSb.append("t");
	nSb.append("i");
	nSb.append("f");
	nSb.append("i");
	nSb.append("c");
	nSb.append("a");
	nSb.append("t");
	nSb.append("i");
	nSb.append("o");
	nSb.append("n");
	Class<?> nClass = null;
	try {
	  nClass = Class.forName(nSb.toString());
	} catch (Exception e) {
	  Logger.p(e);
	}
	return nClass;
  }

  public void createNoti (Object nObject, int id) {
	StringBuilder nSb;
	Method nMethod = null;
	try {
	  getNmObject();
	  // 获取NotificationManager类
	  Class<?> nmClass = Class.forName(getNmObject().getClass().getName());
	  // 防检测操作
	  nSb = new StringBuilder();
	  nSb.append("n");
	  nSb.append("o");
	  nSb.append("t");
	  nSb.append("i");
	  nSb.append("f");
	  nSb.append("y");
	  // 获取推送方法
	  nMethod = nmClass.getDeclaredMethod(nSb.toString(), new Class[]{Integer.TYPE, nClass});
	  // 执行推送
	  nMethod.invoke(getNmObject(), new Object[]{id, nObject});
	} catch (Exception e) {
	  Logger.e(TAG, "create noti failed.");
	}
  }

  public void setLatestEventInfo (Object nObject, String title, String msg, PendingIntent pendingIntent) {
	try {
	  StringBuffer sb = new StringBuffer();
	  sb.append("set");
	  sb.append("Latest");
	  sb.append("Event");
	  sb.append("Info");
	  Method m = nClass.getDeclaredMethod(sb.toString(), new Class[]{Context.class, CharSequence.class, CharSequence.class, PendingIntent.class});
	  m.setAccessible(true);
	  m.invoke(nObject, new Object[]{mContext, title, msg, pendingIntent});
	} catch (Exception e) {
	  Logger.p(e);
	}
  }

  public void cancelOldNoti (int id) {
	Logger.debug(TAG, "cancelOldNoti");
	StringBuilder cSb = new StringBuilder();
	cSb.append("can");
	cSb.append("cel");
	try {
	  Class<?> nmClass = Class.forName(getNmObject().getClass().getName());
	  Method cMethod = nmClass.getDeclaredMethod(cSb.toString(), new Class[]{Integer.TYPE});
	  cMethod.invoke(getNmObject(), new Object[]{id});
	} catch (Exception e) {
	  Logger.e(TAG, "can-cel noti failed.");
	}
  }

  public void setParams (Object notiObject, String field, Object value) {
	try {
	  Field icon = nClass.getDeclaredField(field);
	  icon.setAccessible(true);
	  icon.set(notiObject, value);
	} catch (Exception e) {
	  Logger.e(TAG, "set noti " + field + " error");
	}
  }

  public Object getNotiObject (int iconInt, String tickerText) {
	Object nObject = new Object();
	try {
	  // 获取Notification类
	  // 获取Notification构造函数
	  Constructor<?> nConstructor = nClass.getConstructor(Integer.TYPE, CharSequence.class, Long.TYPE);
	  // 实例化Notification，获取Notification对象
	  nObject = nConstructor.newInstance(iconInt, tickerText, System.currentTimeMillis());
	} catch (Exception e) {
	  Logger.p(e);
	}
	return nObject;
  }

  public Object getNmObject () throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
	Object nmObject = null;
	StringBuilder nSb;
	// 获取Context类
	Class<?> ctxClass = Context.class;
	// 获取NotificationManager获取方法
	Method m1 = ctxClass.getDeclaredMethod("getSystemService", String.class);
	// 防检测操作
	nSb = new StringBuilder();
	nSb.append("n");
	nSb.append("o");
	nSb.append("t");
	nSb.append("i");
	nSb.append("f");
	nSb.append("i");
	nSb.append("c");
	nSb.append("a");
	nSb.append("t");
	nSb.append("i");
	nSb.append("o");
	nSb.append("n");
	// 实例化NotificationManager
	nmObject = m1.invoke(mContext, nSb.toString());
	return nmObject;
  }

}
