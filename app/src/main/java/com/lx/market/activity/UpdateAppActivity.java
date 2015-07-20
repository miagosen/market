package com.lx.market.activity;

import java.util.ArrayList;
import java.util.List;

import market.lx.com.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lx.market.adapter.UpdateAppsAdapter;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.connection.OcHttpConnection;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.protocol.GetLocalAppsUpdateReq;
import com.lx.market.network.protocol.GetLocalAppsUpdateResp;
import com.lx.market.network.utils.TerminalInfoUtil;

public class UpdateAppActivity extends Activity {

  private LinearLayout           ll_loading_view;
  private ListView               lvUpdateApps;
  private TextView               tvNoUpdate;
  private UpdateAppsAdapter      updateAppsAdapter;

  private GetLocalAppsUpdateResp resp;

  private final List<AppInfoBto> appInfoBtos = new ArrayList<AppInfoBto>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.oc_activity_update);

    lvUpdateApps = (ListView) findViewById(R.id.lv_update_apps);
    ll_loading_view = (LinearLayout) findViewById(R.id.ll_loading_view);
    tvNoUpdate = (TextView) findViewById(R.id.tv_no_update_app);
    updateAppsAdapter = new UpdateAppsAdapter(UpdateAppActivity.this, appInfoBtos);
    lvUpdateApps.setAdapter(updateAppsAdapter);
    loadData();
    ll_loading_view.setVisibility(View.GONE);
  }

  private void loadData() {
    ll_loading_view.setVisibility(View.VISIBLE);
    GetLocalAppsUpdateReq req = new GetLocalAppsUpdateReq();
    req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(UpdateAppActivity.this));
    OcHttpConnection http = new OcHttpConnection(UpdateAppActivity.this);
    http.sendRequest(RequestType.UPDATE, req, GetLocalAppsUpdateResp.class, new OcHttpReqCallBack() {

      @Override
      public void onResponse(boolean result, Object respOrMsg) {
        ll_loading_view.setVisibility(View.GONE);
        if (result) {
          resp = (GetLocalAppsUpdateResp) respOrMsg;
          if (resp.getErrorCode() == 0) {
            if (resp.getAppList() != null && resp.getAppList().size() > 0) {
              appInfoBtos.addAll(resp.getAppList());
            }
          }
        } else {
          tvNoUpdate.setVisibility(View.VISIBLE);
        }
        updateAppsAdapter.notifyDataSetChanged();
      }
    });
  }

}
