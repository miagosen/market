package com.lx.market.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lx.market.MarketApplication;

/**
 * Created by Antikvo.Miao on 2014/7/23.
 */
public class PreferenceUtil {
  private static PreferenceUtil    preferenceUtil;
  private SharedPreferences.Editor ed;
  private SharedPreferences        sp;
  private final static String      PREFERENCE_NAME = "config";
  private final static String      FIRST_START     = "first_start";

  private PreferenceUtil() {
  }

  public static PreferenceUtil getInstance() {
    if (preferenceUtil == null)
      preferenceUtil = new PreferenceUtil();
    preferenceUtil.init(MarketApplication.getInstance());
    return preferenceUtil;
  }

  public void init(Context context) {
    if ((this.sp == null) || (this.ed == null)) {
      this.sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
      this.ed = this.sp.edit();
    }
  }

  public boolean isFirstStart() {
	  //TODO false---> TRUE
    return this.sp.getBoolean(FIRST_START, false);
  }

  public void destroy() {
    this.sp = null;
    this.ed = null;
    preferenceUtil = null;
  }

  public boolean getBoolean(String key, boolean value) {
    return this.sp.getBoolean(key, value);
  }

  public int getInt(String key, int value) {
    return this.sp.getInt(key, value);
  }

  public long getLong(String key, long value) {
    return this.sp.getLong(key, value);
  }

  public String getString(Context context, String key, String value) {
    return this.sp.getString(key, value);
  }

  public String getString(String key, String value) {
    return this.sp.getString(key, value);
  }

  public void remove(String key) {
    this.ed.remove(key);
    this.ed.commit();
  }

  public void saveBoolean(String key, boolean value) {
    this.ed.putBoolean(key, value);
    this.ed.commit();
  }

  public void saveInt(String key, int value) {
    if (this.ed != null) {
      this.ed.putInt(key, value);
      this.ed.commit();
    }
  }

  public void saveLong(String key, long value) {
    this.ed.putLong(key, value);
    this.ed.commit();
  }

  public void saveString(String key, String value) {
    this.ed.putString(key, value);
    this.ed.commit();
  }

  public void setIsFirstStart(boolean isFirstStart) {
    this.ed.putBoolean(FIRST_START, isFirstStart);
    this.ed.commit();
  }

}
