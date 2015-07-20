package com.lx.market.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lx.market.utils.AppInfoUtils;

/**
 * Created by Antikvo.Miao on 2014/9/15.
 */
public class PackageInfoReceiver extends BroadcastReceiver {
  @Override
  public void onReceive (Context context, Intent intent) {
	AppInfoUtils.clearCache(context);
  }
}
