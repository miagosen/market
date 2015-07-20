package com.lx.market.network.serializer;

import com.lx.market.network.protocol.GetServerReq;
import com.lx.market.network.protocol.GetServerResp;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class MessageRecognizer {

  private static Map<Integer, Class> m_MessageClasses = new HashMap<Integer, Class>();

  static {
	addClass(GetServerReq.class);
	addClass(GetServerResp.class);
  }

  public static Class getClassByCode (int code) {
	if (m_MessageClasses.containsKey(code)) {
	  return m_MessageClasses.get(code);
	}
	return null;
  }

  public static boolean addClass (Class cls) {
	SignalCode sc = AttributeUitl.getMessageAttribute(cls);
	if (sc != null) {
	  if (!m_MessageClasses.containsKey(sc.messageCode())) {
		m_MessageClasses.put(sc.messageCode(), cls);
		return true;
	  }
	}
	return false;
  }
}
