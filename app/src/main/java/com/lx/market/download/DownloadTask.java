package com.lx.market.download;

import java.io.File;

import android.util.Log;

import com.lx.market.model.ClientInfo;
import com.lx.market.model.DownloadInfo;
import com.lx.market.threads.DownloadThreadPool;
import com.lx.market.utils.FileUtils;

/**
 * 下载执行模块
 * 
 * @author Tony
 */
public class DownloadTask {
  public DownloadInfo      downloadInfo;
  /**
   * 下载状态
   */
  public int               gameDownloadState          = DownloadState.STATE_DOWNLOAD_WAIT;
  /**
   * 已经下载的大小
   */
  public int               gameDownloadPostion;
  /**
   * 下载的执行线程，被中断或者暂停或者停止，请置成null，否则无法continue
   */
  private Download         download;
  protected int            progress;
  public int               loadFlag;                                                      // 任务标记
  /***** 下载的标记值 *****/
  /**
   * 后台任务***
   */
  private static final int LOADTASK_FLAG_BACKGROUD    = 0x0001;
  /**
   * 用户主动暂停下载，恢复下载的时候，需要用户主动恢复
   */
  private static final int LOADTASK_FLAG_STOP_BY_USER = 0x0002;

  public DownloadTask() {
    downloadInfo = new DownloadInfo();
  }

  public DownloadTask(DownloadInfo game) {
    downloadInfo = game;
    loadFlag = 0;
  }

  /**
   * 删除下载的临时文件
   */
  public static void deleteTmpDownloadFile(String packageName, int versionCode) {
    String downloadFile = FileUtils.getTmpDownloadFile(packageName, versionCode);
    // 删除临时文件
    File file = new File(downloadFile);
    if (file.exists()) {
      file.delete();
    }
  }

  public void setBackgroundTaskFlag(boolean isBackground) {
    if (isBackground) {
      loadFlag |= LOADTASK_FLAG_BACKGROUD;
    } else {
      loadFlag &= (~LOADTASK_FLAG_BACKGROUD);
    }
  }

  public boolean isBackgroundTask() {
    return ((loadFlag & LOADTASK_FLAG_BACKGROUD) != 0);
  }

  public boolean isUserPause() {
    return ((loadFlag & LOADTASK_FLAG_STOP_BY_USER) != 0);
  }

  /**
   * 设置暂停标识
   * 
   * @param isUserPaused
   *          true:用户主动暂停,false:用户取消暂停
   */
  private void setLoadTaskPauseFlag(boolean isUserPaused) {
    if (isUserPaused) {
      loadFlag |= LOADTASK_FLAG_STOP_BY_USER;
    } else {
      loadFlag &= (~LOADTASK_FLAG_STOP_BY_USER); // 移除暂停标识
    }
  }

  public void startTask(DownloadState listener) {
    setLoadTaskPauseFlag(false);
    if (isBackgroundTask() && (ClientInfo.networkType != ClientInfo.WIFI)) {
      // 后台任务，并且是非WIFI网络，不执行
      Log.e("startTask", "----startDownloadTask--isBackgroundTask-notWifi");
      return;
    }
    Log.e("startTask", "----downloading");
    if (null == download) {
      Log.e("startTask", "----download null");
      download = new Download(downloadInfo.getRefId(), downloadInfo.getDownloadUrl(), downloadInfo.getPackageName(), downloadInfo.getVersionCode(),
          (int) downloadInfo.getFileSize(), listener);
    } else {
      // 已经有下载线程
      Log.e("startTask", "----not null have download thread");
      return;
    }

    // 继续所有下载的时候,如果是错误下载，删除下载的临时文件
    if (DownloadState.STATE_DOWNLOAD_ERROR == gameDownloadState) {
      DownloadTask.deleteTmpDownloadFile(downloadInfo.getPackageName(), downloadInfo.getVersionCode());
      setDownloadSize((int) downloadInfo.getFileSize(), 0);
    }
    // if has running, do nothing
    // 启动下载,状态都置成 等待下载
    download.readyDownload();
    DownloadThreadPool.getDownloadThreadExe().execute(download);
  }

  /**
   * 必须把执行线程置成null,否则无法continue
   */
  public void pauseTask(boolean isUser) {
    // stop download
    setLoadTaskPauseFlag(isUser);
    if (null == download) {
      return;
    }
    download.stopDownloadByResult(DownloadState.ERROR_NONE);
    download = null;
  }

  public void resetDownloadRunnable() {
    download = null;
  }

  /**
   * 取消任务
   */
  public void cancelTask() {
    // cancel download
    if (null != download) {
      download.stopDownloadByResult(DownloadState.ERROR_NONE);
      download = null;
    }
    gameDownloadPostion = 0;
    gameDownloadState = 0;
    deleteTmpDownloadFile(downloadInfo.getPackageName(), downloadInfo.getVersionCode());
  }

  /**
   * 重置task
   */
  public void resetTask(DownloadInfo game) {
    cancelTask();
    downloadInfo = game;
  }

  /**
   * task 是否在下载中
   * 
   * @return
   */
  public boolean taskIsLoading() {
    if (null != download) {
      return true;
    }
    return false;
  }

  public void setDownloadSize(int totalSize, int loadSize) {
    gameDownloadPostion = loadSize;
    downloadInfo.setFileSize(totalSize);
    loadSize = loadSize >> 10;
    totalSize = totalSize >> 10;
    if (totalSize != 0) {
      progress = loadSize * 100 / totalSize;
    }
  }
}
