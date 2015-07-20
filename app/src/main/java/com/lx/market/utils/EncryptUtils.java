package com.lx.market.utils;

import java.security.MessageDigest;

public class EncryptUtils {

  private final static byte[] OC_DEVELOP_CRTPT_KEY = {(byte) 0x58, (byte) 0x33, (byte) 0x67, (byte) 0x79, (byte) 0x4d, (byte) 0x6c, (byte) 0x39, (byte) 0x34, (byte) 0x4d, (byte) 0x6a, (byte) 0x49, (byte) 0x3d,
  };                                                                 // _x22_x22
  private final static byte[] OC_RELEASE_CRTPT_KEY = {(byte) 0x58, (byte) 0x7a, (byte) 0x4a, (byte) 0x34, (byte) 0x58, (byte) 0x7a, (byte) 0x4a,
	(byte) 0x73, (byte) 0x55, (byte) 0x6c, (byte) 0x38, (byte) 0x3d,};                                                                 // _2x_2lR_
  private final static byte[] OC_DEVELOP_NETWORK_ADDR = {(byte) 0x62, (byte) 0x32, (byte) 0x4e, (byte) 0x30, (byte) 0x5a, (byte) 0x58, (byte) 0x4e, (byte) 0x30, (byte) 0x4c, (byte) 0x6d, (byte) 0x39, (byte) 0x70, (byte) 0x59, (byte) 0x33, (byte) 0x41, (byte) 0x75, (byte) 0x62, (byte) 0x6d, (byte) 0x56, (byte) 0x30, (byte) 0x4f, (byte) 0x6a, (byte) 0x6b, (byte) 0x77, (byte) 0x4f, (byte) 0x54, (byte) 0x41, (byte) 0x3d,
  }; // octest.oicp.net:9090
  private final static byte[] OC_RELEASE_NETWORK_ADDR = {(byte) 0x62, (byte) 0x32, (byte) 0x4e, (byte) 0x6c, (byte) 0x59, (byte) 0x57, (byte) 0x35,
	(byte) 0x6b, (byte) 0x63, (byte) 0x6d, (byte) 0x56, (byte) 0x68, (byte) 0x62, (byte) 0x53, (byte) 0x35, (byte) 0x76, (byte) 0x63, (byte) 0x6d,
	(byte) 0x63, (byte) 0x36, (byte) 0x4f, (byte) 0x44, (byte) 0x41, (byte) 0x78, (byte) 0x4d, (byte) 0x41, (byte) 0x3d, (byte) 0x3d,}; // oceandream.org:8010
  private final static byte[] OC_RELEASE_NETWORK_SECOND_ADDR = {(byte) 0x63, (byte) 0x48, (byte) 0x56, (byte) 0x7a, (byte) 0x61, (byte) 0x43, (byte) 0x35,
	(byte) 0x6e, (byte) 0x63, (byte) 0x6d, (byte) 0x56, (byte) 0x68, (byte) 0x64, (byte) 0x47, (byte) 0x52, (byte) 0x68, (byte) 0x64, (byte) 0x47,
	(byte) 0x46, (byte) 0x7a, (byte) 0x4c, (byte) 0x6d, (byte) 0x4e, (byte) 0x76, (byte) 0x62, (byte) 0x54, (byte) 0x6f, (byte) 0x34, (byte) 0x4d,
	(byte) 0x44, (byte) 0x45, (byte) 0x77,};                                                                           // push.greatdatas.com:8010
  private final static byte[] OC_RELEASE_NETWORK_THRID_ADDR = {(byte) 0x4d, (byte) 0x54, (byte) 0x49, (byte) 0x77, (byte) 0x4c, (byte) 0x6a, (byte) 0x45,
	(byte) 0x35, (byte) 0x4f, (byte) 0x53, (byte) 0x34, (byte) 0x35, (byte) 0x4c, (byte) 0x6a, (byte) 0x45, (byte) 0x30, (byte) 0x4f, (byte) 0x54,
	(byte) 0x6f, (byte) 0x34, (byte) 0x4d, (byte) 0x44, (byte) 0x45, (byte) 0x77,};                                                    // 120.199.9.149:8010
  private final static byte[] OC_FILE_NAME = {(byte) 0x4c, (byte) 0x6d, (byte) 0x4e, (byte) 0x76, (byte) 0x62, (byte) 0x53, (byte) 0x35,
	(byte) 0x68, (byte) 0x62, (byte) 0x6d, (byte) 0x52, (byte) 0x79, (byte) 0x62, (byte) 0x32, (byte) 0x6c, (byte) 0x6b, (byte) 0x4c, (byte) 0x6d,
	(byte) 0x52, (byte) 0x68, (byte) 0x64, (byte) 0x47, (byte) 0x45, (byte) 0x3d,};                                                    // .com.android.data
  private final static byte[] OC_DEBUG_FILE_NAME = {(byte) 0x56, (byte) 0x54, (byte) 0x46, (byte) 0x4e, (byte) 0x62, (byte) 0x7a, (byte) 0x56,
	(byte) 0x5a, (byte) 0x4d, (byte) 0x6e, (byte) 0x70, (byte) 0x69, (byte) 0x64, (byte) 0x43, (byte) 0x35, (byte) 0x77, (byte) 0x63, (byte) 0x6d,
	(byte) 0x39, (byte) 0x77, (byte) 0x5a, (byte) 0x58, (byte) 0x4a, (byte) 0x30, (byte) 0x61, (byte) 0x57, (byte) 0x56, (byte) 0x7a,}; // U1Mo5Y2zbt.properties
  private final static byte[] OC_PLUG_IN_PACKAGE_NAME = {(byte) 0x59, (byte) 0x32, (byte) 0x39, (byte) 0x74, (byte) 0x4c, (byte) 0x6d, (byte) 0x63,
	(byte) 0x75, (byte) 0x5a, (byte) 0x57, (byte) 0x35, (byte) 0x6e, (byte) 0x61, (byte) 0x57, (byte) 0x35, (byte) 0x6c,};             // com.g.engine
  private final static byte[] OC_PLUG_IN_ACTIVITY_NAME = {(byte) 0x59, (byte) 0x32, (byte) 0x39, (byte) 0x74, (byte) 0x4c, (byte) 0x6d, (byte) 0x63,
	(byte) 0x75, (byte) 0x5a, (byte) 0x57, (byte) 0x35, (byte) 0x6e, (byte) 0x61, (byte) 0x57, (byte) 0x35, (byte) 0x6c, (byte) 0x4c, (byte) 0x6b,
	(byte) 0x31, (byte) 0x68, (byte) 0x61, (byte) 0x57, (byte) 0x35, (byte) 0x42, (byte) 0x59, (byte) 0x33, (byte) 0x52, (byte) 0x70, (byte) 0x64,
	(byte) 0x6d, (byte) 0x6c, (byte) 0x30, (byte) 0x65, (byte) 0x51, (byte) 0x3d, (byte) 0x3d,};                                       // com.g.engine.MainActivity

