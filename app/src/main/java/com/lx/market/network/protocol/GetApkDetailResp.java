package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.AppDetailInfoBto;
import com.lx.market.network.serializer.OcResponseResultCode;
import com.lx.market.network.serializer.SignalCode;

@SignalCode(messageCode = 201013, compress = true)
public class GetApkDetailResp extends OcResponseResultCode {

  @Expose
  @SerializedName("detailInfo")
  private AppDetailInfoBto appDetailInfo;

  public AppDetailInfoBto getAppDetailInfo() {
    return appDetailInfo;
  }

  public void setAppDetailInfo(AppDetailInfoBto appDetailInfo) {
    this.appDetailInfo = appDetailInfo;
  }

  @Override
  public String toString() {
    return "GetApkDetailResp{" + "appDetailInfo=" + appDetailInfo + '}';
  }
}