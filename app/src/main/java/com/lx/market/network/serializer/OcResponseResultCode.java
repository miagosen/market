package com.lx.market.network.serializer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OcResponseResultCode implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  @Expose
  @SerializedName("errorCode")
  private int errorCode;
  @Expose
  @SerializedName("errorMessage")
  private String errorMessage;

  public int getErrorCode () {
	return errorCode;
  }

  public void setErrorCode (int errorCode) {
	this.errorCode = errorCode;
  }

  public String getErrorMessage () {
	return errorMessage;
  }

  public void setErrorMessage (String errorMessage) {
	this.errorMessage = errorMessage;
  }

  @Override
  public String toString () {
	return "OCCom_ResponseBody [errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
  }
}
