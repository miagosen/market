package com.lx.market.activity;

import market.lx.com.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

public class NoneNetworkActivity extends Activity implements OnClickListener {

  private RelativeLayout rl_setting_network;
  private RelativeLayout rl_manager_app;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.oc_activity_none_network);

    rl_setting_network = (RelativeLayout) findViewById(R.id.oc_activity_none_network_setting);
    rl_manager_app = (RelativeLayout) findViewById(R.id.oc_activity_none_network_manager_apps);

    rl_setting_network.setOnClickListener(this);
    rl_manager_app.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.oc_activity_none_network_setting:
      startActivity(new Intent("android.settings.SETTINGS"));
      break;

    case R.id.oc_activity_none_network_manager_apps:
      startActivity(new Intent(NoneNetworkActivity.this, DownloadActivity.class));
      break;
    }
  }
}
