package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeyWordInfoBto {

  @Expose
  @SerializedName("key")
  private String key;
  @Expose
  @SerializedName("assemblyId")
  private String assemblyId;//组件ID，非0表示点击分类，0表示关键字搜索

  public String getKey () {
	return key;
  }

  public void setKey (String key) {
	this.key = key;
  }

  public String getAssemblyId () {
	return assemblyId;
  }

  public void setAssemblyId (String assemblyId) {
	this.assemblyId = assemblyId;
  }

    @Override
    public String toString() {
        return "KeyWordInfoBto{" +
                "key='" + key + '\'' +
                ", assemblyId='" + assemblyId + '\'' +
                '}';
    }
}
