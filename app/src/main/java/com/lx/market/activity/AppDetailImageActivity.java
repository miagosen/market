package com.lx.market.activity;

import market.lx.com.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.lx.market.adapter.AppDetailImageAdapter;
import com.viewpagerindicator.CirclePageIndicator;

public class AppDetailImageActivity extends Activity {
  private ViewPager             vp;
  private CirclePageIndicator   mCPI;
  private AppDetailImageAdapter adapter;
  private String[]              data;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.oc_app_detail_image_activity);
    Intent intent = getIntent();
    data = intent.getStringArrayExtra(AppDetailActivity.APP_DETAIL_IMAGES);
    vp = (ViewPager) findViewById(R.id.vp_app_detail_image);
    mCPI = (CirclePageIndicator) findViewById(R.id.ci_app_detail_image);
    adapter = new AppDetailImageAdapter(this, data);
    vp.setAdapter(adapter);
    mCPI.setViewPager(vp);
  }
}
