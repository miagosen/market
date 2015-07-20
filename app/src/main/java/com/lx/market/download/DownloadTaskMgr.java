package com.lx.market.download;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import market.lx.com.R;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lx.market.MarketApplication;
import com.lx.market.database.dao.DownloadDao;
import com.lx.market.model.ClientInfo;
import com.lx.market.model.DownloadInfo;
import com.lx.market.threads.SQLSingleThreadExcutor;
import com.lx.market.utils.DataStoreUtils;
import com.lx.market.utils.FileUtils;
import com.lx.market.utils.Logger;
import com.lx.market.utils.ToastUtils;

public class DownloadTaskMgr {
  private static final String           TAG           = "DownloadTaskMgr";
  /**
   * 下载的app
   */
  private HashMap<String, DownloadTask> downloadTaskHashMap;
  /**
   * 下载模块的状态监听，通下载线程交互
   */
  private DownloadState                 loadListener;
  /**
   * 下载模块的handler，和UI交互*
   */
  private Handler                       loadHandler;
  /**
   * 通知UI下载的状态
   */
  private HashSet<UIDownLoadListener>   uiListners;
  private HashSet<UIDownLoadListener>   loopListners;                     // 专门用户循环
  private DownloadDao                   downloadDao;
  private long                          lastRefreshUI = 0;
  private static DownloadTaskMgr        instance;

  public HashMap<String, DownloadTask> getDownloadTaskHashMap() {
    return downloadTaskHashMap;
  }

  private DownloadTaskMgr() {
    // 不允许外部实例化
    initDownloadAppMode();
  }

  /**
   * 只通知UI,不做其他逻辑处理
   * 
   * @param state
   * @param errorCode
   * @param pkgName
   */
  private void notifyUIDownloadState(int state, int errorCode, String pkgName) {
    if (null == uiListners || 0 == uiListners.size()) {
      return;
    }
    if (null == loopListners) {
      loopListners = new HashSet<UIDownLoadListener>();
    }
    loopListners.clear();
    loopListners.addAll(uiListners);
    for (UIDownLoadListener listener : loopListners) {
      listener.handleDownloadState(state, errorCode, pkgName);
    }
    // 最后通知的时间
    lastRefreshUI = System.currentTimeMillis();
    loopListners.clear();
  }

  /**
   * 提供 appManagerCenter，静默安装的时候，状态变化刷新
   */
  public void notifyRefreshUI(int state) {
    notifyDLTaskUIMsgToHandler(state, null, 0, false);
  }

  private void notifyDLTaskUIMsgToHandler(int state, String pkgname, int errorCode, boolean isBackground) {
    if (isBackground) {

    } else {
      notifyDLTaskUIMsgToHandler(state, pkgname, errorCode, 0);
    }
  }

  /**
   * 通知task的状态给Handler，发送给UI的监听.只通知UI,不做其他逻辑处理
   * 
   * @param state
   * @param pkgname
   * @param errorCode
   */
  private synchronized void notifyDLTaskUIMsgToHandler(int state, String pkgname, int errorCode, long delayMillis) {
    Message msg = Message.obtain();
    msg.what = state;
    msg.arg1 = errorCode;
    msg.obj = pkgname;

    loadHandler.sendMessageDelayed(msg, delayMillis);
  }

  public static DownloadTaskMgr getInstance() {
    if (null == instance) {
      instance = new DownloadTaskMgr();
    }
    return instance;
  }

  /**
   * 初始化洗澡模块,UI线程调用
   */
  private void initDownloadAppMode() {
    Logger.debug("initDownloadAppMode");
    downloadDao = DownloadDao.getInstance();
    // 下载模块的handler，和UI交互
    loadHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        loadHandler.removeMessages(DownloadState.STATE_DOWNLOAD_UPDATE_PROGRESS); // 避免过度刷屏
        notifyUIDownloadState(msg.what, msg.arg1, (String) msg.obj);
      }

