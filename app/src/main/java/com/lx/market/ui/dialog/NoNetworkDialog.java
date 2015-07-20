package com.lx.market.ui.dialog;

import market.lx.com.R;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lx.market.MarketApplication;
import com.lx.market.activity.BaseActivity;

/**
 * Created by Antikvo.Miao on 2014/9/16.
 */
public class NoNetworkDialog extends ButtonDialog<String> {
  public static NoNetworkDialog newInstance(String title, String positive, String negative) {
    NoNetworkDialog dialog = new NoNetworkDialog();
    Bundle bundle = new Bundle();
    bundle.putString(ButtonDialog.TITLE, title);
    bundle.putString(ButtonDialog.NEGATIVE_BUTTON, negative);
    bundle.putString(ButtonDialog.POSITIVE_BUTTON, positive);
    bundle.putBoolean(ButtonDialog.CANCELED_ON_TOUCH_OUTSIDE, false);
    dialog.setArguments(bundle);
    return dialog;
  }

  @Override
  protected String getResult() {
    return "1";
  }

  @Override
  protected void createContent(ViewGroup content) {
    TextView tv = new TextView(MarketApplication.curContext);
    tv.setText(MarketApplication.curContext.getString(R.string.dlg_no_network));
    content.addView(tv);
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    BaseActivity activity = (BaseActivity) getActivity();
    if (null == activity) {
      return;
    }
    switch (id) {
    case R.id.dlg_ok_btn:
      String result = getResult();
      Intent intent = null;
      // 判断手机系统的版本 即API大于10 就是3.0或以上版本
      if (android.os.Build.VERSION.SDK_INT > 10) {
        intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
      } else {
        intent = new Intent();
        ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(component);
        intent.setAction("android.intent.action.VIEW");
      }
      activity.startActivity(intent);
      dismissAllowingStateLoss();
      break;
    case R.id.dlg_cancel_btn:
      dismissAllowingStateLoss();
      break;
    }
  }
}
