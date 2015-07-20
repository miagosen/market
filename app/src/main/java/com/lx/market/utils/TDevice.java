package com.lx.market.utils;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

import market.lx.com.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.lx.market.MarketApplication;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TDevice {

  public static boolean  GTE_HC;
  public static boolean  GTE_ICS;
  public static boolean  PRE_HC;
  private static Boolean _hasBigScreen  = null;
  private static Boolean _hasCamera     = null;
  private static Boolean _isTablet      = null;
  private static Integer _loadFactor    = null;
  private static int     _pageSize      = -1;
  public static float    displayDensity = 0.0F;

  static {
    GTE_ICS = Build.VERSION.SDK_INT >= 14;
    GTE_HC = Build.VERSION.SDK_INT >= 11;
    PRE_HC = Build.VERSION.SDK_INT >= 11 ? false : true;
  }

  public TDevice() {
  }
  public static int getActionBarHeight(Context context) {
    int actionBarHeight = 0;
    TypedValue tv = new TypedValue();
    if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
      actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());

    if (actionBarHeight == 0 && context.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
      actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
    }

    return actionBarHeight;
  }

  public static float dpToPixel(float dp) {
    return dp * (getDisplayMetrics().densityDpi / 160F);
  }

  public static int getDefaultLoadFactor() {
    if (_loadFactor == null) {
      Integer integer = Integer.valueOf(0xf & MarketApplication.curContext.getResources().getConfiguration().screenLayout);
      _loadFactor = integer;
      _loadFactor = Integer.valueOf(Math.max(integer.intValue(), 1));
    }
    return _loadFactor.intValue();
  }

  public static float getDensity() {
    if (displayDensity == 0.0)
      displayDensity = getDisplayMetrics().density;
    return displayDensity;
  }

  public static DisplayMetrics getDisplayMetrics() {
    DisplayMetrics displaymetrics = new DisplayMetrics();
    ((WindowManager) MarketApplication.curContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
    return displaymetrics;
  }

  public static float getScreenHeight() {
    return getDisplayMetrics().heightPixels;
  }

  public static float getScreenWidth() {
    return getDisplayMetrics().widthPixels;
  }

  public static int[] getRealScreenSize(Activity activity) {
    int[] size = new int[2];
    int screenWidth = 0, screenHeight = 0;
    WindowManager w = activity.getWindowManager();
    Display d = w.getDefaultDisplay();
    DisplayMetrics metrics = new DisplayMetrics();
    d.getMetrics(metrics);
    // since SDK_INT = 1;
    screenWidth = metrics.widthPixels;
    screenHeight = metrics.heightPixels;
    // includes window decorations (statusbar bar/menu bar)
    if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
      try {
        screenWidth = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
        screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
      } catch (Exception ignored) {
      }
    // includes window decorations (statusbar bar/menu bar)
    if (Build.VERSION.SDK_INT >= 17)
      try {
        Point realSize = new Point();
        Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
        screenWidth = realSize.x;
        screenHeight = realSize.y;
      } catch (Exception ignored) {
      }
    size[0] = screenWidth;
    size[1] = screenHeight;
    return size;
  }

  public static int getStatusBarHeight() {
    Class<?> c = null;
    Object obj = null;
    Field field = null;
    int x = 0;
    try {
      c = Class.forName("com.android.internal.R$dimen");
      obj = c.newInstance();
      field = c.getField("status_bar_height");
      x = Integer.parseInt(field.get(obj).toString());
      return MarketApplication.curContext.getResources().getDimensionPixelSize(x);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static int getPageSize() {
    if (_pageSize == -1)
      if (TDevice.isTablet())
        _pageSize = 50;
      else if (TDevice.hasBigScreen())
        _pageSize = 20;
      else
        _pageSize = 10;
    return _pageSize;
  }

  public static String getUdid() {
    String udid = MarketApplication.getInstance().getPreferences().getString("udid", "");
    if (udid.length() == 0) {
      SharedPreferences.Editor editor = MarketApplication.getInstance().getPreferences().edit();
      udid = String.format("%s", UUID.randomUUID());
      editor.putString("udid", udid);
      editor.commit();
    }
    return udid;
  }

  public static boolean hasBigScreen() {
    boolean flag = true;
    if (_hasBigScreen == null) {
      boolean flag1;
      if ((0xf & MarketApplication.curContext.getResources().getConfiguration().screenLayout) >= 3)
        flag1 = flag;
      else
        flag1 = false;
      Boolean boolean1 = Boolean.valueOf(flag1);
      _hasBigScreen = boolean1;
      if (!boolean1.booleanValue()) {
        if (getDensity() <= 1.5F)
          flag = false;
        _hasBigScreen = Boolean.valueOf(flag);
      }
    }
    return _hasBigScreen.booleanValue();
  }

  public static final boolean hasCamera() {
    if (_hasCamera == null) {
      PackageManager pckMgr = MarketApplication.curContext.getPackageManager();
      boolean flag = pckMgr.hasSystemFeature("android.hardware.camera.front");
      boolean flag1 = pckMgr.hasSystemFeature("android.hardware.camera");
      boolean flag2;
      if (flag || flag1)
        flag2 = true;
      else
        flag2 = false;
      _hasCamera = Boolean.valueOf(flag2);
    }
    return _hasCamera.booleanValue();
  }

  public static boolean hasHardwareMenuKey(Context context) {
    boolean flag = false;
    if (PRE_HC)
      flag = true;
    else if (GTE_ICS) {
      flag = ViewConfiguration.get(context).hasPermanentMenuKey();
    } else
      flag = false;
    return flag;
  }

  public static boolean hasInternet() {
    boolean flag;
    if (((ConnectivityManager) MarketApplication.curContext.getSystemService("connectivity")).getActiveNetworkInfo() != null)
      flag = true;
    else
      flag = false;
    return flag;
  }

  public static boolean gotoGoogleMarket(Activity activity, String pck) {
    try {
      Intent intent = new Intent();
      intent.setPackage("com.android.vending");
      intent.setAction(Intent.ACTION_VIEW);
      intent.setData(Uri.parse("market://details?id=" + pck));
      activity.startActivity(intent);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean isPackageExist(String pckName) {
    try {
      PackageInfo pckInfo = MarketApplication.curContext.getPackageManager().getPackageInfo(pckName, 0);
      if (pckInfo != null)
        return true;
    } catch (NameNotFoundException e) {
      Logger.p(e);
    }
    return false;
  }

  public static void hideAnimatedView(View view) {
    if (PRE_HC && view != null)
      view.setPadding(view.getWidth(), 0, 0, 0);
  }

  public static void hideSoftKeyboard(View view) {
    if (view == null)
      return;
    ((InputMethodManager) MarketApplication.curContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static boolean isLandscape() {
    boolean flag;
    if (MarketApplication.curContext.getResources().getConfiguration().orientation == 2)
      flag = true;
    else
      flag = false;
    return flag;
  }

  public static boolean isPortrait() {
    boolean flag = true;
    if (MarketApplication.curContext.getResources().getConfiguration().orientation != 1)
      flag = false;
    return flag;
  }

  public static boolean isTablet() {
    if (_isTablet == null) {
      boolean flag;
      if ((0xf & MarketApplication.curContext.getResources().getConfiguration().screenLayout) >= 3)
        flag = true;
      else
        flag = false;
      _isTablet = Boolean.valueOf(flag);
    }
    return _isTablet.booleanValue();
  }

  public static float pixelsToDp(float f) {
    return f / (getDisplayMetrics().densityDpi / 160F);
  }

  public static void showAnimatedView(View view) {
    if (PRE_HC && view != null)
      view.setPadding(0, 0, 0, 0);
  }

  public static void showSoftKeyboard(Dialog dialog) {
    dialog.getWindow().setSoftInputMode(4);
  }

  public static void showSoftKeyboard(View view) {
    ((InputMethodManager) MarketApplication.curContext.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, InputMethodManager.SHOW_FORCED);
  }

  public static void toogleSoftKeyboard(View view) {
    ((InputMethodManager) MarketApplication.curContext.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
  }

  public static boolean isSdcardReady() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }

  public static String getCurCountryLan() {
    return MarketApplication.curContext.getResources().getConfiguration().locale.getLanguage() + "-"
        + MarketApplication.curContext.getResources().getConfiguration().locale.getCountry();
  }

  public static boolean isZhCN() {
    String lang = MarketApplication.curContext.getResources().getConfiguration().locale.getCountry();
    if (lang.equalsIgnoreCase("CN")) {
      return true;
    }
    return false;
  }

  public static String percent(double p1, double p2) {
    String str;
    double p3 = p1 / p2;
    NumberFormat nf = NumberFormat.getPercentInstance();
    nf.setMinimumFractionDigits(2);
    str = nf.format(p3);
    return str;
  }

  public static String percent2(double p1, double p2) {
    String str;
    double p3 = p1 / p2;
    NumberFormat nf = NumberFormat.getPercentInstance();
    nf.setMinimumFractionDigits(0);
    str = nf.format(p3);
    return str;
  }

  public static void gotoMarket(Context context, String pck) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.setData(Uri.parse("market://details?id=" + pck));
    context.startActivity(intent);
  }

  public static void openAppInMarket(Context context) {
    if (context != null) {
      String pckName = context.getPackageName();
      try {
        gotoMarket(context, pckName);
      } catch (Exception ex) {
        try {
          String otherMarketUri = "http://market.android.com/details?id=" + pckName;
          Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(otherMarketUri));
          context.startActivity(intent);
        } catch (Exception e) {

        }
      }
    }
  }

  public static int getStatuBarHeight() {
    Class<?> c = null;
    Object obj = null;
    Field field = null;
    int x = 0, sbar = 38;// 默认为38，貌似大部分是这样的
    try {
      c = Class.forName("com.android.internal.R$dimen");
      obj = c.newInstance();
      field = c.getField("status_bar_height");
      x = Integer.parseInt(field.get(obj).toString());
      sbar = MarketApplication.curContext.getResources().getDimensionPixelSize(x);

    } catch (Exception e1) {
      e1.printStackTrace();
    }
    return sbar;
  }

  /**
   * 用来判断服务是否运行.
   * 
   * @param context
   * @param className
   *          判断的服务名字
   * @return true 在运行 false 不在运行
   */
  public static boolean isServiceRunning(Context mContext, String className) {
    boolean isRunning = false;
    ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(300);
    if (!(serviceList.size() > 0)) {
      return false;
    }
    for (int i = 0; i < serviceList.size(); i++) {
      if (serviceList.get(i).service.getClassName().equals(className)) {
        return true;
      }
    }
    return isRunning;
  }
}
