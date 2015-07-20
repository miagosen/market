package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * app包信息
 */
public class PackageInfoBto {

  @Expose
  @SerializedName("pName")
  private String packageName;

  public String getPackageName () {
	return packageName;
  }

  public void setPackageName (String packageName) {
	this.packageName = packageName;
  }

  @Override
  public String toString () {
	return "PackageInfoBto{" +
	  "packageName='" + packageName + '\'' +
	  '}';
  }
}
