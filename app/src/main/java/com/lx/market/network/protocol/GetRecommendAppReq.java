package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.TerminalInfo;
import com.lx.market.network.serializer.SignalCode;

@SignalCode(messageCode = 101003)
public class GetRecommendAppReq {

  @Expose
  @SerializedName("tInfo")
  private TerminalInfo terminalInfo;

  @Expose
  @SerializedName("resId")
  private int resId;

  @Expose
  @SerializedName("label")
  private String label;

  public TerminalInfo getTerminalInfo () {
	return terminalInfo;
  }

  public void setTerminalInfo (TerminalInfo terminalInfo) {
	this.terminalInfo = terminalInfo;
  }

  public int getResId () {
	return resId;
  }

  public void setResId (int resId) {
	this.resId = resId;
  }

  public String getLabel () {
	return label;
  }

  public void setLabel (String label) {
	this.label = label;
  }

  @Override
  public String toString () {
	return "GetRecommendAppReq{" +
	  "terminalInfo=" + terminalInfo +
	  ", resId=" + resId +
	  ", label='" + label + '\'' +
	  '}';
  }
}
