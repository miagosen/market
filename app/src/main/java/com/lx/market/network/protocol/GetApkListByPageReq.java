package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.PackageInfoBto;
import com.lx.market.network.serializer.SignalCode;

import java.util.List;

@SignalCode(messageCode = 101015)
public class GetApkListByPageReq {

  @Expose
  @SerializedName("sWidth")
  private short screenWidth;

  @Expose
  @SerializedName("sHeight")
  private short screenHeight;

  @Expose
  @SerializedName("assId")
  private int assemblyId;

  @Expose
  @SerializedName("start")
  private int start;

  @Expose
  @SerializedName("fixed")
  private int fixedLength;

  @Expose
  @SerializedName("marketId")
  private String marketId;

  @Expose
  @SerializedName("installs")
  private List<PackageInfoBto> installList;

  @Expose
  @SerializedName("uninstalls")
  private List<PackageInfoBto> uninstallList;

  public List<PackageInfoBto> getInstallList () {
	return installList;
  }

  public void setInstallList (List<PackageInfoBto> installList) {
	this.installList = installList;
  }

  public List<PackageInfoBto> getUninstallList () {
	return uninstallList;
  }

  public void setUninstallList (List<PackageInfoBto> uninstallList) {
	this.uninstallList = uninstallList;
  }

  public int getAssemblyId () {
	return assemblyId;
  }

  public void setAssemblyId (int assemblyId) {
	this.assemblyId = assemblyId;
  }

  public int getStart () {
	return start;
  }

  public void setStart (int start) {
	this.start = start;
  }

  public int getFixedLength () {
	return fixedLength;
  }

  public void setFixedLength (int fixedLength) {
	this.fixedLength = fixedLength;
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

  public String getMarketId () {
	return marketId;
  }

  public void setMarketId (String marketId) {
	this.marketId = marketId;
  }

  @Override
  public String toString () {
	return "GetApkListByPageReq{" +
	  "screenWidth=" + screenWidth +
	  ", screenHeight=" + screenHeight +
	  ", assemblyId=" + assemblyId +
	  ", start=" + start +
	  ", fixedLength=" + fixedLength +
	  ", marketId='" + marketId + '\'' +
	  ", installList=" + installList +
	  ", uninstallList=" + uninstallList +
	  '}';
  }
}
