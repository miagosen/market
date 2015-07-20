package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SilentDownInfoBto {

  @Expose
  @SerializedName("pName")
  private String packageName;

  @Expose
  @SerializedName("downUrl")
  private String downUrl;

  @Expose
  @SerializedName("md5")
  private String md5;

  @Expose
  @SerializedName("verCode")
  private int versionCode;

  @Expose
  @SerializedName("downType")
  private int downType;

  @Expose
  @SerializedName("verName")
  private String versionName;

  @Expose
  @SerializedName("name")
  private String resName;

  public String getPackageName () {
	return packageName;
  }

  public void setPackageName (String packageName) {
	this.packageName = packageName;
  }

  public String getDownUrl () {
	return downUrl;
  }

  public void setDownUrl (String downUrl) {
	this.downUrl = downUrl;
  }

  public String getMd5 () {
	return md5;
  }

  public void setMd5 (String md5) {
	this.md5 = md5;
  }

  public int getVersionCode () {
	return versionCode;
  }

  public void setVersionCode (int versionCode) {
	this.versionCode = versionCode;
  }

  public int getDownType () {
	return downType;
  }

  public void setDownType (int downType) {
	this.downType = downType;
  }

  public String getVersionName () {
	return versionName;
  }

  public void setVersionName (String versionName) {
	this.versionName = versionName;
  }

  public String getResName () {
	return resName;
  }

  public void setResName (String resName) {
	this.resName = resName;
  }

}
