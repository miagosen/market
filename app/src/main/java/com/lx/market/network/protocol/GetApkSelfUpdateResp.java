package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.serializer.OcResponseResultCode;
import com.lx.market.network.serializer.SignalCode;

/**
 * Created by Antikvo.Miao on 2014/8/7.
 */
@SignalCode(messageCode = 203001)
public class GetApkSelfUpdateResp extends OcResponseResultCode {

  @Expose
  @SerializedName("title")
  private String title;
  @Expose
  @SerializedName("content")
  private String content;
  @Expose
  @SerializedName("policy")
  private int policy;
  @Expose
  @SerializedName("pName")
  private String pName;
  @Expose
  @SerializedName("ver")
  private int ver;
  @Expose
  @SerializedName("fileUrl")
  private String fileUrl;
  @Expose
  @SerializedName("md5")
  private String md5;

  public String getTitle () {
	return title;
  }

  public void setTitle (String title) {
	this.title = title;
  }

  public String getContent () {
	return content;
  }

  public void setContent (String content) {
	this.content = content;
  }

  public int getPolicy () {
	return policy;
  }

  public void setPolicy (int policy) {
	this.policy = policy;
  }

  public String getpName () {
	return pName;
  }

  public void setpName (String pName) {
	this.pName = pName;
  }

  public int getVer () {
	return ver;
  }

  public void setVer (int ver) {
	this.ver = ver;
  }

  public String getFileUrl () {
	return fileUrl;
  }

  public void setFileUrl (String fileUrl) {
	this.fileUrl = fileUrl;
  }

  public String getMd5 () {
	return md5;
  }

  public void setMd5 (String md5) {
	this.md5 = md5;
  }

  @Override
  public String toString () {
	return "GetApkSelfUpdateResp{" +
	  "title='" + title + '\'' +
	  ", content='" + content + '\'' +
	  ", policy=" + policy +
	  ", pName='" + pName + '\'' +
	  ", ver=" + ver +
	  ", fileUrl='" + fileUrl + '\'' +
	  ", md5='" + md5 + '\'' +
	  "} " + super.toString();
  }
}
