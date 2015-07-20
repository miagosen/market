package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lidroid.xutils.db.annotation.Table;
import com.lx.market.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Table(name = "assembly_info_bto")
public class AssemblyInfoBto extends BaseEntity {
  @Expose
  @SerializedName("assId")
  private int assemblyId;

  @Expose
  @SerializedName("assName")
  private String assemblyName;

  @Expose
  @SerializedName("type")
  private int type;//  1-Banner条,	2-宫格N*1(列*行),	3-列表1*N(列*行)

  @Expose
  @SerializedName("appLst")
  private List<AppInfoBto> appInfoList = new ArrayList<AppInfoBto>();

  public int getAssemblyId () {
	return assemblyId;
  }

  public void setAssemblyId (int assemblyId) {
	this.assemblyId = assemblyId;
  }

  public int getType () {
	return type;
  }

  public void setType (int type) {
	this.type = type;
  }

  public List<AppInfoBto> getAppInfoList () {
	return appInfoList;
  }

  public void setAppInfoList (List<AppInfoBto> appInfoList) {
	this.appInfoList = appInfoList;
  }

  public void addAppInfo (AppInfoBto appInfo) {
	this.appInfoList.add(appInfo);
  }

  public String getAssemblyName () {
	return assemblyName;
  }

  public void setAssemblyName (String assemblyName) {
	this.assemblyName = assemblyName;
  }

  @Override
  public String toString () {
	return "AssemblyInfoBto{" +
	  "assemblyId=" + assemblyId +
	  ", assemblyName='" + assemblyName + '\'' +
	  ", type=" + type +
	  ", appInfoList=" + appInfoList +
	  '}';
  }
}
