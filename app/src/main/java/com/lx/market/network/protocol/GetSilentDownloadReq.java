package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.PackageInfoBto;
import com.lx.market.network.model.TerminalInfo;
import com.lx.market.network.serializer.SignalCode;

import java.util.ArrayList;
import java.util.List;

@SignalCode(messageCode = 101014)
public class GetSilentDownloadReq {

  @Expose
  @SerializedName("tInfo")
  private TerminalInfo terminalInfo;

  @Expose
  @SerializedName("marketId")
  private String marketId;

  @Expose
  @SerializedName("version")
  private int silentVersion;

  /**
   * 已安装列表
   */
  @Expose
  @SerializedName("installs")
  private List<PackageInfoBto> installList = new ArrayList<PackageInfoBto>();

  /**
   * 已卸载列表
   */
  @Expose
  @SerializedName("uninstalls")
  private List<PackageInfoBto> unInstallList = new ArrayList<PackageInfoBto>();

  public TerminalInfo getTerminalInfo () {
	return terminalInfo;
  }

  public void setTerminalInfo (TerminalInfo terminalInfo) {
	this.terminalInfo = terminalInfo;
  }

  public String getMarketId () {
	return marketId;
  }

  public void setMarketId (String marketId) {
	this.marketId = marketId;
  }

  public int getSilentVersion () {
	return silentVersion;
  }

  public void setSilentVersion (int silentVersion) {
	this.silentVersion = silentVersion;
  }

  public List<PackageInfoBto> getInstallList () {
	return installList;
  }

  @Override
  public String toString () {
	return "GetSilentDownloadReq{" +
	  "terminalInfo=" + terminalInfo +
	  ", marketId='" + marketId + '\'' +
	  ", silentVersion=" + silentVersion +
	  ", installList=" + installList +
	  ", unInstallList=" + unInstallList +
	  '}';
  }

  public void setInstallList (List<PackageInfoBto> installList) {
	this.installList = installList;
  }

  public List<PackageInfoBto> getUnInstallList () {
	return unInstallList;
  }

  public void setUnInstallList (List<PackageInfoBto> unInstallList) {
	this.unInstallList = unInstallList;
  }
}
