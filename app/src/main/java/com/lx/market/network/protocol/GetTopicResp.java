package com.lx.market.network.protocol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lx.market.network.model.AssemblyInfoBto;
import com.lx.market.network.serializer.OcResponseResultCode;
import com.lx.market.network.serializer.SignalCode;

import java.util.ArrayList;
import java.util.List;

@SignalCode(messageCode = 201012, compress = true)
public class GetTopicResp extends OcResponseResultCode {

  @Expose
  @SerializedName("assLst")
  private List<AssemblyInfoBto> assemblyList;

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
	return "GetTopicResp{" +
	  "assemblyList=" + assemblyList +
	  '}';
  }
}
