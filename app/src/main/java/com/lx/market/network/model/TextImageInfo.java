package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Antikvo.Miao on 2014/8/7.
 */
public class TextImageInfo {
  @Expose
  @SerializedName("tittle")
  private String tittle;
  @Expose
  @SerializedName("textContent")
  private String textContent;
  @Expose
  @SerializedName("imgUrl")
  private String imgUrl;
  @Expose
  @SerializedName("orderNum")
  private String orderNum;//排序

  public String getTittle () {
	return tittle;
  }

  public void setTittle (String tittle) {
	this.tittle = tittle;
  }

  public String getTextContent () {
	return textContent;
  }

  public void setTextContent (String textContent) {
	this.textContent = textContent;
  }

  public String getImgUrl () {
	return imgUrl;
  }

  public void setImgUrl (String imgUrl) {
	this.imgUrl = imgUrl;
  }

  public String getOrderNum () {
	return orderNum;
  }

  public void setOrderNum (String orderNum) {
	this.orderNum = orderNum;
  }

  @Override
  public String toString () {
	return "TextImageInfo{" +
	  "tittle='" + tittle + '\'' +
	  ", textContent='" + textContent + '\'' +
	  ", imgUrl='" + imgUrl + '\'' +
	  ", orderNum='" + orderNum + '\'' +
	  '}';
  }
}
