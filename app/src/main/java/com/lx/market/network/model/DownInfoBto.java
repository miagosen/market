package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DownInfoBto {

  @Expose
  @SerializedName("apkName")
  private String apkName;//应用名称

  @Expose
  @SerializedName("pName")
  private String packageName;//应用名称

  @Expose
  @SerializedName("result")
  private int result;//下载状态 3：成功;其它失败(具体有哪些失败，由客户端提供)

  @Expose
  @SerializedName("src")
  private String src;//行为路径，由以下组成  市场ID/频道ID/栏目名称/组件名称  每次保证都是由四个组成，若栏目名称为空，组件名称为空，则传’市场ID/频道ID//’

  @Expose
  @SerializedName("from")
  private String from;//来源区分:  市场内用户下载/市场内静默下载/ 市场外下载

  public String getApkName () {
	return apkName;
  }

  public void setApkName (String apkName) {
	this.apkName = apkName;
  }

  public String getPackageName () {
	return packageName;
  }

  public void setPackageName (String packageName) {
	this.packageName = packageName;
  }

  public int getResult () {
	return result;
  }

  public void setResult (int result) {
	this.result = result;
  }

  public String getSrc () {
	return src;
  }

  public void setSrc (String src) {
	this.src = src;
  }

  public String getFrom () {
	return from;
  }

  public void setFrom (String from) {
	this.from = from;
  }

  @Override
  public String toString () {
	return "DownInfoBto{" +
	  "apkName='" + apkName + '\'' +
	  ", packageName='" + packageName + '\'' +
	  ", result=" + result +
	  ", src='" + src + '\'' +
	  ", from='" + from + '\'' +
	  '}';
  }
}
