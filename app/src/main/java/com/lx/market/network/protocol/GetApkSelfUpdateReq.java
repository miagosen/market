package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.TerminalInfo;
import com.lx.market.network.serializer.SignalCode;

/**
 * Created by Antikvo.Miao on 2014/8/7.
 */

@SignalCode(messageCode = 103001)
public class GetApkSelfUpdateReq {
  @Expose
  @SerializedName("tInfo")
  private TerminalInfo terminalInfo;

  public TerminalInfo getTerminalInfo () {
	return terminalInfo;
  }

  public void setTerminalInfo (TerminalInfo terminalInfo) {
	this.terminalInfo = terminalInfo;
  }

  @Override
  public String toString () {
	return "GetApkSelfUpdateReq{" +
	  "terminalInfo=" + terminalInfo +
	  '}';
  }
}
