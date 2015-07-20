package com.lx.market.network.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESUtil {

  /**
   * 加密
   */
  public static byte[] encrypt (byte[] src, byte[] key) throws Exception {
	src = padding(src, (byte) 0);

	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

	DESKeySpec dks = new DESKeySpec(key);
	SecretKey securekey = keyFactory.generateSecret(dks);

	Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");

	cipher.init(1, securekey);

	return cipher.doFinal(src);
  }

  /**
   * 解密
   *
   * @throws Exception
   */
  public static byte[] decrypt (byte[] src, byte[] key) throws Exception {
	DESKeySpec dks = new DESKeySpec(key);

	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	SecretKey securekey = keyFactory.generateSecret(dks);

	Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");

	cipher.init(2, securekey);

	return cipher.doFinal(src);
  }

  private static byte[] padding (byte[] sourceBytes, byte b) {
	int paddingSize = 8 - sourceBytes.length % 8;
	byte[] paddingBytes = new byte[paddingSize];
	for (int i = 0; i < paddingBytes.length; ++i) {
	  paddingBytes[i] = b;
	}
	sourceBytes = ArrayUtils.addAll(sourceBytes, paddingBytes);
	return sourceBytes;
  }

}