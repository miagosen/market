package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerInfo {

  @Expose
  @SerializedName("modId")
  private short moduleId;

  @Expose
  @SerializedName("ip")
  private String ip;

  @Expose
  @SerializedName("port")
  private int port;

  public short getModuleId () {
	return moduleId;
  }

  public void setModuleId (short moduleId) {
	this.moduleId = moduleId;
  }

  public String getIp () {
	return ip;
  }

  public void setIp (String ip) {
	this.ip = ip;
  }

  public int getPort () {
	return port;
  }

  public void setPort (int port) {
	this.port = port;
  }

  @Override
  public String toString () {
	return "ServerInfo{" +
	  "moduleId=" + moduleId +
	  ", ip='" + ip + '\'' +
	  ", port=" + port +
	  '}';
  }
}
