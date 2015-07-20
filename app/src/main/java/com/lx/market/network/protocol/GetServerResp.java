package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.ServerInfo;
import com.lx.market.network.serializer.OcResponseResultCode;
import com.lx.market.network.serializer.SignalCode;

import java.util.ArrayList;
import java.util.List;

@SignalCode(messageCode = 200001)
public class GetServerResp extends OcResponseResultCode {

  @Expose
  @SerializedName("serverLst")
  private List<ServerInfo> serverList = new ArrayList<ServerInfo>();

  public List<ServerInfo> getServerList () {
	return serverList;
  }

  public void setServerList (List<ServerInfo> serverList) {
	this.serverList = serverList;
  }

  @Override
  public String toString () {
	return "GetServerResp{" +
	  "serverList=" + serverList +
	  '}';
  }
}