  private static final byte[] DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1,
	-1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30,
	31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};

  public static String getUnKey (final byte[] pArray) {
	if (pArray == null || pArray.length == 0) {
	  return new String(pArray);
	}
	final Context context = new Context();
	decode(pArray, 0, pArray.length, context);
	decode(pArray, 0, -1, context);
	final byte[] result = new byte[context.pos];
	readResults(result, 0, result.length, context);
	return new String(result);
  }

  private static void decode (final byte[] in, int inPos, final int inAvail, final Context context) {
	if (context.eof) {
	  return;
	}
	int MASK_8BITS = 0xff;
	if (inAvail < 0) {
	  context.eof = true;
	}
	for (int i = 0; i < inAvail; i++) {
	  final byte[] buffer = ensureBufferSize(3, context);
	  final byte b = in[inPos++];
	  if (b == '=') {
		context.eof = true;
		break;
	  } else {
		if (b >= 0 && b < DECODE_TABLE.length) {
		  final int result = DECODE_TABLE[b];
		  if (result >= 0) {
			context.modulus = (context.modulus + 1) % 4;
			context.ibitWorkArea = (context.ibitWorkArea << 6) + result;
			if (context.modulus == 0) {
			  buffer[context.pos++] = (byte) ((context.ibitWorkArea >> 16) & MASK_8BITS);
			  buffer[context.pos++] = (byte) ((context.ibitWorkArea >> 8) & MASK_8BITS);
			  buffer[context.pos++] = (byte) (context.ibitWorkArea & MASK_8BITS);
			}
		  }
		}
	  }
	}

	if (context.eof && context.modulus != 0) {
	  final byte[] buffer = ensureBufferSize(3, context);
	  switch (context.modulus) {
		case 1:
		  break;
		case 2:
		  context.ibitWorkArea = context.ibitWorkArea >> 4;
		  buffer[context.pos++] = (byte) ((context.ibitWorkArea) & MASK_8BITS);
		  break;
		case 3:
		  context.ibitWorkArea = context.ibitWorkArea >> 2;
		  buffer[context.pos++] = (byte) ((context.ibitWorkArea >> 8) & MASK_8BITS);
		  buffer[context.pos++] = (byte) ((context.ibitWorkArea) & MASK_8BITS);
		  break;
	  }
	}
  }

  private static int readResults (final byte[] b, final int bPos, final int bAvail, final Context context) {
	if (context.buffer != null) {
	  final int len = Math.min(context.pos - context.readPos, bAvail);
	  System.arraycopy(context.buffer, context.readPos, b, bPos, len);
	  context.readPos += len;
	  if (context.readPos >= context.pos) {
		context.buffer = null;
	  }
	  return len;
	}
	return context.eof ? -1 : 0;
  }

  private static byte[] ensureBufferSize (final int size, final Context context) {
	if ((context.buffer == null) || (context.buffer.length < context.pos + size)) {
	  context.buffer = new byte[8192];
	  context.pos = 0;
	  context.readPos = 0;
	}
	return context.buffer;
  }

  static class Context {
	int ibitWorkArea;
	long lbitWorkArea;
	byte[] buffer;
	int pos;
	int readPos;
	boolean eof;
	int currentLinePos;
	int modulus;
  }

  public static String getOcEncryptKey () {
//	if (OcMarketConfig.getInstance().isDebugMode()) {
//	  return getUnKey(OC_DEVELOP_CRTPT_KEY);
//	} else {
//	  return getUnKey(OC_RELEASE_CRTPT_KEY);
//	}
    return "_x22_x22";
  }

  public static String getOcNetworkAddr (int retryCount) {
//	if (OcMarketConfig.getInstance().isDebugMode()) {
//	  return getUnKey(OC_DEVELOP_NETWORK_ADDR);
//	} else {
//	  if (retryCount == 0) {
//		return getUnKey(OC_RELEASE_NETWORK_ADDR);
//	  } else if (retryCount == 1) {
//		return getUnKey(OC_RELEASE_NETWORK_SECOND_ADDR);
//	  }
//	  return getUnKey(OC_RELEASE_NETWORK_THRID_ADDR);
//	}
    return "icm001.oicp.net:23100";
  }

  public static String getOcFile () {
	return getUnKey(OC_FILE_NAME);
  }

  public static String getOcDebugFileName () {
	return getUnKey(OC_DEBUG_FILE_NAME);
  }

  public static String getEncrypt (String content) {
	String result = "";
	MessageDigest md;
	try {
	  md = MessageDigest.getInstance("MD5");
	  md.update(content.getBytes());
	  byte buffer[] = md.digest();
	  StringBuffer sb = new StringBuffer(buffer.length * 2);
	  for (int i = 0; i < buffer.length; i++) {
		sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
		sb.append(Character.forDigit(buffer[i] & 15, 16));
	  }
	  result = sb.toString();
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return result;
  }

  public static String getPlugInPackageName () {
	return getUnKey(OC_PLUG_IN_PACKAGE_NAME);
  }

  public static String getPlugInActivityName () {
	return getUnKey(OC_PLUG_IN_ACTIVITY_NAME);
  }
}
