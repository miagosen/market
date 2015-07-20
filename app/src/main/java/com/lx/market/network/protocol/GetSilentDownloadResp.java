package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.SilentConfigBto;
import com.lx.market.network.serializer.OcResponseResultCode;
import com.lx.market.network.serializer.SignalCode;

@SignalCode(messageCode = 201014, compress = true)
public class GetSilentDownloadResp extends OcResponseResultCode {

  @Expose
  @SerializedName("config")
  private SilentConfigBto silentConfigBto = new SilentConfigBto();

  public SilentConfigBto getSilentConfigBto () {
	return silentConfigBto;
  }

  public void setSilentConfigBto (SilentConfigBto silentConfigBto) {
	this.silentConfigBto = silentConfigBto;
  }

  @Override
  public String toString () {
	return "GetSilentDownloadResp{" +
	  "silentConfigBto=" + silentConfigBto +
	  '}';
  }
}
