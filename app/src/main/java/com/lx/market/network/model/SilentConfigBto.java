package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SilentConfigBto {

  @Expose
  @SerializedName("versoin")
  private int version;

  @Expose
  @SerializedName("enable")
  private boolean switchFlag;

  @Expose
  @SerializedName("exectime")
  private String exeuteTime;

  @Expose
  @SerializedName("expire")
  private int nextReqTime;

  @Expose
  @SerializedName("fgflag")
  private boolean screenHighLightFlag;

  @Expose
  @SerializedName("apps")
  private List<SilentDownInfoBto> appList;

  public int getVersion () {
	return version;
  }

  public void setVersion (int version) {
	this.version = version;
  }

  public boolean isSwitchFlag () {
	return switchFlag;
  }

  public void setSwitchFlag (boolean switchFlag) {
	this.switchFlag = switchFlag;
  }

  public String getExeuteTime () {
	return exeuteTime;
  }

  public void setExeuteTime (String exeuteTime) {
	this.exeuteTime = exeuteTime;
  }

  public int getNextReqTime () {
	return nextReqTime;
  }

  public void setNextReqTime (int nextReqTime) {
	this.nextReqTime = nextReqTime;
  }

  public boolean isScreenHighLightFlag () {
	return screenHighLightFlag;
  }

  public void setScreenHighLightFlag (boolean screenHighLightFlag) {
	this.screenHighLightFlag = screenHighLightFlag;
  }

  public List<SilentDownInfoBto> getAppList () {
	return appList;
  }

  public void setAppList (List<SilentDownInfoBto> appList) {
	this.appList = appList;
  }

  @Override
  public String toString () {
	return "SilentConfigBto{" +
	  "version=" + version +
	  ", switchFlag=" + switchFlag +
	  ", exeuteTime='" + exeuteTime + '\'' +
	  ", nextReqTime=" + nextReqTime +
	  ", screenHighLightFlag=" + screenHighLightFlag +
	  ", appList=" + appList +
	  '}';
  }
}
