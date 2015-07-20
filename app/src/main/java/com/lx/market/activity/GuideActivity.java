package com.lx.market.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lx.market.listener.OnViewChangeListener;
import com.lx.market.ui.widget.GuideScrollLayout;

import market.lx.com.R;

public class GuideActivity extends Activity implements
  OnViewChangeListener {
  private GuideScrollLayout mScrollLayout;
  private ImageView[] imgs;
  private int count;
  private int currentItem;
  private Button startBtn;
  private LinearLayout pointLLayout;

  @Override
  public void onCreate (Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	  WindowManager.LayoutParams.FLAG_FULLSCREEN);
	setContentView(R.layout.oc_guide);
	initView();
  }

  private void initView () {
	mScrollLayout = (GuideScrollLayout) findViewById(R.id.ScrollLayout);
	pointLLayout = (LinearLayout) findViewById(R.id.llayout);
	startBtn = (Button) findViewById(R.id.startBtn);
	startBtn.setOnClickListener(onClick);
	count = mScrollLayout.getChildCount();
	imgs = new ImageView[count];
	for (int i = 0; i < count; i++) {
	  imgs[i] = (ImageView) pointLLayout.getChildAt(i);
	  imgs[i].setEnabled(true);
	  imgs[i].setTag(i);
	}
	currentItem = 0;
	imgs[currentItem].setEnabled(false);
	mScrollLayout.setOnViewChangeListener(this);
  }

  private View.OnClickListener onClick = new View.OnClickListener() {
	@Override
	public void onClick (View v) {
	  switch (v.getId()) {
		case R.id.startBtn:
		  mScrollLayout.setVisibility(View.GONE);
		  pointLLayout.setVisibility(View.GONE);
		  startActivity(new Intent(GuideActivity.this, AppDetailActivity.class));
		  GuideActivity.this.finish();
		  break;
	  }
	}
  };

  @Override
  public void OnViewChange (int position) {
	setCurrentPoint(position);
  }

  private void setCurrentPoint (int position) {
	if (position < 0 || position > count - 1 || currentItem == position) {
	  return;
	}
	imgs[currentItem].setEnabled(true);
	imgs[position].setEnabled(false);
	currentItem = position;
  }

}
