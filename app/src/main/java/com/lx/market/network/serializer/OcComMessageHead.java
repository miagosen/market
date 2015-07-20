package com.lx.market.network.serializer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Antikvo.Miao on 2014/7/31.
 */
public class OcComMessageHead {
  @Expose
  @SerializedName("ver")
  private byte version;
  @Expose
  @SerializedName("type")
  private byte type;
  @Expose
  @SerializedName("msb")
  private long mostSignificantBits;
  @Expose
  @SerializedName("lsb")
  private long leastSignificantBits;
  @Expose
  @SerializedName("mcd")
  private int messageCode;

  public byte getVersion () {
	return version;
  }

  public void setVersion (byte version) {
	this.version = version;
  }

  public byte getType () {
	return type;
  }

  public void setType (byte type) {
	this.type = type;
  }

  public long getMostSignificantBits () {
	return mostSignificantBits;
  }

  public void setMostSignificantBits (long mostSignificantBits) {
	this.mostSignificantBits = mostSignificantBits;
  }

  public long getLeastSignificantBits () {
	return leastSignificantBits;
  }

  public void setLeastSignificantBits (long leastSignificantBits) {
	this.leastSignificantBits = leastSignificantBits;
  }

  public int getMessageCode () {
	return messageCode;
  }

  public void setMessageCode (int messageCode) {
	this.messageCode = messageCode;
  }
}
