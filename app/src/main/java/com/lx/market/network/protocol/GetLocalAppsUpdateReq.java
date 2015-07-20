package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.AppSnapshotBto;
import com.lx.market.network.model.TerminalInfo;
import com.lx.market.network.serializer.SignalCode;

import java.util.List;

@SignalCode(messageCode = 101004)
public class GetLocalAppsUpdateReq {

  @Expose
  @SerializedName("tInfo")
  private TerminalInfo terminalInfo;

  @Expose
  @SerializedName("appLst")
  private List<AppSnapshotBto> appList;

  public TerminalInfo getTerminalInfo () {
	return terminalInfo;
  }

  public void setTerminalInfo (TerminalInfo terminalInfo) {
	this.terminalInfo = terminalInfo;
  }

  public List<AppSnapshotBto> getAppList () {
	return appList;
  }

  public void setAppList (List<AppSnapshotBto> appList) {
	this.appList = appList;
  }

  @Override
  public String toString () {
	return "GetAppsUpdateReq{" +
	  "terminalInfo=" + terminalInfo +
	  ", appList=" + appList +
	  '}';
  }
}
