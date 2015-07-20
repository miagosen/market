package com.lx.market.fragment;

import market.lx.com.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lx.market.activity.AboutUs;
import com.lx.market.activity.DownloadActivity;
import com.lx.market.activity.UpdateAppActivity;
import com.lx.market.activity.UpdateDialogListener;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.connection.OcHttpConnection;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.protocol.GetApkSelfUpdateReq;
import com.lx.market.network.protocol.GetApkSelfUpdateResp;
import com.lx.market.network.utils.TerminalInfoUtil;
import com.lx.market.service.FloatWindowService;
import com.lx.market.ui.dialog.UpdateDialogFragment;
import com.lx.market.utils.Logger;

/**
 * Created by zhangyn on 2014/11/26.
 */
public class UserFragment extends Fragment implements OnClickListener {

  private View                view;
  private CheckBox            cbRoot;
  private CheckBox            cbAutoLoad;
  private CheckBox            cbNoWifi;
  private CheckBox            cbFlowWindow;
  private LinearLayout        ll_loading_view;
  private RelativeLayout      rl_about_us;
  private RelativeLayout      rl_check_update_mine;
  private RelativeLayout      rl_download_manager;
  private RelativeLayout      rl_update_manager;
  private SharedPreferences   sharedPreferences;
  private static final String SETTING_SHARED_PREFERENCE = "setting";
  private static final String SETTING_ROOT              = "root";
  private static final String SETTING_AUTO_LOAD         = "auto_load";
  private static final String SETTING_NO_WIFI           = "no_wifi";
  private static final String SETTING_FLOW_WINDOW       = "flow_window";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
    if (view == null) {
      view = inflater.inflate(R.layout.oc_mine, container, false);
      initViews(view);
      sharedPreferences = getActivity().getSharedPreferences(SETTING_SHARED_PREFERENCE, 0);
      setUserSettings();
      initListener();
    } else {
      ViewGroup viewGroup = (ViewGroup) view.getParent();
      if (viewGroup != null) {
        viewGroup.removeAllViewsInLayout();
      }
    }
    return view;
  }

  private void initViews(View view) {
    cbRoot = (CheckBox) view.findViewById(R.id.ck_root);
    cbNoWifi = (CheckBox) view.findViewById(R.id.ck_no_wifi);
    cbAutoLoad = (CheckBox) view.findViewById(R.id.ck_auto_load);
    cbFlowWindow = (CheckBox) view.findViewById(R.id.cb_flow_window);
    ll_loading_view = (LinearLayout) view.findViewById(R.id.ll_loading_view);
    ll_loading_view.setVisibility(View.GONE);
    rl_about_us = (RelativeLayout) view.findViewById(R.id.rl_about_us);
    rl_check_update_mine = (RelativeLayout) view.findViewById(R.id.rl_check_update_mine);
    rl_download_manager = (RelativeLayout) view.findViewById(R.id.download_manager);
    rl_update_manager = (RelativeLayout) view.findViewById(R.id.update_manager);
  }

  private int getLocalOCMarketVersionName() {
    PackageManager manager = getActivity().getPackageManager();
    PackageInfo packageInfo = null;
    try {
      packageInfo = manager.getPackageInfo(getActivity().getPackageName(), 0);

    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
    return packageInfo.versionCode;
  }

  private void setUserSettings() {

    boolean autoLoad = sharedPreferences.getBoolean(SETTING_AUTO_LOAD, false);
    boolean root = sharedPreferences.getBoolean(SETTING_ROOT, false);
    boolean noWifi = sharedPreferences.getBoolean(SETTING_NO_WIFI, false);
    boolean flowWindow = sharedPreferences.getBoolean(SETTING_FLOW_WINDOW, false);
    cbAutoLoad.setChecked(autoLoad);
    cbRoot.setChecked(root);
    cbNoWifi.setChecked(noWifi);
    cbFlowWindow.setChecked(flowWindow);
  }

  private void initListener() {
    cbAutoLoad.setOnClickListener(this);
    cbNoWifi.setOnClickListener(this);
    cbRoot.setOnClickListener(this);
    cbFlowWindow.setOnClickListener(this);
    rl_about_us.setOnClickListener(this);
    rl_check_update_mine.setOnClickListener(this);
    rl_download_manager.setOnClickListener(this);
    rl_update_manager.setOnClickListener(this);
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
    case R.id.ck_no_wifi:
      sharedPreferences.edit().putBoolean(SETTING_NO_WIFI, ((CheckBox) v).isChecked()).apply();
      break;
    case R.id.cb_flow_window:
      sharedPreferences.edit().putBoolean(SETTING_FLOW_WINDOW, ((CheckBox) v).isChecked()).apply();
      Intent service = new Intent(getActivity(), FloatWindowService.class);
      if (sharedPreferences.getBoolean(SETTING_FLOW_WINDOW, false)) {
        getActivity().startService(service);
      } else {
        getActivity().stopService(service);
      }
      break;
    case R.id.rl_about_us:
      Intent intent = new Intent(getActivity(), AboutUs.class);
      startActivity(intent);
      break;
    case R.id.rl_check_update_mine:
      checkUpdate();
      break;
    case R.id.download_manager:
      startActivity(new Intent(getActivity(), DownloadActivity.class));
      break;
    case R.id.update_manager:
      startActivity(new Intent(getActivity(), UpdateAppActivity.class));
      break;
    }
  }

  private void showDialog() {
    UpdateDialogFragment fragment = new UpdateDialogFragment(getActivity(), "", "", new UpdateDialogListener() {

      @Override
      public void onDialogPositiveClick() {
        Toast.makeText(getActivity(), "yes", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onDialogNegativeClick() {
        Toast.makeText(getActivity(), "no", Toast.LENGTH_SHORT).show();
      }
    });
    fragment.show(getFragmentManager(), "ShowDialog");
  }

  private void checkUpdate() {
    ll_loading_view.setVisibility(View.VISIBLE);
    final GetApkSelfUpdateReq req = new GetApkSelfUpdateReq();
    OcHttpConnection httpConnection = new OcHttpConnection(getActivity());
    req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(getActivity()));
    httpConnection.sendRequest(RequestType.UPDATE, req, GetApkSelfUpdateResp.class, new OcHttpReqCallBack() {
      @Override
      public void onResponse(boolean result, Object respOrMsg) {
        Log.e("----onResponse-----", "--------" + result);
        ll_loading_view.setVisibility(View.GONE);
        if (result) {
          GetApkSelfUpdateResp resp = (GetApkSelfUpdateResp) respOrMsg;
          if (resp.getErrorCode() == 0) {
            Logger.error("GetApkSelfUpdateResp:" + resp.toString());
            int serverVCode = resp.getVer();
            int localVCode = getLocalOCMarketVersionName();
            if (localVCode < serverVCode) {
              showDialog();
            } else {
              Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_update_version), Toast.LENGTH_SHORT).show();
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
