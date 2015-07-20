package com.lx.market.download;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.lx.market.MarketApplication;
import com.lx.market.model.DownloadInfo;
import com.lx.market.utils.DataStoreUtils;
import com.lx.market.utils.FileUtils;
import com.lx.market.utils.Logger;
import com.lx.market.utils.PackageUtils;

/**
 * APP管理：安装，下载，删除等
 */

public class AppManagerCenter {

  // ------------------ APP状态 Start ------------------
  /**
   * 应用不存在
   */
  public static final int        APP_STATE_UNEXIST        = 1000;
  /**
   * 应用正在被下载
   */
  public static final int        APP_STATE_DOWNLOADING    = APP_STATE_UNEXIST + 1;
  /**
   * 应用下载被暂停
   */
  public static final int        APP_STATE_DOWNLOAD_PAUSE = APP_STATE_DOWNLOADING + 1;
  /**
   * 应用已完成下载，但尚未安装
   */
  public static final int        APP_STATE_DOWNLOADED     = APP_STATE_DOWNLOAD_PAUSE + 1;
  /**
   * 应用已被安装
   */
  public static final int        APP_STATE_INSTALLED      = APP_STATE_DOWNLOADED + 1;
  /**
   * 应用需要更新
   */
  public static final int        APP_STATE_UPDATE         = APP_STATE_INSTALLED + 1;
  /**
   * 应用等待下载
   */
  public static final int        APP_STATE_WAIT           = APP_STATE_UPDATE + 1;
  /**
   * 应用正在被安装（仅静默安装时使用） *
   */
  public static final int        APP_STATE_INSTALLING     = APP_STATE_WAIT + 1;
  // ------------------ APP状态 End ------------------

  private static final Context   context                  = MarketApplication.curContext;
  protected static final String  TAG                      = "AppManagerCenter";
  private static HashSet<String> staticInstallPkg         = new HashSet<String>();

  /**
   * 是否存在该游戏
   * 
   * @param appPackage
   * @return
   */
  public static boolean isAppExist(String appPackage) {
    try {
      MarketApplication.curContext.getPackageManager().getApplicationInfo(appPackage, 0);
      return true;
    } catch (NameNotFoundException e) {
    }
    return false;
  }

  /**
   * @param versionCode
   *          : 版本号，用来判断是否要更新
   * @param packageName
   *          : 用来查询是否存在APK或下载中的临时文件
   */
  public static int getAppDownlaodState(String packageName, int versionCode) {

    if (null == packageName) {
      return APP_STATE_UNEXIST;
    }
    int appState = APP_STATE_UNEXIST; // 默认不存在
    /*
     * 四種狀態：不存在、存在不需更新、存在需更新、Apk文件已下載、是否正在下载
     */
    do {
      if (staticInstallPkg.contains(packageName)) {
        appState = APP_STATE_INSTALLING;
        break;
      }

      // 先判断是否存在 和 是否更新
      if (isAppExist(packageName)) {
        appState = APP_STATE_INSTALLED;
        Log.e("----", "---app-installed---");
        if (appIsNeedUpate(packageName, versionCode)) {
          // 还需要更新
          appState = APP_STATE_UPDATE;
          Log.e("----", "---app-update---");
          break;
        } else {
          Log.e("----", "---app-installed---");
          break;
        }
      } else if (isApkFileExist(packageName, versionCode)) {
        appState = APP_STATE_DOWNLOADED;
        break;
      }
      /*
       * 为什么要判断是否有Task呢？判断是否有正在下载的包 *
       */
      // 需要判断是否有TASK
      DownloadTask task = DownloadTaskMgr.getInstance().getDownloadTask(packageName);
      if (null == task || task.isBackgroundTask()) {
        // 没有下载，do nothing, 或者是后台任务，不处理
      } else {
        switch (task.gameDownloadState) {
        case DownloadState.STATE_DOWNLOAD_WAIT:
          appState = APP_STATE_WAIT;
          break;
        case DownloadState.STATE_DOWNLOAD_START_LOADING:
        case DownloadState.STATE_DOWNLOAD_UPDATE_PROGRESS:
          appState = APP_STATE_DOWNLOADING;
          break;
        case DownloadState.STATE_DOWNLOAD_PAUSE:
        case DownloadState.STATE_DOWNLOAD_ERROR: // 错误下载，需要重新下载，设置成暂停状态
          appState = APP_STATE_DOWNLOAD_PAUSE;
          break;
        case DownloadState.STATE_DOWNLOAD_SUCESS: {
          if (APP_STATE_UPDATE == appState) {
            // app已存在，但需要更新
            if (versionCode > task.downloadInfo.getVersionCode()) {
              // 线上版本的版本号比下载的版本号还新，重新下载
              appState = APP_STATE_UPDATE;
            } else {
              // 下载的版本和线上的版本一致
              appState = APP_STATE_DOWNLOADED; // 下载成功，提示安装
            }
          } else if (APP_STATE_INSTALLED == appState) {
            // 已安装
            // if(MyGamesDataSource.getInstance().isPirateGame(packageName)){
            // 山寨游戏
            appState = APP_STATE_DOWNLOADED; // 下载成功，提示安装
            // }
          } else {
            appState = APP_STATE_DOWNLOADED; // 默认下载成功，提示安装
          }
        }
          break;
        default:
          // do nothing
          break;
        }
      }
    } while (false);
    return appState;
  }

