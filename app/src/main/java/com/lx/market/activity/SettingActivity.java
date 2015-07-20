package com.lx.market.activity;

import market.lx.com.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.connection.OcHttpConnection;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.protocol.GetApkSelfUpdateReq;
import com.lx.market.network.protocol.GetApkSelfUpdateResp;
import com.lx.market.network.utils.TerminalInfoUtil;
import com.lx.market.utils.Logger;
import com.lx.market.utils.ToastUtils;

/**
 * Created by Antikvo.Miao on 2014/10/9.
 */
@ContentView(R.layout.oc_setting)
public class SettingActivity extends ActionBarActivity implements View.OnClickListener, UpdateDialogListener {

  @ViewInject(R.id.setting_back_button)
  private ImageView           ivBackButton;
  @ViewInject(R.id.ck_root)
  private CheckBox            cbRoot;
  @ViewInject(R.id.ck_auto_load)
  private CheckBox            cbAutoLoad;
  @ViewInject(R.id.ck_no_wifi)
  private CheckBox            cbNoWifi;
  @ViewInject(R.id.rb_female)
  private RadioButton         rbFemale;

  @ViewInject(R.id.ll_loading_view)
  private LinearLayout        ll_loading_view;

  private SharedPreferences   sharedPreferences;
  private static final String SETTING_SHARED_PREFERENCE = "setting";
  private static final String SETTING_ROOT              = "root";
  private static final String SETTING_AUTO_LOAD         = "auto_load";
  private static final String SETTING_NO_WIFI           = "no_wifi";
  private static final String SETTING_SEX               = "sex";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    sharedPreferences = getSharedPreferences(SETTING_SHARED_PREFERENCE, 0);
    ViewUtils.inject(this);
    setUserSettings();
    initListener();
    ll_loading_view.setVisibility(View.GONE);
  }

  private int getLocalOCMarketVersionName() {
    PackageManager manager = getPackageManager();
    PackageInfo packageInfo = null;
    try {
      packageInfo = manager.getPackageInfo(getPackageName(), 0);

    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
    return packageInfo.versionCode;
  }

  private void setUserSettings() {

    boolean autoLoad = sharedPreferences.getBoolean(SETTING_AUTO_LOAD, false);
    boolean root = sharedPreferences.getBoolean(SETTING_ROOT, false);
    boolean noWifi = sharedPreferences.getBoolean(SETTING_NO_WIFI, false);
    boolean sex = sharedPreferences.getBoolean(SETTING_SEX, false);

    cbAutoLoad.setChecked(autoLoad);
    cbRoot.setChecked(root);
    cbNoWifi.setChecked(noWifi);
    if (sex) {
      rbFemale.setChecked(sex);
    }

  }

  private void initListener() {
    rbFemale.setOnClickListener(this);
    cbAutoLoad.setOnClickListener(this);
    cbNoWifi.setOnClickListener(this);
    cbRoot.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.ck_auto_load:
      sharedPreferences.edit().putBoolean(SETTING_AUTO_LOAD, ((CheckBox) v).isChecked()).apply();
      break;
    case R.id.ck_root:
      sharedPreferences.edit().putBoolean(SETTING_ROOT, ((CheckBox) v).isChecked()).apply();
      break;
    case R.id.rb_female:
      sharedPreferences.edit().putBoolean(SETTING_SEX, ((RadioButton) v).isChecked()).apply();
      break;
    case R.id.ck_no_wifi:
      sharedPreferences.edit().putBoolean(SETTING_NO_WIFI, ((CheckBox) v).isChecked()).apply();
      break;
    }
  }

  @OnClick(R.id.rl_suggest_set)
  public void suggestOnClick(View v) {

  }

  @OnClick(R.id.rl_about_us)
  public void aboutUsOnClick(View v) {
    Intent intent = new Intent(this, AboutUs.class);
    this.startActivity(intent);
  }

  @OnClick(R.id.rl_check_update)
  public void updateOnClick(View v) {
    checkUpdate();
  }

  @OnClick(R.id.setting_back_button)
  public void onBackClick(View v) {
    this.finish();
  }

  private void showDialog() {
    // UpdateDialogFragment fragment = new UpdateDialogFragment(this, "", "");
    // fragment.show(getSupportFragmentManager(), "ShowDialog");
  }

  @Override
  public void onDialogPositiveClick() {
    ToastUtils.showToast("Yes");
    // DownloadInfo info = new DownloadInfo();
    // info.setPackageName(packageName);
    // info.setDownloadUrl(downloadUrl);
    // AppManagerCenter.startDownload(game)
  }

  @Override
  public void onDialogNegativeClick() {
    ToastUtils.showToast("No");

  }

  private void checkUpdate() {
    ll_loading_view.setVisibility(View.VISIBLE);
    final GetApkSelfUpdateReq req = new GetApkSelfUpdateReq();
    OcHttpConnection httpConnection = new OcHttpConnection(this);
    req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(this));
    httpConnection.sendRequest(RequestType.UPDATE, req, GetApkSelfUpdateResp.class, new OcHttpReqCallBack() {
      @Override
      public void onResponse(boolean result, Object respOrMsg) {
        Log.e("---------", "--------" + result);
        ll_loading_view.setVisibility(View.GONE);
        if (result) {
          GetApkSelfUpdateResp resp = (GetApkSelfUpdateResp) respOrMsg;
          if (resp.getErrorCode() == 0) {
            Logger.error("GetApkSelfUpdateResp:" + resp.toString());
            // TODO
            int serverVCode = resp.getVer();
            int localVCode = getLocalOCMarketVersionName();
            if (localVCode < serverVCode) {

              showDialog();
            } else {
              ll_loading_view.setVisibility(View.GONE);
              Toast.makeText(SettingActivity.this, getString(R.string.no_update_version), Toast.LENGTH_SHORT).show();
            }

          } else {
            Logger.error(resp.getErrorMessage());
          }
        } else {
          Logger.error("GetApkSelfUpdateReq error:" + respOrMsg);

        }
      }
    });
  }

}
