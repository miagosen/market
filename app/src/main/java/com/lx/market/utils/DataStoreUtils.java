package com.lx.market.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;

import com.lx.market.MarketApplication;

import java.io.InputStream;

public class DataStoreUtils {
	public static final String FILE_NAME = "oz_market";
	/**通用 true*/
	public static final String SP_TRUE = "true";
	/**通用 false*/
	public static final String SP_FALSE = "false";
	
	public static final String DEFAULT_VALUE = "";
	/** 封面页更新时间 */
	public static final String FRONT_COVER_UPDATE_TIME = "a";
	// 退出是否继续下载设置
	public static final String CONTINUE_DOWNLOAD_EXIT = "f";
	public static final String CONTINUE_DOWNLOAD_ENABLE = "1";//退出后继续下载,不杀进程
	public static final String CONTINUE_DOWNLOAD_UNABLE = "0"; // 退出后不继续下载,杀进程
	public static final String CONTINUE_DOWNLOAD_UNKNOW = ""; // 默认退出的时候，不继续下载

	/********* 更新信息 ***************/
	// 是否需要更新
	public static final String UPDATE_STATE = "x";
	public static final String UPDATE_STATE_ENABLE = "1";
	//
	public static final String UPDATE_APKDOWNLOADURL = "x1";
	public static final String UPDATE_APKNEWVER = "x2";
	public static final String UPDATE_APKNEWVERINT = "x3";
	public static final String UPDATE_UPGRADEPOLICY = "x4";
	public static final String UPDATE_UPGRADETOOLTIP = "x5";

	/*****************************/

	// WIFI设置
	public static final String DOWNLOAD_WIFI_ONLY = "y";
	public static final String DOWNLOAD_WIFI_ONLY_ENABLE = "1";
	public static final String DOWNLOAD_WIFI_ONLY_UNABLE = "";

	//check box on off
	public static final String CHECK_ON="on";
	public static final String CHECK_OFF="off";
	
	// 平台安装记录
	public static final String JOLY_PF = "z";
	public static final String JOLY_PF_UNINSTALLED = "";
	public static final String JOLY_PF_INSTALLED = "1";
	/**平台启动的最后时间*/
	public static final String LAST_ACTIVATE_CLIENT_TIME = "last_activate_t";
	/**是否提醒用户, on , off*/
	public static final String REMIND_USER_ENABLE = "remind";
	/**多长时间提醒, 单位：ms*/
	public static final String REMIND_INTERVAL_MS = "remind_ms";
	/**替换游戏对话框是否不再显示*/
	public static final String REPLACE_GAME_ALERT_NOT_SHOW="not_show_replace";
	/**软件更新提示设置**/
	public static final String GAME_UPDATES_REMINDER ="update_reminder";
	/**自动下载更新包*/
	public static final String AUTO_LOAD_UPDATE_PKG = "auto_load";
	/**notify news switch*/
	public static final String NOTIFY_NEWS_ENABLE = "notify_news";

	/**WIFI下提示“一键安装界面的开关和时间间隔”**/
	public static final String NESSARY_ENABLE = "nessary_enable";
	public static final String NESSARY_INTERVAL = "nessary_interval";
	public static final String LAST_GET_NESSARY_TIME = "last_get_nessary_time";
	
	public static final String SWITCH_INSTALL_SILENT = "silent";
	
	// 保存本地信息
	public static void saveLocalInfo(String name, String value) {
		SharedPreferences share = MarketApplication.curContext
				.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);

		if (share != null) {
			share.edit().putString(name, value).apply();
		}
	}

	// 读取本地信息
	public static String readLocalInfo(String name) {
		SharedPreferences share = MarketApplication.curContext
				.getSharedPreferences(FILE_NAME, 0);
		if (share != null) {
			return share.getString(name, DEFAULT_VALUE);
		}
		return DEFAULT_VALUE;
	}
	
	/**
	 * 从asserts 目录下读取图片文件
	 * 
	 * @param context
	 * @return
	 */
	public static BitmapDrawable readImgFromAssert(Context context,
			String imgFileName) {
		InputStream inputStream = null;
		BitmapDrawable drawable = null;

		if (null == imgFileName) {
			return null;
		}

		try {
			inputStream = context.getResources().getAssets().open(imgFileName);
			drawable = new BitmapDrawable(context.getResources(),inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return drawable;
	}

}
