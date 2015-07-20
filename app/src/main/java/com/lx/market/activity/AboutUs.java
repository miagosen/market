package com.lx.market.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import market.lx.com.R;

/**
 * Created by Antikvo.Miao on 2014/10/13.
 */

public class AboutUs extends Activity {

  private TextView tvAboutUs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.oc_activity_about_us);
    initView();
  }

  private void initView() {
    tvAboutUs = (TextView) findViewById(R.id.tv_about_us);
  }
}
