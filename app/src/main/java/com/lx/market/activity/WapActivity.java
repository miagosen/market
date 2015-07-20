package com.lx.market.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lx.market.constants.BundleConstants;
import com.lx.market.network.model.AppInfoBto;

import market.lx.com.R;

/**
 * Created by Antikvo.Miao on 2014/10/10.
 */
@ContentView(R.layout.wap_activity)
public class WapActivity extends Activity {

  @ViewInject(R.id.wv_wap_screen)
  private WebView wv_wap_screen;
  @ViewInject(R.id.ll_loading_view)
  private LinearLayout llGeneralLoading;
  AppInfoBto appInfoBto;
  private int position;
  private long startActivityTime;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ViewUtils.inject(this);
	Intent i = getIntent();
	if (i != null) {
	  appInfoBto = (AppInfoBto) i.getSerializableExtra(BundleConstants.BUNDLE_APP_INFO_BTO);
	  if (appInfoBto != null) {
		wv_wap_screen.loadUrl(appInfoBto.getDownUrl());
	  }
	}
  }

  private void initViews () {
	WebSettings webSettings = wv_wap_screen.getSettings();
	webSettings.setSaveFormData(false);
	webSettings.setJavaScriptEnabled(true);
	webSettings.setSupportZoom(true);
	webSettings.setBuiltInZoomControls(true);
	wv_wap_screen.setWebViewClient(new WebViewClient() {
	  @Override
	  public boolean shouldOverrideUrlLoading (WebView view, String url) {
		if (url.replaceAll(" ", "").contains("about:blank")) {
		  return false;
		}
		view.loadUrl(url);
		return true;
	  }

	});

	wv_wap_screen.setWebChromeClient(new WebChromeClient() {
	  @Override
	  public void onProgressChanged (WebView view, int newProgress) {
		if (newProgress > 60)
		  llGeneralLoading.setVisibility(View.GONE);
	  }
	});
  }

  public boolean onKeyDown (int keyCode, KeyEvent event) {
	if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_wap_screen.canGoBack()) {
	  wv_wap_screen.goBack();
	  return true;
	}
	return super.onKeyDown(keyCode, event);
  }

  @Override
  protected void onDestroy () {
	super.onDestroy();
  }

}
