package com.lx.market.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChannelInfoBto {

  @Expose
  @SerializedName("chnId")
  private String channelId;//频道ID

  @Expose
  @SerializedName("name")
  private String channelName;//频道名称

  @Expose
  @SerializedName("topicLst")
  private List<TopicInfoBto> topicList = new ArrayList<TopicInfoBto>();//栏目列表

  public String getChannelId () {
	return channelId;
  }

  public void setChannelId (String channelId) {
	this.channelId = channelId;
  }

  public String getChannelName () {
	return channelName;
  }

  public void setChannelName (String channelName) {
	this.channelName = channelName;
  }

  public List<TopicInfoBto> getTopicList () {
	return topicList;
  }

  public void setTopicList (List<TopicInfoBto> topicList) {
	this.topicList = topicList;
  }

  public void addTopicInfo (TopicInfoBto topicInfo) {
	topicList.add(topicInfo);
  }

  @Override
  public String toString () {
	return "ChannelInfoBto{" +
	  "channelId='" + channelId + '\'' +
	  ", channelName='" + channelName + '\'' +
	  ", topicList=" + topicList +
	  '}';
  }
}
