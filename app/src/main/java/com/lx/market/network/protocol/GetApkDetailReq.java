package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.TerminalInfo;
import com.lx.market.network.serializer.SignalCode;

@SignalCode(messageCode = 101013)
public class GetApkDetailReq {

  @Expose
  @SerializedName("tInfo")
  private TerminalInfo terminalInfo;

  @Expose
  @SerializedName("resId")
  private int          appid;

  public int getAppid() {
    return appid;
  }

  public void setAppid(int appid) {
    this.appid = appid;
  }

  public TerminalInfo getTerminalInfo() {
    return terminalInfo;
  }

  public void setTerminalInfo(TerminalInfo terminalInfo) {
    this.terminalInfo = terminalInfo;
  }

  @Override
  public String toString() {
    return "GetApkDetailReq{" + "terminalInfo=" + terminalInfo + ", appid=" + appid + '}';
  }
}