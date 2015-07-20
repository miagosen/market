package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.model.KeyWordInfoBto;
import com.lx.market.network.serializer.OcResponseResultCode;
import com.lx.market.network.serializer.SignalCode;

import java.util.ArrayList;
import java.util.List;

@SignalCode(messageCode = 201008, compress = true)
public class GetStaticSearchAppResp extends OcResponseResultCode {

  @Expose
  @SerializedName("appLst")
  private List<AppInfoBto> appList;
  @Expose
  @SerializedName("keywords")
  private List<KeyWordInfoBto> keyList;

  public List<AppInfoBto> getAppList () {
	return appList;
  }

  public void setAppList (List<AppInfoBto> appList) {
	this.appList = appList;
  }

  public void addAppInfo (AppInfoBto appInfo) {
	if (appList == null) {
	  appList = new ArrayList<AppInfoBto>();
	}
	appList.add(appInfo);
  }

    public List<KeyWordInfoBto> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<KeyWordInfoBto> keyList) {
        this.keyList = keyList;
    }


    @Override
  public String toString () {
	return "GetStaticSearchAppResp{" +
	  "appList=" + appList +
	  ", keyList=" + keyList +
	  '}';
  }
}
