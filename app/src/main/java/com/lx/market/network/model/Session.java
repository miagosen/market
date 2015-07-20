package com.lx.market.network.model;

import android.text.TextUtils;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

@Table(name = "Session_Table")
public final class Session implements Serializable {
  @Id
  private int id;
  private static final long serialVersionUID = 1044237039063420007L;
  // 市场数据网络地址
  @Column(column = "marketNetworkAddr")
  private String marketNetworkAddr = null;
  // 数据统计网络地址
  @Column(column = "statisNetworkAddr")
  private String statisNetworkAddr = null;
  // 更新服务器网络地址
  @Column(column = "updateNetworkAddr")
  private String updateNetworkAddr = null;

  private long time;

  public Session () {
	time = System.currentTimeMillis();
  }

  public int getId () {
	return id;
  }

  public void setId (int id) {
	this.id = id;
  }

  public String appendHttp (String serverAddress) {
	String addr;
	if (serverAddress != null && serverAddress.toLowerCase().startsWith("http://")) {
	  addr = serverAddress;
	} else {
	  addr = "http://" + serverAddress;
	}
	return addr;
  }

  public String getMarketNetworkAddr () {
	return marketNetworkAddr;
  }

  public void setMarketNetworkAddr (String marketNetworkAddr) {
	this.marketNetworkAddr = appendHttp(marketNetworkAddr);
  }

  public String getStatisNetworkAddr () {
	return statisNetworkAddr;
  }

  public void setStatisNetworkAddr (String statisNetworkAddr) {
	this.statisNetworkAddr = appendHttp(statisNetworkAddr);
  }

  public String getUpdateNetworkAddr () {
	return updateNetworkAddr;
  }

  public void setUpdateNetworkAddr (String updateNetworkAddr) {
	this.updateNetworkAddr = appendHttp(updateNetworkAddr);
  }

  public String getServerAddress (RequestType type) {
	String addr = "";
	switch (type) {
	  case MARKET_DATA:
		addr = marketNetworkAddr;
		break;
	  case UPDATE:
		addr = updateNetworkAddr;
		break;
	  case STATISTICS:
		addr = statisNetworkAddr;
		break;
	}
	return addr;
  }

  public long getTime () {
	return time;
  }

  public void setTime (long time) {
	this.time = time;
  }

  @Override
  public String toString () {
	return "Session{" +
	  "id=" + id +
	  ", marketNetworkAddr='" + marketNetworkAddr + '\'' +
	  ", statisNetworkAddr='" + statisNetworkAddr + '\'' +
	  ", updateNetworkAddr='" + updateNetworkAddr + '\'' +
	  ", time=" + time +
	  '}';
  }

  public boolean isEmpty () {
	if (TextUtils.isEmpty(marketNetworkAddr) || TextUtils.isEmpty(statisNetworkAddr) || TextUtils.isEmpty(updateNetworkAddr)) {
	  return true;
	}
	return false;
  }
}
