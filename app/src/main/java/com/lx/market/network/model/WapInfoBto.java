package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.model.BaseEntity;

/**
 * Created by Antikvo.Miao on 2014/9/19.
 */
public class WapInfoBto extends BaseEntity {

  @Expose
  @SerializedName("url")
  private String url;
  @Expose
  @SerializedName("name")
  private String name;

  public String getUrl () {
	return url;
  }

  public void setUrl (String url) {
	this.url = url;
  }

  public String getName () {
	return name;
  }

  public void setName (String name) {
	this.name = name;
  }

  @Override
  public String toString () {
	return "WapInfoBto{" +
	  "url='" + url + '\'' +
	  ", name='" + name + '\'' +
	  "} " + super.toString();
  }
}
