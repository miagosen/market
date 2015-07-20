package com.lx.market.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.lx.market.database.DatabaseHelper;
import com.lx.market.database.DownloadInfoTable;
import com.lx.market.download.DownloadState;
import com.lx.market.download.DownloadTask;
import com.lx.market.model.DownloadInfo;

import java.util.HashMap;

/**
 * Created by Antikvo.Miao on 2014/10/17.
 */
public class DownloadDao {
  /**
   * 下载数据库列名
   */
  public static String mColunmName[] = new String[]{BaseColumns._ID,
	DownloadInfoTable.DOWNLOAD_INFO_1_REF_ID, DownloadInfoTable.DOWNLOAD_INFO_2_PACKAGE_NAME,
	DownloadInfoTable.DOWNLOAD_INFO_3_FILE_NAME, DownloadInfoTable.DOWNLOAD_INFO_4_VERSION_CODE,
	DownloadInfoTable.DOWNLOAD_INFO_5_VERSION_NAME, DownloadInfoTable.DOWNLOAD_INFO_6_FILE_SIZE,
	DownloadInfoTable.DOWNLOAD_INFO_7_LOADED_SIZE, DownloadInfoTable.DOWNLOAD_INFO_8_APK_DOWNLOAD_URL,
	DownloadInfoTable.DOWNLOAD_INFO_9_DOWNLOAD_STATE, DownloadInfoTable.DOWNLOAD_INFO_10_TASK_FLAG,
	DownloadInfoTable.DOWNLOAD_INFO_11_POSITION, DownloadInfoTable.DOWNLOAD_INFO_12_MD5,DownloadInfoTable.DOWNLOAD_INFO_13_ICON_URL};

  private static DownloadDao instance;

  public static DownloadDao getInstance () {
	if (null == instance) {
	  instance = new DownloadDao();
	}
	return instance;
  }

