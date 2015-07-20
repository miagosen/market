package com.lx.market.model;

import java.io.Serializable;

/**
 * Created by Antikvo.Miao on 2014/9/15.
 */
public class UninstallAppInfo implements Serializable {
  private static final long serialVersionUID = -4437792055557593864L;
  private int id;
  private String packageName;
  private long uninstallTime;

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

  public long getUninstallTime () {
	return uninstallTime;
  }

  public void setUninstallTime (long uninstallTime) {
	this.uninstallTime = uninstallTime;
  }

  @Override
  public String toString () {
	return "UninstallAppInfo{" +
	  "packageName='" + packageName + '\'' +
	  ", uninstallTime=" + uninstallTime +
	  '}';
  }
}
