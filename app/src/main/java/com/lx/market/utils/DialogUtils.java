package com.lx.market.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.lx.market.MarketApplication;
import com.lx.market.model.ClientInfo;
import com.lx.market.ui.dialog.CustomWaitingDialog;
import com.lx.market.ui.dialog.WaitingDialog;

/**
 * 功能：实现Dialog的类，用于创建通用对话框 例如 Yes/No对话框，等待对话框等等
 */
public class DialogUtils {

  public static final String NET_SET_DLG = "net_set_dlg";

  // 等待对话框
  public static DialogFragment createWaitingDialog (String message) {
	return WaitingDialog.newInstance(message);
  }

  // 等待对话框
  public static Dialog createWaitingDialog (Context ctx) {
	CustomWaitingDialog dlg = new CustomWaitingDialog(ctx);
	return dlg;
  }

  // 自定义提示的对话框
  public static Dialog createWaitingDialog (Context ctx, int strID) {
	CustomWaitingDialog dlg = new CustomWaitingDialog(ctx);
	dlg.setWaitInfo(strID);
	return dlg;
  }

  /**
   * 设置网络的DLG
   *
   * @param ctx
   * @return
   */
  public static boolean setNetDlg (Context ctx) {
	int netType = ClientInfo.networkType;
	// 弹出是否继续对话框
	if ((netType != ClientInfo.WIFI)
	  && MarketApplication.isDownloadWIFIOnly()) {
	  return true;
	}
	return false;
  }

}
