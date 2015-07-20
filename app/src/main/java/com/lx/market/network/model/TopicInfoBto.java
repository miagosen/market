package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TopicInfoBto {

  @Expose
  @SerializedName("topicId")
  private String topicId;//栏目ID=marketId@pindaoId@topicId

  @Expose
  @SerializedName("topicName")
  private String topicName;//栏目名称

  @Expose
  @SerializedName("imgUrl")
  private String imgUrl;//栏目图片URL

  @Expose
  @SerializedName("assLst")
  private List<AssemblyInfoBto> assemblyList;//组件列表
  @Expose
  @SerializedName("topicType")
  private int topicType;//栏目类型 1-普通栏目，  2-	我喜欢栏目

  public int getTopicType () {
	return topicType;
  }

  public void setTopicType (int topicType) {
	this.topicType = topicType;
  }

  public String getTopicId () {
	return topicId;
  }

  public void setTopicId (String topicId) {
	this.topicId = topicId;
  }

  public String getTopicName () {
	return topicName;
  }

  public void setTopicName (String topicName) {
	this.topicName = topicName;
  }

  public String getImgUrl () {
	return imgUrl;
  }

  public void setImgUrl (String imgUrl) {
	this.imgUrl = imgUrl;
  }

  public List<AssemblyInfoBto> getAssemblyList () {
	return assemblyList;
  }

  public void setAssemblyList (List<AssemblyInfoBto> assemblyList) {
	this.assemblyList = assemblyList;
  }

  public void addAssemblyInfo (AssemblyInfoBto assemblyInfo) {
	if (assemblyList == null) {
	  assemblyList = new ArrayList<AssemblyInfoBto>();
	}
	assemblyList.add(assemblyInfo);
  }

  @Override
  public String toString () {
	return "TopicInfoBto{" +
	  "topicId=" + topicId +
	  ", topicName='" + topicName + '\'' +
	  ", imgUrl='" + imgUrl + '\'' +
	  ", assemblyList=" + assemblyList +
	  '}';
  }
}
