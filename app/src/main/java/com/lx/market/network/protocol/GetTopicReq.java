package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.PackageInfoBto;
import com.lx.market.network.model.TerminalInfo;
import com.lx.market.network.serializer.SignalCode;

import java.util.ArrayList;
import java.util.List;

@SignalCode(messageCode = 101012)
public class GetTopicReq {

  @Expose
  @SerializedName("tInfo")
  private TerminalInfo terminalInfo = new TerminalInfo();

  @Expose
  @SerializedName("topicId")
  private String topicId;

  @Expose
  @SerializedName("start")
  private int start;

  @Expose
  @SerializedName("fixed")
  private int fixedLength;
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

  public String getTopicId () {
	return topicId;
  }

  public void setTopicId (String topicId) {
	this.topicId = topicId;
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

  public List<PackageInfoBto> getInstallList () {
	return installList;
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

  @Override
  public String toString () {
	return "GetTopicReq{" +
	  "terminalInfo=" + terminalInfo +
	  ", topicId=" + topicId +
	  ", start=" + start +
	  ", fixedLength=" + fixedLength +
	  ", installList=" + installList +
	  ", unInstallList=" + unInstallList +
	  '}';
  }
}
