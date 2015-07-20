package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.serializer.OcResponseResultCode;
import com.lx.market.network.serializer.SignalCode;

import java.util.ArrayList;
import java.util.List;

@SignalCode(messageCode = 201003, compress = true)
public class GetRecommendAppResp extends OcResponseResultCode {

  @Expose
  @SerializedName("appLst")
  private List<AppInfoBto> appList = new ArrayList<AppInfoBto>();

  public List<AppInfoBto> getAppList () {
	return appList;
  }

  public void setAppList (List<AppInfoBto> appList) {
	this.appList = appList;
  }

  public void addAppInfo (AppInfoBto appInfo) {
	this.appList.add(appInfo);
  }

  @Override
  public String toString () {
	return "GetRecommendAppResp{" +
	  "appList=" + appList +
	  '}';
  }
}
