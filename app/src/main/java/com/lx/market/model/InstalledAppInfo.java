package com.lx.market.model;

import java.io.Serializable;

/**
 * Created by Antikvo.Miao on 2014/9/15.
 */
public class InstalledAppInfo implements Serializable {
  private static final long serialVersionUID = -491354343581067502L;
  private int id;
  private String packageName;
  private long installTime;

  public int getId () {
	return id;
  }

  public void setId (int id) {
	this.id = id;
  }

  public String getPackageName () {
	return packageName;
  }

  public void setPackageName (String packageName) {
	this.packageName = packageName;
  }

  public long getInstallTime () {
	return installTime;
  }

  public void setInstallTime (long installTime) {
	this.installTime = installTime;
  }
}
