package com.lx.market.network.serializer;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lx.market.network.utils.DESUtil;
import com.lx.market.utils.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Antikvo.Miao on 2014/8/1.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class MessageCodec {
  private static byte[] m_key = "_x22_x22".getBytes();
  ;
  private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
  ;

  /**
   * 编码包
   *
   * @param msg
   * @return
   * @throws Exception
   */
  public static byte[] serializeMessage (OcHttpMessage msg) throws Exception {
	try {
	  if (msg.body == null) {
		throw new Exception("message body is null");
	  }
	  SignalCode attrib = AttributeUitl.getMessageAttribute(msg.body);
	  msg.setHeadJson(gson.toJson(msg.head));
	  msg.setBodyJson(gson.toJson(msg.body));
	  String jsonContent = gson.toJson(msg);
	  //需要压缩
	  if (attrib != null && attrib.compress()) {
		jsonContent = compress(jsonContent, "utf-8");
	  }
	  byte[] messageArray = jsonContent.getBytes("utf-8");
	  //需要加密
	  if (attrib != null && attrib.encrypt()) {
		messageArray = DESUtil.encrypt(messageArray, m_key);
	  }
	  return messageArray;
	} catch (Exception e) {
	  throw new Exception("serialize message " + msg.body.getClass() + " fail:" + e.getLocalizedMessage());
	}
  }

  /**
   * 字符串的压缩
   *
   * @param str 待压缩的字符串
   * @return 返回压缩后的字符串
   * @throws IOException
   */
  public static String compress (String str, String charSet) throws IOException {
	if (null == str || str.length() <= 0) {
	  return str;
	}
	// 创建一个新的 byte 数组输出流
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	// 使用默认缓冲区大小创建新的输出流
	GZIPOutputStream gzip = new GZIPOutputStream(out);
	// 将 b.length 个字节写入此输出流
	gzip.write(str.getBytes());
	gzip.close();
	return out.toString(charSet);
  }

  /**
   * 字符串的解压
   *
   * @param str 对字符串解压
   * @return 返回解压缩后的字符串
   * @throws IOException
   */
  public static byte[] unCompress (byte[] str) throws IOException {
	if (null == str || str.length <= 0) {
	  return str;
	}
	// 创建一个新的 byte 数组输出流
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	// 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
	ByteArrayInputStream in = new ByteArrayInputStream(str);
	// 使用默认缓冲区大小创建新的输入流
	GZIPInputStream gzip = new GZIPInputStream(in);
	byte[] buffer = new byte[256];
	int n = 0;
	while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
	  // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
	  out.write(buffer, 0, n);
	}
	// 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
	return out.toByteArray();
  }

  /**
   * 解码包头
   *
   * @throws Exception
   */
  public synchronized static OcComMessageHead deserializeHead (String headJson) throws Exception {
	OcComMessageHead head = null;
	try {
	  if (TextUtils.isEmpty(headJson)) {
		throw new Exception("head length error");
	  }
	  head = gson.fromJson(headJson, OcComMessageHead.class);
	} catch (Exception e) {
	  throw new Exception("deserialize head fail:" + e.getLocalizedMessage());
	}
	return head;
  }

  /**
   * 解码包体
   *
   * @throws Exception
   */
  public static Object deserializeBody (String bodyJson, Class<? extends OcResponseResultCode> respClass) throws Exception {
	return gson.fromJson(bodyJson, respClass);
  }

  public synchronized static byte[] decryptResponse (byte[] respContent) {
	try {
	  byte[] result = DESUtil.decrypt(respContent, m_key);
	  return result;
	} catch (Exception e) {
	  Logger.p(e);
	}
	return null;
  }

  public static OcHttpMessage deserializeMessage (byte[] respJsonContent, boolean isCompress) {
	OcHttpMessage respMessage = null;
	try {
	  if (isCompress) {
		respJsonContent = unCompress(respJsonContent);
	  }
	  respMessage = gson.fromJson(new String(respJsonContent).trim(), OcHttpMessage.class);
	} catch (Exception e) {
	  Logger.p(e);
	}
	return respMessage;
  }
}
