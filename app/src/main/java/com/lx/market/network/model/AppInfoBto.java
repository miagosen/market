package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lx.market.model.BaseEntity;

@Table(name = "app_info_bto")
public class AppInfoBto extends BaseEntity {

  private static final long serialVersionUID = 5000061078671708189L;
  @Expose
  @SerializedName("id")
  @Column(column = "refId")
  private int refId;//apkId或栏目ID

  @Expose
  @SerializedName("name")
  @Column(column = "name")
  private String name;//应用名称或栏目名称

  @Expose
  @SerializedName("imgUrl")
  @Column(column = "imgUrl")
  private String imgUrl;//大图下载地址
  @Expose
  @SerializedName("iconUrl")
  @Column(column = "iconUrl")
  private String iconUrl;//图标下载地址

  @Expose
  @SerializedName("pName")
  @Column(column = "pName")
  private String packageName;//包名

  @Expose
  @SerializedName("downUrl")
  @Column(column = "downUrl")
  private String downUrl;//APK下载地址

  @Expose
  @SerializedName("md5")
  @Column(column = "md5")
  private String md5;

  @Expose
  @SerializedName("type")
  @Column(column = "type")
  private int resType;//1:应用2:栏目;3 WAP

  @Expose
  @SerializedName("fileSize")
  @Column(column = "fileSize")
  private long fileSize;//文件大小

  @Expose
  @SerializedName("downTm")
  @Column(column = "downTm")
  private int downTimes;//下载次数

  @Expose
  @SerializedName("hot")
  @Column(column = "hot")
  private int hot;//1:图标标优0:不标优

  @Expose
  @SerializedName("brief")
  @Column(column = "brief")
  private String briefDesc;//应用简介

  @Expose
  @SerializedName("verCode")
  @Column(column = "verCode")
  private int versionCode;//应用版本号

  @Expose
  @SerializedName("verName")
  @Column(column = "verName")
  private String versionName;//应用版本名称

  public String getIconUrl () {
	return iconUrl;
  }

  public void setIconUrl (String iconUrl) {
	this.iconUrl = iconUrl;
  }

  public int getRefId () {
	return refId;
  }

  public void setRefId (int refId) {
	this.refId = refId;
  }

  public String getName () {
	return name;
  }

  public void setName (String name) {
	this.name = name;
  }

  public String getImgUrl () {
	return imgUrl;
  }

  public void setImgUrl (String imgUrl) {
	this.imgUrl = imgUrl;
  }

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

  public int getResType () {
	return resType;
  }

  public void setResType (int resType) {
	this.resType = resType;
  }

  public long getFileSize () {
	return fileSize;
  }

  public void setFileSize (long fileSize) {
	this.fileSize = fileSize;
  }

  public int getDownTimes () {
	return downTimes;
  }

  public void setDownTimes (int downTimes) {
	this.downTimes = downTimes;
  }

  public int getHot () {
	return hot;
  }

  public void setHot (int hot) {
	this.hot = hot;
  }

  public String getBriefDesc () {
	return briefDesc;
  }

  public void setBriefDesc (String briefDesc) {
	this.briefDesc = briefDesc;
  }

  public int getVersionCode () {
	return versionCode;
  }

  public void setVersionCode (int versionCode) {
	this.versionCode = versionCode;
  }

  public String getVersionName () {
	return versionName;
  }

  public void setVersionName (String versionName) {
	this.versionName = versionName;
  }

  @Override
  public boolean equals (Object o) {
	if (this == o) return true;
	if (o == null) return false;
	if (o instanceof AppInfoBto) {
	  AppInfoBto that = (AppInfoBto) o;
	  if (refId == that.refId) return true;
	}
	return false;
  }

  @Override
  public int hashCode () {
	int result = refId;
	result = 31 * result + (name != null ? name.hashCode() : 0);
	result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
	result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
	result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
	result = 31 * result + (downUrl != null ? downUrl.hashCode() : 0);
	result = 31 * result + (md5 != null ? md5.hashCode() : 0);
	result = 31 * result + resType;
	result = 31 * result + (int) (fileSize ^ (fileSize >>> 32));
	result = 31 * result + downTimes;
	result = 31 * result + hot;
	result = 31 * result + (briefDesc != null ? briefDesc.hashCode() : 0);
	result = 31 * result + versionCode;
	result = 31 * result + (versionName != null ? versionName.hashCode() : 0);
	return result;
  }

  @Override
  public String toString () {
	return "AppInfo{" +
	  "refId=" + refId +
	  ", name='" + name + '\'' +
	  ", imgUrl='" + imgUrl + '\'' +
	  ", packageName='" + packageName + '\'' +
	  ", downUrl='" + downUrl + '\'' +
	  ", md5='" + md5 + '\'' +
	  ", resType=" + resType +
	  ", fileSize=" + fileSize +
	  ", downTimes=" + downTimes +
	  ", hot=" + hot +
	  ", briefDesc='" + briefDesc + '\'' +
	  ", versionCode=" + versionCode +
	  ", versionName='" + versionName + '\'' +
	  '}';
  }
}