      ;
    };

    // 下载模块的状态监听，通下载线程交互
    loadListener = new DownloadState() {
      @Override
      public void onDownloadState(int state, String pkgname, int errorCode) {
        DownloadTask task = downloadTaskHashMap.get(pkgname);

        if (null == task) {
          return;
        }
        // 先更新task的状态，后续执行，会和状态相关
        task.gameDownloadState = state;

        if (DownloadState.STATE_DOWNLOAD_SUCESS == state) {
          task.resetDownloadRunnable();// 移除下载任务线程，否则pause全部的时候，状态会被置成暂停
          if (checkDownloadSucess(pkgname)) {
            downloadDao.updateState(pkgname, state);
            // 继续后台任务的下载
            if (task.isBackgroundTask()) {
            } else {
              continueBackgroundDownload();
            }
          } else {
            // 下载成功后，发现文件出错，重新下载
            task.gameDownloadState = DownloadState.STATE_DOWNLOAD_ERROR;
            startDownload(task.downloadInfo, task.isBackgroundTask());
            return;
          }
        } else if ((DownloadState.STATE_DOWNLOAD_PAUSE == state) || (DownloadState.STATE_DOWNLOAD_ERROR == state)) {
          // 下载任务线程被结束或暂停了
          task.resetDownloadRunnable();
          downloadDao.updateState(pkgname, state);
          if ((DownloadState.ERROR_CODE_TIME_OUT == errorCode) || (DownloadState.ERROR_CODE_HTTP == errorCode)
              || (DownloadState.ERROR_CODE_URL_ERROR == errorCode)) {
            // 如果是超时，或者网络连接失败，重试
            task.gameDownloadState = state;
            startDownload(task.downloadInfo, task.isBackgroundTask());

            return;
          }

          if (task.isBackgroundTask()) {
            // 后台任务被暂停，不继续后台任务下载.原因： 可能是因为正常任务把后台任务中止的。故不再启动
          } else {
            // 正常任务暂停后，继续后台任务的下载
            continueBackgroundDownload();
          }
        }
        notifyDLTaskUIMsgToHandler(state, pkgname, errorCode, task.isBackgroundTask());
      }

      @Override
      public void updateDownloadProgress(String pkgname, int downloadFileSize, int downloadPosition) {
        // task的更新进度
        DownloadTask task = downloadTaskHashMap.get(pkgname);
        if (null == task) {
          return;
        }
        // 判断刷新的频率，防止过度刷屏
        long current = System.currentTimeMillis();
        if ((current - lastRefreshUI) < 600) {
          // 如果600ms内已经刷新
        } else {
          notifyDLTaskUIMsgToHandler(DownloadState.STATE_DOWNLOAD_UPDATE_PROGRESS, pkgname, DownloadState.ERROR_NONE, task.isBackgroundTask());
        }

        task.setDownloadSize(downloadFileSize, downloadPosition);
        // update database
        downloadDao.updateDownloadSize(pkgname, downloadFileSize, downloadPosition);
      }
    };
    // 下载游戏的数据池
    downloadTaskHashMap = new HashMap<String, DownloadTask>();

    // 从数据库中初始化下载数据
    initDownloadGameTaskFromDB();
  }

  /**
   * 初始化下载的数据
   */
  private void initDownloadGameTaskFromDB() {
    SQLSingleThreadExcutor.getInstance().execute(new Runnable() {

      @Override
      public void run() {
        // 从数据库中恢复下载数据
        HashMap<String, DownloadTask> taskMap = downloadDao.getAllDownloadExeTask();
        if (null == taskMap) {
          Log.e(TAG, "taskMap is null");
          return;
        }
        for (DownloadTask downloadTask : taskMap.values()) {
          Log.e(TAG, "----getDownloadTaskFromDB--" + downloadTask.downloadInfo.getPackageName());
        }
        downloadTaskHashMap.putAll(taskMap);
        Logger.debug("initDownloadGameTaskFromDB END");
        notifyDLTaskUIMsgToHandler(DownloadState.STATE_DOWNLOAD_MODE_INIT, null, 0, 5000); // 5s后执行，需要等UI准备好
      }
    });
  }

  /**
   * 查看网络状态，是否有SD卡
   * 
   * @return 错误值 : DownloadState.ERROR_CODE_NO_NET,
   *         DownloadState.ERROR_CODE_NOT_WIFI
   *         ,DownloadState.ERROR_CODE_NO_SDCARD
   */
  private int checkNetAndSpace() {
    int netType = ClientInfo.getAPNType(MarketApplication.curContext);
    int errorCode = DownloadState.ERROR_NONE;
    Log.e(TAG, "----netTypeAndErrorCode---" + netType + "-----" + errorCode);
    if (netType == ClientInfo.NONET) {
      // 通知网络错误
      errorCode = DownloadState.ERROR_CODE_NO_NET;
    } else if ((netType != ClientInfo.WIFI) && MarketApplication.isDownloadWIFIOnly()) {
      // 通知网络设置
      errorCode = DownloadState.ERROR_CODE_NOT_WIFI;
    } else if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      // 通知无SD卡
      errorCode = DownloadState.ERROR_CODE_NO_SDCARD;
    }
    return errorCode;
  }

  /**
   * 下载接口
   * 
   * @param downloadInfo
   * @return false: 继续下载， true： 新下载
   */
  public boolean startDownload(DownloadInfo downloadInfo, boolean isBackground) {
    boolean isNewDownload = false;
    if (null == downloadInfo || null == downloadInfo.getPackageName() || 0 == downloadInfo.getRefId() || null == downloadInfo.getDownloadUrl()) {
      return isNewDownload;
    }
    String pkgnameString = downloadInfo.getPackageName();

    // check net and space
    int checkResult = checkNetAndSpace();
    Log.e(TAG, "---checkNetAndSpace----" + checkResult);

    if (DownloadState.ERROR_NONE == checkResult) {
    } else if (DownloadState.ERROR_CODE_NOT_WIFI == checkResult) {
      ToastUtils.showToast(R.string.toast_tip_mobile_data);
    } else {
      // 通知错误
      notifyDLTaskUIMsgToHandler(DownloadState.STATE_DOWNLOAD_ERROR, pkgnameString, checkResult, isBackground);
    }
    synchronized (downloadTaskHashMap) {
      DownloadTask downloadTask = downloadTaskHashMap.get(pkgnameString);
      Log.e(TAG, "---syn---");
      if (null == downloadTask) {
        // 新的下载
        DownloadTask.deleteTmpDownloadFile(downloadInfo.getPackageName(), downloadInfo.getVersionCode());
        downloadTask = new DownloadTask(downloadInfo);
        downloadTaskHashMap.put(pkgnameString, downloadTask);
        isNewDownload = true;
        downloadTask.setBackgroundTaskFlag(isBackground);// 新增任务时，初始化
        // insert to database
        downloadDao.insertDownloadInfo(downloadTask);
      } else {
        if (downloadTask.isBackgroundTask() && !isBackground) {
          // 后台任务才会切换状态
          downloadTask.setBackgroundTaskFlag(false);
          downloadDao.updateTaskFlag(downloadTask);
        }
        // continue download.
        if ((downloadInfo.getVersionCode() > downloadTask.downloadInfo.getVersionCode())
            || (DownloadState.STATE_DOWNLOAD_ERROR == downloadTask.gameDownloadState)) {
          // 线上版本比下载中的版本更新了，或者下载出错了，重新下载
          downloadTask.resetTask(downloadInfo); // 替换下载信息
          downloadDao.updateTaskFlag(downloadTask);
        } else if (DownloadState.STATE_DOWNLOAD_SUCESS == downloadTask.gameDownloadState) {
          Logger.debug("startDownload DownloadState.STATE_DOWNLOAD_SUCESS is " + downloadTask.downloadInfo.getPackageName());
          // 下载的版本已经最新，并已下载成功.
          // 判断文件是否存在，如果不存在，重新下载. -----用户删除了文件，或者更换了SD卡
          File apkFile = new File(FileUtils.getAPKFilePath(downloadTask.downloadInfo.getPackageName(), downloadTask.downloadInfo.getVersionCode()));
          if (apkFile.exists()) {
            // install
            if (isBackground) {
            } else {
              SharedPreferences sp = MarketApplication.curContext.getSharedPreferences("setting", 0);
              boolean isAutoInstallApk = sp.getBoolean("root", true);
              if (isAutoInstallApk) {
                AppManagerCenter.installApk(downloadTask.downloadInfo);
              }
              notifyDLTaskUIMsgToHandler(DownloadState.STATE_DOWNLOAD_SUCESS, downloadTask.downloadInfo.getPackageName(), 0, 1000);
            }
            return isNewDownload;
          } else {
            downloadTask.gameDownloadState = DownloadState.STATE_DOWNLOAD_ERROR;
          }
        }
      }

      if (!downloadTask.isBackgroundTask()) {
        // cancel background task first，优先下载前台任务
        pauseAllBackgroundDownload();
      }
      // start task
      startDownloadTask(downloadTask);

      return isNewDownload;
    }
  }

  /**
   * 停止所有下载
   */
  public void pauseAllBackgroundDownload() {
    if (null == downloadTaskHashMap || 0 == downloadTaskHashMap.size()) {
      return;
    }
    synchronized (downloadTaskHashMap) {
      Iterator<Entry<String, DownloadTask>> ite = downloadTaskHashMap.entrySet().iterator();
      Entry<String, DownloadTask> entity = null;
      DownloadTask task = null;
      while (ite.hasNext()) {
        entity = ite.next();
        task = entity.getValue();
        if (task.isBackgroundTask()) {
          pauseDownloadTask(task, false);
        }
      }
    }
  }

  public void continueBackgroundDownload() {
    if (ClientInfo.networkType != ClientInfo.WIFI) {
      // 非WIFI网络，直接退出
      return;
    }

    String autoLoad = DataStoreUtils.readLocalInfo(DataStoreUtils.AUTO_LOAD_UPDATE_PKG);
    if (DataStoreUtils.CHECK_OFF.equals(autoLoad)) {
      return;
    }

    if (null == downloadTaskHashMap || 0 == downloadTaskHashMap.size()) {
      return;
    }

    int checkResult = checkNetAndSpace();
    if (DownloadState.ERROR_NONE == checkResult) {

    } else {
      // 通知错误
      return;
    }

    Logger.i(TAG, "continueBackgroundDownload");
    synchronized (downloadTaskHashMap) {
      Iterator<Entry<String, DownloadTask>> ite = downloadTaskHashMap.entrySet().iterator();
      Entry<String, DownloadTask> entity = null;
      DownloadTask task = null;
      while (ite.hasNext()) {
        entity = ite.next();
        task = entity.getValue();
        if ((DownloadState.STATE_DOWNLOAD_SUCESS != task.gameDownloadState) && task.isBackgroundTask()) {
          startDownloadTask(task);
        }
      }
    }
  }

  /**
   * cancel downloadtask and delete from database
   * 
   * @param game
   */
  public void cancelDownload(DownloadInfo game) {
    if (null == game || null == game.getPackageName()) {
      return;
    }

    DownloadTask loadGameTask = removeTask(game.getPackageName());
    if (null != loadGameTask) {
      // stop task
      loadGameTask.cancelTask();
      notifyDLTaskUIMsgToHandler(DownloadState.STATE_DOWNLOAD_CANCEL, game.getPackageName(), 0, loadGameTask.isBackgroundTask());
    }
  }

  public void clearDownloadHistory(DownloadInfo game) {
    if (null == game || null == game.getPackageName()) {
      return;
    }

    DownloadTask loadGameTask = removeTask(game.getPackageName());
    if (null != loadGameTask) {

      notifyDLTaskUIMsgToHandler(DownloadState.STATE_DOWNLOAD_CANCEL, game.getPackageName(), 0, loadGameTask.isBackgroundTask());
    }
  }

  public void clearAllDownloadHistory(List<DownloadInfo> arrayList) {
    if (null == arrayList) {
      return;
    }
    for (DownloadInfo downloadInfo : arrayList) {
      clearDownloadHistory(downloadInfo);
    }
  }
  /**
   * pause Download ，停止下载，必须把执行线程置成null
   * 
   * @param game
   */
  public void pauseDownload(DownloadInfo game, boolean isUser) {
    if (null == game || null == game.getPackageName()) {
      return;
    }
    DownloadTask loadGameTask = downloadTaskHashMap.get(game.getPackageName());
    // pause task
    pauseDownloadTask(loadGameTask, isUser);
  }

  /**
   * @param loadGameTask
   * @param isUser
   */
  private void pauseDownloadTask(DownloadTask loadGameTask, boolean isUser) {
    if (null == loadGameTask) {
      return;
    }
    loadGameTask.pauseTask(isUser);
    downloadDao.updateTaskFlag(loadGameTask);
  }

  /**
   * 继续所有下载
   */
  public void continueAllDownload() {

    if (null == downloadTaskHashMap || 0 == downloadTaskHashMap.size()) {
      Log.d(TAG, "null downloadTaskHashMap");
      return;
    }
    Log.d(TAG, "not null");
    int checkResult = checkNetAndSpace();
    if (DownloadState.ERROR_NONE == checkResult) {
      Log.d(TAG, "not null error none");
    } else if (DownloadState.ERROR_CODE_NOT_WIFI == checkResult) {
      ToastUtils.showToast(R.string.toast_tip_mobile_data);
    } else {
      // 通知错误
      Log.d(TAG, "not null 通知错误");
      return;
    }

    Logger.i(TAG, "continueAllDownload------");
    synchronized (downloadTaskHashMap) {
      Iterator<Entry<String, DownloadTask>> ite = downloadTaskHashMap.entrySet().iterator();
      Entry<String, DownloadTask> entity = null;
      DownloadTask task = null;
      while (ite.hasNext()) {
        entity = ite.next();
        task = entity.getValue();
        Logger.i(TAG, "continueAllDownload-----" + task.gameDownloadState);
        if (DownloadState.STATE_DOWNLOAD_SUCESS != task.gameDownloadState) {
          // 只要还没下载完成，或者安装的，都恢复下载
          if (task.isUserPause()) {
            Logger.i(TAG, "continueAllDownload-----is user pause");
            startDownloadTask(task);
          } else if (task.isBackgroundTask()) {
            // 后台任务不启动继续下载，由外部控制
            Log.d(TAG, "not null后台任务");
          } else {
            startDownloadTask(task);
          }
        }
      }
    }
  }

  /**
   * 启动下载
   * 
   * @param task
   */
  private void startDownloadTask(DownloadTask task) {
    if (null == task) {
      return;
    }
    Log.e(TAG, "----startDownloadTask---");
    task.startTask(loadListener);
    downloadDao.updateTaskFlag(task);
  }

  /**
   * 停止所有下载
   */
  public void pauseAllDownload() {
    if (null == downloadTaskHashMap || 0 == downloadTaskHashMap.size()) {
      return;
    }
    synchronized (downloadTaskHashMap) {
      Iterator<Entry<String, DownloadTask>> ite = downloadTaskHashMap.entrySet().iterator();
      Entry<String, DownloadTask> entity = null;
      DownloadTask task = null;
      while (ite.hasNext()) {
        entity = ite.next();
        task = entity.getValue();
        pauseDownloadTask(task, task.isUserPause());
      }
    }
  }

  /**
   * 判断是否有正在下载的任务
   */
  public boolean hasDownloadingTask() {
    if (null == downloadTaskHashMap || 0 == downloadTaskHashMap.size()) {
      return false;
    }
    synchronized (downloadTaskHashMap) {
      Iterator<Entry<String, DownloadTask>> ite = downloadTaskHashMap.entrySet().iterator();
      Entry<String, DownloadTask> entity = null;
      DownloadTask task = null;
      while (ite.hasNext()) {
        entity = ite.next();
        task = entity.getValue();

        if (task.isBackgroundTask()) {
          // 后台任务，不计算
        } else {
          if (task.taskIsLoading()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public int getDownloadProgress(String pkgName) {
    DownloadTask task = downloadTaskHashMap.get(pkgName);
    if (null == task) {
      return 0;
    }
    return task.progress;
  }

  public void setUIDownloadListener(UIDownLoadListener refreshHanle) {
    if (null == uiListners) {
      uiListners = new HashSet<UIDownLoadListener>();
    }
    uiListners.add(refreshHanle);
  }

  public void removeUIDownloadListener(UIDownLoadListener refreshHanle) {
    if (null == uiListners) {
      return;
    }
    uiListners.remove(refreshHanle);
  }

  public DownloadInfo getDownloadInfoByPkgname(String pkgName) {
    DownloadTask task = downloadTaskHashMap.get(pkgName);
    if (null == task) {
      return null;
    }
    return task.downloadInfo;
  }

  /**
   * 下载，并安装成功游戏
   * 
   * @param packageName
   */
  public void installedGame(String packageName) {
    DownloadTask task = downloadTaskHashMap.get(packageName);
    if ((null == task) || task.isBackgroundTask()) {
      return;
    }
    notifyDLTaskUIMsgToHandler(DownloadState.STATE_DOWNLOAD_INSTALLED, packageName, DownloadState.ERROR_NONE, task.isBackgroundTask());
  }

  private DownloadTask removeTask(String packageName) {
    if (null == packageName) {
      return null;
    }
    DownloadTask task = null;
    synchronized (downloadTaskHashMap) {
      task = downloadTaskHashMap.remove(packageName);
      // remove from DB
      downloadDao.deleteData(packageName);
      return task;
    }
  }

  public DownloadTask getDownloadTask(String pkgName) {
    return downloadTaskHashMap.get(pkgName);
  }

  public boolean DownloadTaskCotain(String pkgName) {
    return (downloadTaskHashMap.get(pkgName) != null);
  }

  private boolean checkDownloadSucess(String pkgname) {
    if (null == pkgname) {
      return false;
    }
    Logger.debug("checkDownloadSucess is " + pkgname);
    DownloadTask task = downloadTaskHashMap.get(pkgname);
    if (null == task) {
      return false;
    }
    // 下载成功，rename tmp download file
    try {
      // 文件重命名
      String pkgName = task.downloadInfo.getPackageName();
      int verCode = task.downloadInfo.getVersionCode();
      if (renameDownloadGameAPP(pkgName, verCode)) {
        // 后台任务不安装
        if (!task.isBackgroundTask()) {
          AppManagerCenter.installApk(task.downloadInfo);
        }
        return true;
      } else {
        // 文件意外删除了
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 将临时文件重命名
   * 
   * @param packageName
   * @return
   * @throws java.io.IOException
   */
  private boolean renameDownloadGameAPP(String packageName, int versionCode) throws IOException {
    File oldfile = new File(FileUtils.getTmpDownloadFile(packageName, versionCode));
    File newfile = new File(FileUtils.getAPKFilePath(packageName, versionCode));

    if (newfile.exists()) {
      newfile.delete();
    }

    if (oldfile.exists()) {
      return oldfile.renameTo(newfile);
    } else {
      // 文件意外删除了
      return false;
    }
  }
}
