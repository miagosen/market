package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.TerminalInfo;
import com.lx.market.network.serializer.SignalCode;

/**
 * Created by Antikvo.Miao on 2014/7/31.
 */
@SignalCode(messageCode = 100001)
public class GetServerReq {

  @Expose
  @SerializedName("tInfo")
  private TerminalInfo terminalInfo = new TerminalInfo();
  @Expose
  @SerializedName("s")
  private String source;

  public TerminalInfo getTerminalInfo () {
	return terminalInfo;
  }

  public void setTerminalInfo (TerminalInfo terminalInfo) {
	this.terminalInfo = terminalInfo;
  }

  public String getSource () {
	return source;
  }

  public void setSource (String source) {
	this.source = source;
  }

  @Override
  public String toString () {
	return "GetServerReq{" +
	  "terminalInfo=" + terminalInfo +
	  ", source='" + source + '\'' +
	  '}';
  }
}
