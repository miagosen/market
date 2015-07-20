package com.lx.market.network.serializer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Antikvo.Miao on 2014/8/5.
 */
public class OcHttpMessage {

  public OcComMessageHead head;

  public Object body;

  @Expose
  @SerializedName("head")
  private String headJson;

  @Expose
  @SerializedName("body")
  private String bodyJson;

  public String getHeadJson () {
	return headJson;
  }

  public String getBodyJson () {
	return bodyJson;
  }

  public void setBodyJson (String bodyJson) {
	this.bodyJson = bodyJson;
  }

  public void setHeadJson (String headJson) {
	this.headJson = headJson;
  }
}
