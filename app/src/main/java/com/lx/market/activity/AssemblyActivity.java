package com.lx.market.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lx.market.MarketApplication;
import com.lx.market.adapter.AppListAdapter;
import com.lx.market.constants.BundleConstants;
import com.lx.market.constants.GlobalConstants;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.connection.OcHttpConnection;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.protocol.GetApkListByPageReq;
import com.lx.market.network.protocol.GetApkListByPageResp;
import com.lx.market.utils.AppInfoUtils;
import com.lx.market.utils.PhoneInfoUtils;

import java.util.ArrayList;
import java.util.List;

import market.lx.com.R;

/**
 * Created by Antikvo.Miao on 2014/10/10.
 */

public class AssemblyActivity extends Activity {
  
  private PullToRefreshListView lvNewApps;
  
  private ImageView             ivBackButton;
  private AppListAdapter        appListAdapter;
  private List<AppInfoBto>      appInfoBtos = new ArrayList<AppInfoBto>();
  private int                   assemblyId;
  
  private ProgressBar loadingView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.app_list_activity);
    initView();
    Intent intent = getIntent();
    if (intent != null) {
      assemblyId = intent.getIntExtra(BundleConstants.BUNDLE_ASSEMBLY_ID, -1);
      appListAdapter = new AppListAdapter(this, appInfoBtos);
      lvNewApps.setAdapter(appListAdapter);
      lvNewApps.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
      lvNewApps.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
        @Override
        public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
          onLoadMore();
        }
      });
      ivBackButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          finish();
        }
      });
      loadingView.setVisibility(View.VISIBLE);
      loadData();
    } else {
      finish();
    }
  } 
  
  private void initView(){
	  lvNewApps = (PullToRefreshListView)findViewById(R.id.xlv_app_list);
	  ivBackButton = (ImageView)findViewById(R.id.setting_back_button);	 
	  loadingView = (ProgressBar)findViewById(R.id.pb_loading_view);
  }
  
@Override
  protected void onDestroy() {
    super.onDestroy();
    appListAdapter.removeDownLoadHandler();
  }

  protected void loadData() {
    if (assemblyId > 0) {
      GetApkListByPageReq req = new GetApkListByPageReq();
      req.setInstallList(AppInfoUtils.getInstalledPageInfos(getApplicationContext()));
      req.setUninstallList(AppInfoUtils.getUninstalledPageInfos());
      req.setAssemblyId(assemblyId);
      req.setScreenHeight(PhoneInfoUtils.getPhoneScreenHeight(getApplicationContext()));
      req.setScreenWidth(PhoneInfoUtils.getPhoneScreenWidth(getApplicationContext()));
      req.setMarketId(MarketApplication.getInstance().marketFrameResp.getMarketId());
      req.setStart(appInfoBtos.size());
      req.setFixedLength(GlobalConstants.PAGE_SIZE);
      OcHttpConnection http = new OcHttpConnection(getApplicationContext());
      http.sendRequest(RequestType.MARKET_DATA, req, GetApkListByPageResp.class, new OcHttpReqCallBack() {
        @Override
        public void onResponse(boolean result, Object respOrMsg) {
          if (result) {
        	  loadingView.setVisibility(View.GONE);
            GetApkListByPageResp resp = (GetApkListByPageResp) respOrMsg;
            if (resp.getErrorCode() == 0 && resp.getAppList() != null) {
              if (resp.getAppList().size() == 0) {
                // showToast(getResources().getString(R.string.no_more));
              } else {
                appInfoBtos.addAll(resp.getAppList());
                appListAdapter.notifyDataSetChanged();
              }
            }
          }
        }
      });
    }
  }

  public void onLoadMore() {
    loadData();
  }
}
