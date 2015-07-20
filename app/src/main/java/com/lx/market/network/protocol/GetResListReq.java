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

@SignalCode(messageCode = 101100)
public class GetResListReq {
  @Expose
  @SerializedName("tInfo")
  private TerminalInfo terminalInfo;
  @Expose
  @SerializedName("installList")
  private List<PackageInfoBto> installList;
  @Expose
  @SerializedName("uninstallList")
  private List<PackageInfoBto> uninstallList;

  @Expose
  @SerializedName("marketId")
  private String marketId;
  @Expose
  @SerializedName("resType")
  private int resType;//资源类型：  1-游戏精灵列表；	2-我的游戏列表

  @Expose
  @SerializedName("start")
  private int start;//索引位置

  public TerminalInfo getTerminalInfo () {
	return terminalInfo;
  }

  public void setTerminalInfo (TerminalInfo terminalInfo) {
	this.terminalInfo = terminalInfo;
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

  public String getMarketId () {
	return marketId;
  }

  public void setMarketId (String marketId) {
	this.marketId = marketId;
  }

  public int getResType () {
	return resType;
  }

  public void setResType (int resType) {
	this.resType = resType;
  }

  public int getStart () {
	return start;
  }

  public void setStart (int start) {
	this.start = start;
  }

  @Override
  public String toString () {
	return "GetResListReq{" +
	  "terminalInfo=" + terminalInfo +
	  ", installList=" + installList +
	  ", uninstallList=" + uninstallList +
	  ", marketId='" + marketId + '\'' +
	  ", resType=" + resType +
	  ", start=" + start +
	  '}';
  }
}
