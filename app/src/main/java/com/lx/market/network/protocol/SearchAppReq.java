package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.TerminalInfo;
import com.lx.market.network.serializer.SignalCode;

@SignalCode(messageCode = 101009)
public class SearchAppReq  {

  @Expose
  @SerializedName("tInfo")
  private TerminalInfo terminalInfo;

  @Expose
  @SerializedName("key")
  private String          keyword;

  @Expose
  @SerializedName("start")
  private int             start;

  @Expose
  @SerializedName("pSize")
  private int             pageSize;

  public TerminalInfo getTerminalInfo() {
    return terminalInfo;
  }

  public void setTerminalInfo(TerminalInfo terminalInfo) {
    this.terminalInfo = terminalInfo;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

}
