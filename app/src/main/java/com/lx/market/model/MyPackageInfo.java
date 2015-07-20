package com.lx.market.model;

import java.io.Serializable;

public class MyPackageInfo implements Serializable {
  private static final long serialVersionUID = -4644479123519038658L;

  private String packageName = "";

  private int versionCode;

  private String apkPath;

  private int position;

  private String activityName;

  private boolean imeInstall = false;

  private boolean imeOpen = true;

  public String getActivityName () {
	return activityName;
  }

  public void setActivityName (String activityName) {
	this.activityName = activityName;
  }

  public MyPackageInfo (String packageName) {
	this.packageName = packageName;
  }

  public MyPackageInfo (String packageName, int versionCode) {
	this.packageName = packageName;
	this.versionCode = versionCode;
  }

  public MyPackageInfo (String packageName, int versionCode, int position, int source) {
	this.packageName = packageName;
	this.versionCode = versionCode;
	this.position = position;
  }

  public MyPackageInfo (String packageName, int versionCode, int position, int source, String activityName) {
	this.packageName = packageName;
	this.versionCode = versionCode;
	this.position = position;
	this.activityName = activityName;
  }

  public MyPackageInfo (String packageName, int versionCode, int position, int source, boolean imeOpen) {
	this.packageName = packageName;
	this.versionCode = versionCode;
	this.position = position;
	this.imeOpen = imeOpen;
  }

  public MyPackageInfo (String packageName, int versionCode, int position, int source, boolean imeInstall, boolean imeOpen) {
	super();
	this.packageName = packageName;
	this.versionCode = versionCode;
	this.position = position;
	this.imeInstall = imeInstall;
	this.imeOpen = imeOpen;
  }

  public String getPackageName () {
	return packageName;
  }

  public void setPackageName (String packageName) {
	this.packageName = packageName;
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

  public boolean isImeOpen () {
	return imeOpen;
  }

  public void setImeOpen (boolean imeOpen) {
	this.imeOpen = imeOpen;
  }

  public boolean isImeInstall () {
	return imeInstall;
  }

  public void setImeInstall (boolean imeInstall) {
	this.imeInstall = imeInstall;
  }

  public String getApkPath () {
	return apkPath;
  }

  public void setApkPath (String apkPath) {
	this.apkPath = apkPath;
  }

  @Override
  public boolean equals (Object o) {
	if (o instanceof MyPackageInfo) {
	  MyPackageInfo info = (MyPackageInfo) o;
	  return info.getPackageName().equals(packageName) && info.getVersionCode() == versionCode;
	}
	return false;
  }

  @Override
  public int hashCode () {
	return packageName.hashCode() + versionCode;
  }

  @Override
  public String toString () {
	return "MyPackageInfo{" +
	  "packageName='" + packageName + '\'' +
	  ", versionCode=" + versionCode +
	  ", apkPath='" + apkPath + '\'' +
	  ", position=" + position +
	  ", activityName='" + activityName + '\'' +
	  ", imeInstall=" + imeInstall +
	  ", imeOpen=" + imeOpen +
	  '}';
  }
}
