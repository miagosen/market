package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.ChannelInfoBto;
import com.lx.market.network.serializer.OcResponseResultCode;
import com.lx.market.network.serializer.SignalCode;

import java.util.ArrayList;
import java.util.List;

@SignalCode(messageCode = 201010, compress = true)
public class GetMarketFrameResp extends OcResponseResultCode {

  private static final long serialVersionUID = 5443814419299665006L;
  @Expose
  @SerializedName("marketId")
  private String marketId;

  @Expose
  @SerializedName("chLst")
  private List<ChannelInfoBto> channelList = new ArrayList<ChannelInfoBto>();

  public String getMarketId () {
	return marketId;
  }

  public void setMarketId (String marketId) {
	this.marketId = marketId;
  }

  public List<ChannelInfoBto> getChannelList () {
	return channelList;
  }

  public void setChannelList (List<ChannelInfoBto> channelList) {
	this.channelList = channelList;
  }

  public void addChannelInfo (ChannelInfoBto channelInfo) {
	channelList.add(channelInfo);
  }

  @Override
  public String toString () {
	return "GetMarketFrameResp{" +
	  "marketId='" + marketId + '\'' +
	  ", channelList=" + channelList +
	  "} " + super.toString();
  }
}
