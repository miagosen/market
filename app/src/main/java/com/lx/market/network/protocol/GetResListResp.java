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
public class GetResListResp extends OcResponseResultCode {

  @Expose
  @SerializedName("appLst")
  private List<AppInfoBto> appInfoList;

  @Expose
  @SerializedName("textInfos")
  private List<TextImageInfo> textImageInfoList;

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
	return "GetResListResp{" +
	  "appInfoList=" + appInfoList +
	  ", textImageInfoList=" + textImageInfoList +
	  '}';
  }
}