  private static boolean isApkFileExist(String packageName, int versionCode) {
    String apkFilePath = FileUtils.getAPKFilePath(packageName, versionCode);
    File apkFile = new File(apkFilePath);
    if (apkFile.exists()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * * 安装下载下来的APK包
   * <p/>
   * : 用来统计数据
   * 
   * @param gameApp
   */
  public static void installApk(DownloadInfo gameApp) {
    if (null == gameApp) {
      return;
    }

    String packageName = gameApp.getPackageName();
    int versionCode = gameApp.getVersionCode();
    if (staticInstallPkg.contains(packageName)) {
      // 已经在安装
      return;
    }
    if (AppManagerCenter.isAppExist(packageName)) {
      // 如果是山寨或替换的游戏，先执行删除操作
      // AppManagerCenter.uninstallGameApp(packageName);

      return;
    }

    String gameAPKFilePath = FileUtils.getAPKFilePath(packageName, versionCode);
    File gameAPKFile = new File(gameAPKFilePath);
    if (gameAPKFile.exists()) {
      // 如果要设置静默安装开关
      String valInstall = DataStoreUtils.readLocalInfo(DataStoreUtils.SWITCH_INSTALL_SILENT);
      if (DataStoreUtils.CHECK_ON.equals(valInstall)) {
        installRoot(gameAPKFilePath, packageName);
      } else {
        PackageUtils.installNormal(context, gameAPKFilePath);
      }
    } else {
      // 文件被删除了,重新下载
      startDownload(gameApp);
    }
  }

  private static void refreshUI() {
    DownloadTaskMgr.getInstance().notifyRefreshUI(DownloadState.STATE_DOWNLOAD_REFRESH);
  }

  /**
   * 静默安装
   * 
   * @param path
   */
  public static void installRoot(final String path, final String pkg) {
    Runnable task = new Runnable() {
      @Override
      public void run() {
        staticInstallPkg.add(pkg);
        refreshUI();
        PackageUtils.install(context, path);
        staticInstallPkg.remove(pkg);
        refreshUI();
      }
    };
    new Thread(task).start();
  }

  /**
   * 卸载游戏
   * 
   * @param packageName
   */
  public static void uninstallGameApp(final String packageName) {
    PackageUtils.uninstallNormal(MarketApplication.curContext, packageName);
    // 不希望用户卸载
    // if (isAppExist(packageName)) {
    // Runnable task = new Runnable() {
    // @Override
    // public void run() {
    // PackageUtils.uninstall(MarketApplication.curContext, packageName);
    // refreshUI();
    // }
    // };
    // new Thread(task).start();
    // }
  }

  /**
   * 判断是否要更新版本，根据versionCode来判断
   * 
   * @param packageName
   * @param versionCode
   * @return
   */

  public static boolean appIsNeedUpate(String packageName, int versionCode) {
    try {
      PackageInfo packageInfo = MarketApplication.curContext.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);

      if (packageInfo != null) {
        if (packageInfo.versionCode < versionCode) {
          return true;
        }
      }
    } catch (NameNotFoundException e) {
      return false;
    }
    return false;
  }

  public static void startDownload(DownloadInfo game) {
    startDownload(game, false);
  }

  public static void startDownloadBackground(DownloadInfo game) {
    startDownload(game, true);
  }

  // ///////////////////////////////////以下代码为下载API////////////////////////////////////////////////////////

  /**
   * 开始下载
   * 
   * @param game
   * @param isBackground
   *          : 是否是后台任务
   */
  private static void startDownload(DownloadInfo game, final boolean isBackground) {
    Log.e(TAG, "-----onClick--wait--pkn-" + game.getPackageName() + "---url---" + game.getDownloadUrl());
    if (null == game || null == game.getDownloadUrl() || null == game.getPackageName()) {
      return;
    }

    DownloadTaskMgr.getInstance().startDownload(game, isBackground);

    if (isBackground) {
      // 后台任务不加入我玩
    } else {
      // MyGamesDataSource.getInstance().addGame(game);
    }
  }

  /**
   * 暂停下载
   * 
   * @param isUserPressed
   *          :用户主动停止
   */

  public static void pauseDownload(DownloadInfo game, boolean isUserPressed) {
    DownloadTaskMgr.getInstance().pauseDownload(game, isUserPressed);
  }

  /**
   * 取消下载，会删除已下载的文件，从数据库中删除下载信息
   */
  public static void cancelDownload(DownloadInfo game) {
    DownloadTaskMgr.getInstance().cancelDownload(game);
    // MyGamesDataSource.getInstance().deleteGame(game);
  }

  /**
   * 删除下载的APK包或下载的临时文件
   */
  public static void deleteDownloadGameApk(DownloadInfo game) {
    DownloadTaskMgr.getInstance().cancelDownload(game);
    // MyGamesDataSource.getInstance().deleteGame(game);
  }

  /**
   * 继续下载所有下载中任务
   */
  public static void continueAllDownload() {
    Logger.i(TAG, "APP continueAllDownload");
    DownloadTaskMgr.getInstance().continueAllDownload();
  }

  /**
   * 暂停所有下载中任务
   */
  public static void pauseAllDownload() {
    DownloadTaskMgr.getInstance().pauseAllDownload();
  }

  /**
   * 根据pkgName查询游戏的下载进度
   * 
   * @param pkgName
   * @return
   */
  public static int getDownloadProgress(String pkgName) {
    return DownloadTaskMgr.getInstance().getDownloadProgress(pkgName);
  }

  /**
   * UI对download状态的监听. 注意：当UI界面销毁，或者的被置于后台的时候，移除监听。避免重复多次的刷新数据和UI 记得删除,否则会引起内存泄露
   * 
   * @param refreshHanle
   */
  public static void setDownloadRefreshHandle(UIDownLoadListener refreshHanle) {
    DownloadTaskMgr.getInstance().setUIDownloadListener(refreshHanle);
  }

  /**
   * 删除下载监听句柄
   * 
   * @param refreshHanle
   */
  public static void removeDownloadRefreshHandle(UIDownLoadListener refreshHanle) {
    DownloadTaskMgr.getInstance().removeUIDownloadListener(refreshHanle);
  }

  public static boolean hasDownloadingApp() {
    return DownloadTaskMgr.getInstance().hasDownloadingTask();
  }

  // ///////////////////////////////////////下载的接口
  // end/////////////////////////////////////////////////////////////////////
  public static final String OLD_PKG_NAME = "com.socogame.ppc";

  /**
   * 判定是否存在旧版的版本
   * 
   * @return
   */
  public static boolean isOldVersionExist() {
    List<PackageInfo> pkgs = context.getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
    int pkgSize = pkgs.size();
    for (int i = 0; i < pkgSize; i++) {
      PackageInfo pkgInfo = pkgs.get(i);
      if (OLD_PKG_NAME.equalsIgnoreCase(pkgInfo.packageName)) {
        if (isSystemApp(pkgInfo) || isSystemUpdateApp(pkgInfo)) {
          return false;
        }
      }
    }
    return false;
  }

  /**
   * 是否是系统应用
   * 
   * @param pInfo
   * @return
   */
  public static boolean isSystemApp(PackageInfo pInfo) {
    return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
  }

  public static boolean isSystemApp(String packageName) {
    try {
      PackageInfo pInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
      return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 是否是系统应用更新
   * 
   * @param pInfo
   * @return
   */
  public static boolean isSystemUpdateApp(PackageInfo pInfo) {
    return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
  }

  private static final String SCHEME                   = "package";
  /**
   * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
   */
  private static final String APP_PKG_NAME_21          = "com.android.settings.ApplicationPkgName";
  /**
   * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
   */
  private static final String APP_PKG_NAME_22          = "pkg";
  /**
   * InstalledAppDetails所在包名
   */

  private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
  /**
   * InstalledAppDetails类名
   */

  private static final String APP_DETAILS_CLASS_NAME   = "com.android.settings.InstalledAppDetails";

  /**
   * 调用系统InstalledAppDetails界面显示已安装应用程序的详细信息。 对于Android 2.3（Api Level
   * 9）以上，使用SDK提供的接口； 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）。
   * 
   * @param context
   * @param packageName
   *          应用程序的包名
   */
  public static void showInstalledAppDetails(Context context, String packageName) {
    if (!AppManagerCenter.isAppExist(packageName)) {
      // 不存在的应用，退出
      return;
    }
    Intent intent = new Intent();
    final int apiLevel = Build.VERSION.SDK_INT;
    if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
      Uri uri = Uri.fromParts(SCHEME, packageName, null);
      intent.setData(uri);
    } else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
      // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
      final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22 : APP_PKG_NAME_21);
      intent.setAction(Intent.ACTION_VIEW);
      intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME);
      intent.putExtra(appPkgName, packageName);
    }
    context.startActivity(intent);
  }
}
