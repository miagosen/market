package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.TerminalInfo;
import com.lx.market.network.serializer.SignalCode;

@SignalCode(messageCode = 101002)
public class GetStartPageReq {

  @Expose
  @SerializedName("tInfo")
  private TerminalInfo terminalInfo = new TerminalInfo();

  public TerminalInfo getTerminalInfo () {
	return terminalInfo;
  }

  public void setTerminalInfo (TerminalInfo terminalInfo) {
	this.terminalInfo = terminalInfo;
  }

  @Override
  public String toString () {
	return "GetStartPageReq{" +
	  "terminalInfo=" + terminalInfo +
	  '}';
  }
}
