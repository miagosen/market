package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.model.TextImageInfo;
import com.lx.market.network.serializer.OcResponseResultCode;

import java.util.List;

/**
 * Created by Antikvo.Miao on 2014/8/7.
 */
public class GetRecommondPageResp extends OcResponseResultCode {

  @Expose
  @SerializedName("isPop")
  private int isPop;//是否弹出：1-是，0-否

  @Expose
  @SerializedName("appLst")
  private List<AppInfoBto> appInfoList;

  @Expose
  @SerializedName("textInfos")
  private List<TextImageInfo> textImageInfoList;

  public int getIsPop () {
	return isPop;
  }

  public void setIsPop (int isPop) {
	this.isPop = isPop;
  }

  public List<AppInfoBto> getAppInfoList () {
	return appInfoList;
  }

  public void setAppInfoList (List<AppInfoBto> appInfoList) {
	this.appInfoList = appInfoList;
  }

  public List<TextImageInfo> getTextImageInfoList () {
	return textImageInfoList;
  }

  public void setTextImageInfoList (List<TextImageInfo> textImageInfoList) {
	this.textImageInfoList = textImageInfoList;
  }

  @Override
  public String toString () {
	return "GetRecommondPageResp{" +
	  "isPop=" + isPop +
	  ", appInfoList=" + appInfoList +
	  ", textImageInfoList=" + textImageInfoList +
	  '}';
  }
}
