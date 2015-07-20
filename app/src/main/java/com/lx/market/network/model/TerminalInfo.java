package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TerminalInfo implements Cloneable {

  @Expose
  @SerializedName("hman")
  private String hsman;

  @Expose
  @SerializedName("htype")
  private String hstype;

  @Expose
  @SerializedName("osVer")
  private String osVer;

  @Expose
  @SerializedName("sWidth")
  private short screenWidth;

  @Expose
  @SerializedName("sHeight")
  private short screenHeight;

  @Expose
  @SerializedName("ramSize")
  private long ramSize;

  @Expose
  @SerializedName("imsi")
  private String imsi;

  @Expose
  @SerializedName("imei")
  private String imei;

  @Expose
  @SerializedName("lac")
  private short lac;

  @Expose
  @SerializedName("ip")
  private String ip;

  @Expose
  @SerializedName("netType")
  private byte networkType;

  @Expose
  @SerializedName("chId")
  private String channelId;

  @Expose
  @SerializedName("appId")
  private String appId;

  @Expose
  @SerializedName("apkVer")
  private int apkVersion;

  @Expose
  @SerializedName("pName")
  private String packageName;

  @Expose
  @SerializedName("apkVerName")
  private String apkVersionName;

  @Expose
  @SerializedName("mac")
  private String mac;

  @Override
  public Object clone () throws CloneNotSupportedException {
	return super.clone();
  }

  public String getHsman () {
	return hsman;
  }

  public void setHsman (String hsman) {
	this.hsman = hsman;
  }

  public String getHstype () {
	return hstype;
  }

  public void setHstype (String hstype) {
	this.hstype = hstype;
  }

  public String getOsVer () {
	return osVer;
  }

  public void setOsVer (String osVer) {
	this.osVer = osVer;
  }

  public short getScreenWidth () {
	return screenWidth;
  }

  public void setScreenWidth (short screenWidth) {
	this.screenWidth = screenWidth;
  }

  public short getScreenHeight () {
	return screenHeight;
  }

  public void setScreenHeight (short screenHeight) {
	this.screenHeight = screenHeight;
  }

  public long getRamSize () {
	return ramSize;
  }

  public void setRamSize (long ramSize) {
	this.ramSize = ramSize;
  }

  public String getImsi () {
	return imsi;
  }

  public void setImsi (String imsi) {
	this.imsi = imsi;
  }

  public String getImei () {
	return imei;
  }

  public void setImei (String imei) {
	this.imei = imei;
  }

  public short getLac () {
	return lac;
  }

  public void setLac (short lac) {
	this.lac = lac;
  }

  public String getIp () {
	return ip;
  }

  public void setIp (String ip) {
	this.ip = ip;
  }

  public byte getNetworkType () {
	return networkType;
  }

  public void setNetworkType (byte networkType) {
	this.networkType = networkType;
  }

  public String getChannelId () {
	return channelId;
  }

  public void setChannelId (String channelId) {
	this.channelId = channelId;
  }

  public String getAppId () {
	return appId;
  }

  public void setAppId (String appId) {
	this.appId = appId;
  }

  public int getApkVersion () {
	return apkVersion;
  }

  public void setApkVersion (int apkVersion) {
	this.apkVersion = apkVersion;
  }

  public String getPackageName () {
	return packageName;
  }

  public void setPackageName (String packageName) {
	this.packageName = packageName;
  }

  public String getApkVersionName () {
	return apkVersionName;
  }

  public void setApkVersionName (String apkVersionName) {
	this.apkVersionName = apkVersionName;
  }

  public String getMac () {
	return mac;
  }

  public void setMac (String mac) {
	this.mac = mac;
  }

  @Override
  public String toString () {
	return "TerminalInfo{" +
	  "hsman='" + hsman + '\'' +
	  ", hstype='" + hstype + '\'' +
	  ", osVer='" + osVer + '\'' +
	  ", screenWidth=" + screenWidth +
	  ", screenHeight=" + screenHeight +
	  ", ramSize=" + ramSize +
	  ", imsi='" + imsi + '\'' +
	  ", imei='" + imei + '\'' +
	  ", lac=" + lac +
	  ", ip='" + ip + '\'' +
	  ", networkType=" + networkType +
	  ", channelId='" + channelId + '\'' +
	  ", appId='" + appId + '\'' +
	  ", apkVersion=" + apkVersion +
	  ", packageName='" + packageName + '\'' +
	  ", apkVersionName='" + apkVersionName + '\'' +
	  ", mac='" + mac + '\'' +
	  '}';
  }
}
