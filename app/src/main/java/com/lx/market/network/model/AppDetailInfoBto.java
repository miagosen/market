package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lidroid.xutils.db.annotation.Column;
import com.lx.market.model.BaseEntity;

/**
 * Created by Antikvo.Miao on 2014/8/7.
 */
public class AppDetailInfoBto extends BaseEntity {
  @Expose
  @SerializedName("id")
  @Column(column = "appid")
  private int appid;
  @Expose
  @SerializedName("apkName")
  @Column(column = "apkName")
  private String apkName;//应用名称
  @Expose
  @SerializedName("pName")
  @Column(column = "pName")
  private String pName;//应用包名
  @Expose
  @SerializedName("verCode")
  @Column(column = "verCode")
  private int verCode;//应用版本号
  @Expose
  @SerializedName("verName")
  @Column(column = "verName")
  private String verName;//应用版本名称
  @Expose
  @SerializedName("downUrl")
  @Column(column = "downUrl")
  private String downUrl;//下载地址
  @Expose
  @SerializedName("md5")
  @Column(column = "md5")
  private String md5;//Md5
  @Expose
  @SerializedName("desc")
  @Column(column = "desc")
  private String desc;//应用详情信息
  @Expose
  @SerializedName("fileSize")
  @Column(column = "fileSize")
  private long fileSize;//文件大小
  @Expose
  @SerializedName("shotImg")
  @Column(column = "shotImg")
  private String shotImg;//应用截图,存在多张情况以”,”隔开
  @Expose
  @SerializedName("label")
  @Column(column = "label")
  private String label;//应用标签
  @Expose
  @SerializedName("downNum")
  @Column(column = "downNum")
  private int downNum;//下载次数
  @Expose
  @SerializedName("company")
  @Column(column = "company")
  private String company;
  @Expose
  @SerializedName("uptime")
  @Column(column = "uptime")
  private String uptime;//更新时间
  @Expose
  @SerializedName("iconUrl")
  @Column(column = "iconUrl")
  private String iconUrl;

  public String getApkName () {
	return apkName;
  }

  public void setApkName (String apkName) {
	this.apkName = apkName;
  }

  public String getpName () {
	return pName;
  }

  public void setpName (String pName) {
	this.pName = pName;
  }

  public int getVerCode () {
	return verCode;
  }

  public void setVerCode (int verCode) {
	this.verCode = verCode;
  }

  public String getVerName () {
	return verName;
  }

  public void setVerName (String verName) {
	this.verName = verName;
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

  public String getDesc () {
	return desc;
  }

  public void setDesc (String desc) {
	this.desc = desc;
  }

  public long getFileSize () {
	return fileSize;
  }

  public void setFileSize (long fileSize) {
	this.fileSize = fileSize;
  }

  public String getShotImg () {
	return shotImg;
  }

  public void setShotImg (String shotImg) {
	this.shotImg = shotImg;
  }

  public String getLabel () {
	return label;
  }

  public void setLabel (String label) {
	this.label = label;
  }

  public int getDownNum () {
	return downNum;
  }

  public void setDownNum (int downNum) {
	this.downNum = downNum;
  }

  public String getCompany () {
	return company;
  }

  public void setCompany (String company) {
	this.company = company;
  }

  public String getUptime () {
	return uptime;
  }

  public void setUptime (String uptime) {
	this.uptime = uptime;
  }

  public String getIconUrl () {
	return iconUrl;
  }

  public void setIconUrl (String iconUrl) {
	this.iconUrl = iconUrl;
  }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    @Override
  public String toString () {
	return "AppDetailInfo{" +
	  "appid=" + appid +
	  ", apkName='" + apkName + '\'' +
	  ", pName='" + pName + '\'' +
	  ", verCode=" + verCode +
	  ", verName='" + verName + '\'' +
	  ", downUrl='" + downUrl + '\'' +
	  ", md5='" + md5 + '\'' +
	  ", desc='" + desc + '\'' +
	  ", fileSize=" + fileSize +
	  ", shotImg='" + shotImg + '\'' +
	  ", label='" + label + '\'' +
	  ", downNum=" + downNum +
	  ", company='" + company + '\'' +
	  ", uptime='" + uptime + '\'' +
	  ", iconUrl='" + iconUrl + '\'' +
	  '}';
  }
}
