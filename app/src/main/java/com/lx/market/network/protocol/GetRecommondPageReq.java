package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.PackageInfoBto;
import com.lx.market.network.model.TerminalInfo;
import com.lx.market.network.serializer.SignalCode;

import java.util.List;

/**
 * Created by Antikvo.Miao on 2014/8/7.
 */
@SignalCode(messageCode = 101101, compress = true)
public class GetRecommondPageReq {
  @Expose
  @SerializedName("tInfo")
  private TerminalInfo terminalInfo;

  @Expose
  @SerializedName("marketId")
  private String marketId;

  @Expose
  @SerializedName("installs")
  private List<PackageInfoBto> installList;

  @Expose
  @SerializedName("uninstalls")
  private List<PackageInfoBto> uninstallList;

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

  @Override
  public String toString () {
	return "GetRecommondPageReq{" +
	  "terminalInfo=" + terminalInfo +
	  ", marketId='" + marketId + '\'' +
	  ", installList=" + installList +
	  ", uninstallList=" + uninstallList +
	  '}';
  }
}
