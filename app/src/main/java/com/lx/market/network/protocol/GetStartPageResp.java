package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.serializer.OcResponseResultCode;
import com.lx.market.network.serializer.SignalCode;

@SignalCode(messageCode = 201002)
public class GetStartPageResp extends OcResponseResultCode {

  @Expose
  @SerializedName("txt")
  private String text;

  @Expose
  @SerializedName("imgUrl")
  private String imgUrl;

  public String getText () {
	return text;
  }

  public void setText (String text) {
	this.text = text;
  }

  public String getImgUrl () {
	return imgUrl;
  }

  public void setImgUrl (String imgUrl) {
	this.imgUrl = imgUrl;
  }

  @Override
  public String toString () {
	return "GetStartPageResp{" +
	  "text='" + text + '\'' +
	  ", imgUrl='" + imgUrl + '\'' +
	  '}';
  }
}
