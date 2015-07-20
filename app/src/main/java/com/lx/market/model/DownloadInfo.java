package com.lx.market.model;

import com.lx.market.network.model.AppInfoBto;

/**
 * Created by Antikvo.Miao on 2014/8/29.
 */
public class DownloadInfo extends BaseEntity {
  private static final long serialVersionUID = 149599583210572365L;
  private int refId;
  private String packageName;
  private String versionName;
  private int versionCode;
  private int position;
  private String md5;
  private String downloadUrl;
  private long fileSize;
  private long loadedSize;
  private String fileSavePath;
  private String fileName;
  private int downloadState;
  private int taskFlag;//任务标示
  private String iconUrl;

  public String getIconUrl () {
	return iconUrl;
  }

  public void setIconUrl (String iconUrl) {
	this.iconUrl = iconUrl;
  }

  public DownloadInfo (AppInfoBto appInfoBto, int position) {
	this.position = position;
	this.refId = appInfoBto.getRefId();
	this.packageName = appInfoBto.getPackageName();
	this.versionCode = appInfoBto.getVersionCode();
	this.versionName = appInfoBto.getVersionName();
	this.fileName = appInfoBto.getName();
	this.md5 = appInfoBto.getMd5();
	this.downloadUrl = appInfoBto.getDownUrl();
	this.fileSize = appInfoBto.getFileSize();
	this.iconUrl = appInfoBto.getIconUrl();
  }

  public DownloadInfo () {
  }

  public int getRefId () {
	return refId;
  }

  public void setRefId (int refId) {
	this.refId = refId;
  }

  public static long getSerialVersionUID () {
	return serialVersionUID;
  }

  public String getPackageName () {
	return packageName;
  }

  public void setPackageName (String packageName) {
	this.packageName = packageName;
  }

  public String getVersionName () {
	return versionName;
  }

  public void setVersionName (String versionName) {
	this.versionName = versionName;
  }

  public int getVersionCode () {
	return versionCode;
  }

  public void setVersionCode (int versionCode) {
	this.versionCode = versionCode;
  }

  public int getPosition () {
	return position;
  }

  public void setPosition (int position) {
	this.position = position;
  }

  public String getMd5 () {
	return md5;
  }

  public void setMd5 (String md5) {
	this.md5 = md5;
  }

  public String getDownloadUrl () {
	return downloadUrl;
  }

  public void setDownloadUrl (String downloadUrl) {
	this.downloadUrl = downloadUrl;
  }

  public long getFileSize () {
	return fileSize;
  }

  public void setFileSize (long fileSize) {
	this.fileSize = fileSize;
  }

  public long getLoadedSize () {
	return loadedSize;
  }

  public void setLoadedSize (long loadedSize) {
	this.loadedSize = loadedSize;
  }

  public String getFileSavePath () {
	return fileSavePath;
  }

  public void setFileSavePath (String fileSavePath) {
	this.fileSavePath = fileSavePath;
  }

  public String getFileName () {
	return fileName;
  }

  public void setFileName (String fileName) {
	this.fileName = fileName;
  }

  public int getDownloadState () {
	return downloadState;
  }

  public void setDownloadState (int downloadState) {
	this.downloadState = downloadState;
  }
}
