package com.lx.market.config;

import android.util.Log;

import com.lx.market.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MarketConfig {
  private static MarketConfig instance = null;

  private Properties p = null;

  // 是否打印日志
  private boolean isOpenLog = true;
  // 内外网判断
  private boolean isDebugMode = true;

  public static MarketConfig getInstance () {
	if (instance == null) {
	  instance = new MarketConfig();
	}
	return instance;
  }

  private MarketConfig() {
	if (p == null) {
	  File myFile = FileUtils.getDebugFile();
	  if (myFile != null && myFile.exists()) {
		p = new Properties();
		FileInputStream fis = null;
		try {
		  fis = new FileInputStream(myFile);
		  p.load(fis);
		} catch (FileNotFoundException e) {
		  e.printStackTrace();
		} catch (IOException e) {
		  e.printStackTrace();
		} finally {
		  if (fis != null) {
			try {
			  fis.close();
			} catch (IOException e) {
			  e.printStackTrace();
			}
		  }
		}
	  }
	}
	initDebugMode();
	initLog();
  }

  // 如果有该属性且为b，设为内网，否则为现网
  private void initDebugMode () {
	if (p != null) {
	  String b = p.getProperty("a");
	  if ("b".equals(b)) {
		isDebugMode = true;
	  }
	}
	Log.d("OcMarket", "initDebugMode = " + isDebugMode);
  }

  /**
   * b=1，则打开log信息
   */
  private void initLog () {
	if (p != null) {
	  String b = p.getProperty("b");
	  if ("1".equals(b)) {
		isOpenLog = true;
	  }
	}
	Log.d("OcMarket", "isOpenLog = " + isOpenLog);
  }

  public boolean isOpenLog () {
	return isOpenLog;
  }

  public boolean isDebugMode () {
	return isDebugMode;
  }

}
