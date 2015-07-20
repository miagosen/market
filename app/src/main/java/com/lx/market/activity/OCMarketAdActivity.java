package com.lx.market.activity;

import market.lx.com.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.lx.market.ui.widget.MutiCheckAppItemView;
import com.lx.market.ui.widget.ProgressButton;

public class OCMarketAdActivity extends Activity implements OnClickListener {

  private ImageView ibBackButton;
  private ImageView ivTop;
  private TextView  tvMid;
  private MutiCheckAppItemView mv_1, mv_2, mv_3, mv_4;
  private ProgressButton       pbDownload;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.oc_activity_market_ad);
    initView();
    initListener();
  }

  private void initView() {
    ibBackButton = (ImageView) findViewById(R.id.market_ad_back_btn);
    ivTop = (ImageView) findViewById(R.id.market_ad_top_image);
    tvMid = (TextView) findViewById(R.id.market_ad_text);

    pbDownload = (ProgressButton) findViewById(R.id.pb_muti_check);

    mv_1 = (MutiCheckAppItemView) findViewById(R.id.muti_check_view_1);
    mv_2 = (MutiCheckAppItemView) findViewById(R.id.muti_check_view_2);
    mv_3 = (MutiCheckAppItemView) findViewById(R.id.muti_check_view_3);
    mv_4 = (MutiCheckAppItemView) findViewById(R.id.muti_check_view_4);
  }

  private void initListener() {
    ibBackButton.setOnClickListener(this);

    mv_1.setOnClickListener(this);
    mv_2.setOnClickListener(this);
    mv_3.setOnClickListener(this);
    mv_4.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {

    switch (v.getId()) {
    case R.id.market_ad_back_btn:
      finish();
      startActivity(new Intent(OCMarketAdActivity.this, MainActivity.class));
      break;
    case R.id.muti_check_view_1:
      mv_1.setOnClickListener(this);
      break;
    case R.id.muti_check_view_2:
      mv_2.setOnClickListener(this);
      break;
    case R.id.muti_check_view_3:
      mv_3.setOnClickListener(this);
      break;
    case R.id.muti_check_view_4:
      mv_4.setOnClickListener(this);
      break;
    }

  }

  private void loadData() {
    // TODO:加载数据
  }

}
