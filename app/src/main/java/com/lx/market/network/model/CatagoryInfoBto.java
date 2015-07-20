package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Antikvo.Miao on 2014/8/7.
 */
public class CatagoryInfoBto {

  @Expose
  @SerializedName("cName")
  private String catagoryName;//分类名称
  @Expose
  @SerializedName("cType")
  private String catagoryType;//类型：1-男，2-女，3-其它
  @Expose
  @SerializedName("orderNum")
  private String orderNume;//排序
  @Expose
  @SerializedName("isDefault")
  private String isDefault;//是否默认的分类：1-是，0-否
  @Expose
  @SerializedName("assId")
  private int assId;//组件ID

  public String getCatagoryName () {
	return catagoryName;
  }

  public void setCatagoryName (String catagoryName) {
	this.catagoryName = catagoryName;
  }

  public String getCatagoryType () {
	return catagoryType;
  }

  public void setCatagoryType (String catagoryType) {
	this.catagoryType = catagoryType;
  }

  public String getOrderNume () {
	return orderNume;
  }

  public void setOrderNume (String orderNume) {
	this.orderNume = orderNume;
  }

  public String getIsDefault () {
	return isDefault;
  }

  public void setIsDefault (String isDefault) {
	this.isDefault = isDefault;
  }

  public int getAssId () {
	return assId;
  }

  public void setAssId (int assId) {
	this.assId = assId;
  }

  @Override
  public String toString () {
	return "CatagoryInfoBto{" +
	  "catagoryName='" + catagoryName + '\'' +
	  ", catagoryType='" + catagoryType + '\'' +
	  ", orderNume='" + orderNume + '\'' +
	  ", isDefault='" + isDefault + '\'' +
	  ", assId=" + assId +
	  '}';
  }
}
