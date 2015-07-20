package com.lx.market.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lx.market.MarketApplication;

import market.lx.com.R;

public class ToastUtils {

  /**
   * Ui线程/非UI线程中显示 Toast
   */
  public static void showToast (final int strID) {
	showToast(strID, Gravity.BOTTOM);
  }

  /**
   * UI线程/非UI线程均可调用 显示 Toast
   */
  public static void showToast (final String str) {
	showToast(str, Gravity.BOTTOM);
  }

  /**
   * UI线程/非UI线程均可调用 显示 Toast
   */
  public static void showToast (final int strID, final int gravity) {
	showToast(MarketApplication.curContext.getString(strID), gravity);
  }

  private static Toast toast = null;
  private static Toast tipToast = null;
  private static Context context = null;

  /**
   * UI线程/非UI线程均可调用 显示 Toast
   */
  public static void showToast (final String str, final int gravity) {
	if (toast == null) {
	  try {
		context = MarketApplication.curContext;
		toast = Toast.makeText(MarketApplication.curContext, str,
		  Toast.LENGTH_SHORT);
		if (gravity == Gravity.BOTTOM) {
		  toast.setGravity(gravity, 0, 100);
		} else {
		  toast.setGravity(gravity, 0, 0);
		}
		toast.show();
	  } catch (Exception e) {
		MarketApplication.handler.post(new Runnable() {
		  @Override
		  public void run () {
			showToast(str, gravity);
		  }
		});
	  }
	} else {
	  toast.cancel();
	  toast = null;
	  showToast(str, gravity);
	}
  }

  /**
   * tip Toast
   *
   * @param msgId
   * @param iconId
   */
  public static void showTipToast (final int msgId, final int iconId) {
	if (tipToast == null) {
	  try {
		context = MarketApplication.curContext;
		tipToast = new Toast(context);
		View layout = LayoutInflater.from(context).inflate(
		  R.layout.toast_result, null);
		ImageView icon = (ImageView) layout
		  .findViewById(R.id.toast_image);
		icon.setBackgroundResource(iconId);
		TextView msg = (TextView) layout.findViewById(R.id.toast_text);
		msg.setText(msgId);
		// 设置Toast的位置
		tipToast.setGravity(Gravity.CENTER, 0, 0);
		tipToast.setDuration(Toast.LENGTH_SHORT);
		// 让Toast显示为我们自定义的样子
		tipToast.setView(layout);
		tipToast.show();
	  } catch (Exception e) {
		MarketApplication.handler.post(new Runnable() {
		  @Override
		  public void run () {
			showTipToast(msgId, iconId);
		  }
		});
	  }
	} else {
	  tipToast.cancel();
	  tipToast = null;
	  showTipToast(msgId, iconId);
	}
  }

  /**
   * 警告
   *
   * @param msgId
   */
  public static void showWarnningToast (int msgId) {
	showTipToast(msgId, R.drawable.toast_result_warnning);
  }

  /**
   * 成功
   *
   * @param msgId
   */
  public static void showOKToast (int msgId) {
	showTipToast(msgId, R.drawable.toast_result_ok);
  }

  /**
   * 错误
   *
   * @param msgId
   */
  public static void showErrorToast (int msgId) {
	showTipToast(msgId, R.drawable.toast_result_error);
  }

}
