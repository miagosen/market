package com.lx.market.entities;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lx.market.model.BaseEntity;
import com.lx.market.network.model.AppInfoBto;

@Table(name = "AppInfoBto")
public class LocAppInfoBto extends BaseEntity {

  @Column(column = "refId")
  private int refId;//apkId或栏目ID

  @Column(column = "name")
  private String name;//应用名称或栏目名称

  @Column(column = "imgUrl")
  private String imgUrl;//图标下载地址

  @Column(column = "packageName")
  private String packageName;//包名

  @Column(column = "downUrl")
  private String downUrl;//APK下载地址

  @Column(column = "md5")
  private String md5;

  @Column(column = "resType")
  private int resType;//1:应用2:栏目

  @Column(column = "fileSize")
  private long fileSize;//文件大小

  @Column(column = "downTimes")
  private int downTimes;//下载次数

  @Column(column = "hot")
  private int hot;//1:图标标优0:不标优

  @Column(column = "briefDesc")
  private String briefDesc;//应用简介

  @Column(column = "versionCode")
  private int versionCode;//应用版本号

  @Column(column = "versionName")
  private String versionName;//应用版本名称

  @Column(column = "sType")
  private int sType;//数据类型分类，例如：应用更新，搜索页应用推荐

    public LocAppInfoBto() {
    }

    public LocAppInfoBto(AppInfoBto info,int stype) {
        this.refId = info.getRefId();
        this.name = info.getName();
        this.imgUrl =  info.getImgUrl();
        this.packageName = info.getPackageName();
        this.downUrl = info.getDownUrl();
        this.md5 = info.getMd5();
        this.resType = info.getResType();
        this.fileSize = info.getFileSize();
        this.downTimes = info.getDownTimes();
        this.hot = info.getHot();
        this.briefDesc = info.getBriefDesc();
        this.versionCode = info.getVersionCode();
        this.versionName = info.getVersionName();
        this.sType = stype;
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

    public int getsType() {
        return sType;
    }

    public void setsType(int sType) {
        this.sType = sType;
    }

    @Override
    public String toString() {
        return "LocAppInfoBto{" +
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
                ", sType='" + sType + '\'' +
                '}';
    }
}
