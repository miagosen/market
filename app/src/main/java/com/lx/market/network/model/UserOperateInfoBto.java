package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOperateInfoBto {

  @Expose
  @SerializedName("apkName")
  private String apkName;

  @Expose
  @SerializedName("pName")
  private String packageName;

  @Expose
  @SerializedName("act")
  private int action;

  @Expose
  @SerializedName("src")
  private String src;

  public String getApkName () {
	return apkName;
  }

  public void setApkName (String apkName) {
	this.apkName = apkName;
  }

  public String getPackageName () {
	return packageName;
  }

  public void setPackageName (String packageName) {
	this.packageName = packageName;
  }

  public int getAction () {
	return action;
  }

  public void setAction (int action) {
	this.action = action;
  }

  public String getSrc () {
	return src;
  }

  public void setSrc (String src) {
	this.src = src;
  }
}
