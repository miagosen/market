package com.lx.market.network.protocol;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.serializer.OcResponseResultCode;
import com.lx.market.network.serializer.SignalCode;

@SignalCode(messageCode = 201015, compress = true)
public class GetApkListByPageResp extends OcResponseResultCode {

  @Expose
  @SerializedName("appLst")
  private List<AppInfoBto> appList;

  public List<AppInfoBto> getAppList() {
    return appList;
  }

  public void setAppList(List<AppInfoBto> appList) {
    this.appList = appList;
  }

  @Override
  public String toString() {
    return "GetApkListByPageResp{" + "appList=" + appList + '}';
  }
}