  /**
   * 修改下载状态，只需记录 STATE_DOWNLOAD_SUCESS，STATE_DOWNLOAD_ERROR，STATE_DOWNLOAD_PAUSE，恢复时，用于区别下载状态
   *
   * @param pkgName 包名
   * @param state   状态
   */
  public void updateState (String pkgName, int state) {
	if ((DownloadState.STATE_DOWNLOAD_SUCESS == state)
	  || (DownloadState.STATE_DOWNLOAD_ERROR == state)
	  || (DownloadState.STATE_DOWNLOAD_PAUSE == state)) {
	  ContentValues contentValues = new ContentValues();
	  contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_9_DOWNLOAD_STATE, state);
	  DatabaseHelper.update(DownloadInfoTable.TABLE_NAME_DOWNLOAD_INFO,
		contentValues, DownloadInfoTable.DOWNLOAD_INFO_2_PACKAGE_NAME + "=?",
		new String[]{pkgName});
	}
  }

  /**
   * 更新下载进度
   *
   * @param pkgName 包名
   * @param pos     进度
   */
  public void updateDownloadSize (String pkgName, long totalSize, long pos) {
	ContentValues contentValues = new ContentValues();
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_6_FILE_SIZE, totalSize);
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_7_LOADED_SIZE, pos);
	DatabaseHelper.update(DownloadInfoTable.TABLE_NAME_DOWNLOAD_INFO,
	  contentValues, DownloadInfoTable.DOWNLOAD_INFO_2_PACKAGE_NAME + "=?",
	  new String[]{pkgName});
  }

  public void updateTaskFlag (DownloadTask downloadTask) {
	if (null == downloadTask) {
	  return;
	}
	ContentValues contentValues = new ContentValues();
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_10_TASK_FLAG, downloadTask.loadFlag);
	String pkgName = downloadTask.downloadInfo.getPackageName();
	DatabaseHelper.update(DownloadInfoTable.TABLE_NAME_DOWNLOAD_INFO,
	  contentValues, DownloadInfoTable.DOWNLOAD_INFO_2_PACKAGE_NAME + "=?",
	  new String[]{pkgName});
  }

  /**
   * 创建游戏下载任务
   */
  public boolean insertDownloadInfo (DownloadTask task) {
	if (null == task) {
	  return false;
	}
	DownloadInfo downloadInfo = task.downloadInfo;
	ContentValues contentValues = new ContentValues();
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_1_REF_ID, downloadInfo.getRefId());
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_2_PACKAGE_NAME, downloadInfo.getPackageName());
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_3_FILE_NAME, downloadInfo.getFileName());
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_4_VERSION_CODE, downloadInfo.getVersionCode());
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_5_VERSION_NAME, downloadInfo.getVersionName());
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_6_FILE_SIZE, downloadInfo.getFileSize());
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_7_LOADED_SIZE, task.gameDownloadPostion);
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_8_APK_DOWNLOAD_URL, downloadInfo.getDownloadUrl());
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_9_DOWNLOAD_STATE, task.gameDownloadState);
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_10_TASK_FLAG, task.loadFlag);
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_11_POSITION, downloadInfo.getPosition());
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_12_MD5, downloadInfo.getMd5());
	contentValues.put(DownloadInfoTable.DOWNLOAD_INFO_13_ICON_URL, downloadInfo.getIconUrl());
	DatabaseHelper.insert(DownloadInfoTable.TABLE_NAME_DOWNLOAD_INFO, null, contentValues);
	return true;
  }

  /**
   * 获取所有的下载任务
   *
   * @return
   */
  public HashMap<String, DownloadTask> getAllDownloadExeTask () {
	HashMap<String, DownloadTask> dataMap = new HashMap<String, DownloadTask>();
	Cursor cursor = DatabaseHelper
	  .query(DownloadInfoTable.TABLE_NAME_DOWNLOAD_INFO, mColunmName,
		null, null, null, null, null);
	DownloadTask task;
	if (cursor != null) {
	  while (cursor.moveToNext()) {
		task = sqlToDownloadTask(cursor);
		dataMap.put(task.downloadInfo.getPackageName(), task);
	  }
	  cursor.close();
	}

	return dataMap;
  }

  /**
   * 转换为下载任务
   */
  private DownloadTask sqlToDownloadTask (Cursor cursor) {
	DownloadTask data = new DownloadTask();
	DownloadInfo downloadInfo = data.downloadInfo;
	downloadInfo.setId(cursor.getInt(cursor.getColumnIndex(mColunmName[0])));
	downloadInfo.setRefId(cursor.getInt(cursor.getColumnIndex(mColunmName[1])));
	downloadInfo.setPackageName(cursor.getString(cursor.getColumnIndex(mColunmName[2])));
	downloadInfo.setFileName(cursor.getString(cursor.getColumnIndex(mColunmName[3])));
	downloadInfo.setVersionCode(cursor.getInt(cursor.getColumnIndex(mColunmName[4])));
	downloadInfo.setVersionName(cursor.getString(cursor.getColumnIndex(mColunmName[5])));
	downloadInfo.setFileSize(cursor.getInt(cursor.getColumnIndex(mColunmName[6])));
	downloadInfo.setLoadedSize(cursor.getInt(cursor.getColumnIndex(mColunmName[7])));
	int loadedSize = (int) cursor.getInt(cursor.getColumnIndex(mColunmName[7]));
	downloadInfo.setDownloadUrl(cursor.getString(cursor.getColumnIndex(mColunmName[8])));
	downloadInfo.setDownloadState(cursor.getInt(cursor.getColumnIndex(mColunmName[9])));
	data.loadFlag = cursor.getInt(cursor.getColumnIndex(mColunmName[10]));
	downloadInfo.setPosition(cursor.getInt(cursor.getColumnIndex(mColunmName[11])));
	downloadInfo.setMd5(cursor.getString(cursor.getColumnIndex(DownloadInfoTable.DOWNLOAD_INFO_12_MD5)));
	downloadInfo.setIconUrl(cursor.getString(cursor.getColumnIndex(DownloadInfoTable.DOWNLOAD_INFO_13_ICON_URL)));
	data.setDownloadSize((int) downloadInfo.getFileSize(), loadedSize);
	return data;
  }

  /**
   * 根据pkgname删除一个数据
   */
  public void deleteData (String pkgName) {
	DatabaseHelper.delete(DownloadInfoTable.TABLE_NAME_DOWNLOAD_INFO,
	  DownloadInfoTable.DOWNLOAD_INFO_2_PACKAGE_NAME + "=?", new String[]{pkgName});
  }
}
