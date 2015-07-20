package com.lx.market.fragment;

import java.util.ArrayList;
import java.util.List;

import market.lx.com.R;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lx.market.MarketApplication;
import com.lx.market.activity.SearchAppActivity;
import com.lx.market.adapter.AppListAdapter;
import com.lx.market.adapter.FlowBarAdapter;
import com.lx.market.adapter.PopularAppInfoAdapter;
import com.lx.market.constants.GlobalConstants;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.connection.OcHttpConnection;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.model.AssemblyInfoBto;
import com.lx.market.network.model.ChannelInfoBto;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.protocol.GetApkListByPageReq;
import com.lx.market.network.protocol.GetApkListByPageResp;
import com.lx.market.network.protocol.GetMarketFrameResp;
import com.lx.market.ui.dialog.CommonToast;
import com.lx.market.ui.widget.HorizontalListView;
import com.lx.market.utils.AppInfoUtils;
import com.lx.market.utils.DialogUtils;
import com.lx.market.utils.Logger;
import com.lx.market.utils.PhoneInfoUtils;
import com.viewpagerindicator.CirclePageIndicator;

public class HomeFragment extends Fragment {
  private View                  contentView;
  private HorizontalListView    popularListView;
  /**
   * 圆点
   */
  private CirclePageIndicator   mCirclePageIndicator;
  /**
   * 推荐页1*N列表
   */
  private ListView              xlvAppInfos;
  /**
   * 顶部轮播
   */
  private ViewPager             vpFlowPager;
  /**
   * 首页（推荐页）
   */
  private PullToRefreshListView pullToRefreshListView;
  private FlowBarAdapter        flowBarAdapter;
  private PopularAppInfoAdapter popularAppInfoAdapter;
  private AppListAdapter        appListAdapter;
  private List<AppInfoBto>      flowBarData = new ArrayList<AppInfoBto>();
  private List<AppInfoBto>      popularData = new ArrayList<AppInfoBto>();
  private List<AppInfoBto>      appListData = new ArrayList<AppInfoBto>();
  // 应用列表栏目
  private AssemblyInfoBto       appListAssemblyInfo;
  private String                marketId    = "";
  private Dialog                loadingDialog;
  public static final int       HOME_PAGE   = 0;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (contentView == null) {
      contentView = inflater.inflate(R.layout.oc_activity_home, container, false);
      findViews(contentView);
      initViews();
      loadData();
      initAdapter();
    } else {
      ViewGroup p = (ViewGroup) contentView.getParent();
      if (p != null) {
        p.removeAllViewsInLayout();
      }
    }
    return contentView;
  }

  private void initAdapter() {
    flowBarAdapter = new FlowBarAdapter(getActivity(), flowBarData);
    vpFlowPager.setAdapter(flowBarAdapter);
    mCirclePageIndicator.setViewPager(vpFlowPager, 1);
    popularAppInfoAdapter = new PopularAppInfoAdapter(getActivity(), popularData);
    popularListView.setAdapter(popularAppInfoAdapter);
    appListAdapter = new AppListAdapter(getActivity(), appListData);
    appListAdapter.setDownlaodRefreshHandle();
    xlvAppInfos.setAdapter(appListAdapter);
    xlvAppInfos.setOnItemClickListener(appListAdapter);
    xlvAppInfos.setFocusable(true);
    pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    pullToRefreshListView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
      @Override
      public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
        onLoadMore();
      }
    });
  }
  public void onLoadMore() {
    if (appListAssemblyInfo != null) {
      GetApkListByPageReq req = new GetApkListByPageReq();
      req.setScreenHeight(PhoneInfoUtils.getPhoneScreenHeight(getActivity()));
      req.setScreenWidth(PhoneInfoUtils.getPhoneScreenWidth(getActivity()));
      req.setAssemblyId(appListAssemblyInfo.getAssemblyId());
      req.setStart(appListAssemblyInfo.getAppInfoList().size() + 1);
      req.setInstallList(AppInfoUtils.getInstalledPageInfos(getActivity()));
      req.setUninstallList(AppInfoUtils.getUninstalledPageInfos());
      req.setFixedLength(20);
      req.setMarketId(marketId);
      OcHttpConnection httpConnection = new OcHttpConnection(getActivity());
      httpConnection.sendRequest(RequestType.MARKET_DATA, req, GetApkListByPageResp.class, new OcHttpReqCallBack() {
        @Override
        public void onResponse(boolean result, Object respOrMsg) {
          if (result) {
            GetApkListByPageResp resp = (GetApkListByPageResp) respOrMsg;
            if (resp.getErrorCode() == 0) {
              List<AppInfoBto> newAppInfos = resp.getAppList();
              if (newAppInfos != null && newAppInfos.size() > 0) {
                appListAdapter.addAppInfoList(newAppInfos);
              } else {
                // HomeActivity.this.showToast(R.string.no_more, icon,
                // gravity)(getString(R.string.no_more));
                CommonToast commonToast = new CommonToast(getActivity());
                commonToast.setMessageIc(R.string.no_more);
              }
            }
          } else {
            // HomeActivity.this.toast(getString(R.string.slow_network));
            CommonToast commonToast = new CommonToast(getActivity());
            commonToast.setMessageIc(R.string.slow_network);
          }
          pullToRefreshListView.onRefreshComplete();
          appListAdapter.notifyDataSetChanged();
        }
      });
    }
  }

  private void findViews(View v) {
    pullToRefreshListView = (PullToRefreshListView) v.findViewById(R.id.lv_home_page);
    xlvAppInfos = pullToRefreshListView.getRefreshableView();
  }
  protected void loadData() {
    loadingDialog = DialogUtils.createWaitingDialog(getActivity(), R.string.dlg_waiting);
    loadingDialog.show();
    GetMarketFrameResp resp = MarketApplication.getInstance().marketFrameResp;
    if (resp != null) {
      try {
        marketId = resp.getMarketId();
        List<ChannelInfoBto> channelInfoBtos = resp.getChannelList();
        if (channelInfoBtos != null && channelInfoBtos.size() > 0) {
          for (int i = 0; i < channelInfoBtos.size(); i++) {
            if (i == HOME_PAGE) {
              setChannelHome(channelInfoBtos.get(HOME_PAGE));
              // } else if (i == SCEND_PAGE) {
              // setChannelStroll(channelInfoBtos.get(SCEND_PAGE));
              // } else if (i == THREED_PAGE) {
              // setChannelMy(channelInfoBtos.get(THREED_PAGE));
            }
          }
        }
      } catch (Exception e) {
        Logger.p(e);
      }
    }
    loadingDialog.dismiss();
  }

  private void setChannelHome(ChannelInfoBto channelInfoBto) {
    List<AssemblyInfoBto> assemblyInfoBtos = channelInfoBto.getTopicList().get(0).getAssemblyList();
    if (assemblyInfoBtos != null) {
      for (AssemblyInfoBto infoBto : assemblyInfoBtos) {
        if (infoBto.getType() == GlobalConstants.ASSEMBLY_TYPE_BANNER) {
          flowBarData = infoBto.getAppInfoList();
        }
        if (infoBto.getType() == GlobalConstants.ASSEMBLY_TYPE_GRID_N_1) {
          popularData = infoBto.getAppInfoList();
        }
        if (infoBto.getType() == GlobalConstants.ASSEMBLY_TYPE_LIST_1_N) {
          appListAssemblyInfo = infoBto;
          appListData = infoBto.getAppInfoList();
        }
      }
    }
  }
  private void initViews() {
    LayoutInflater lif = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View flowBar = lif.inflate(R.layout.oc_flow_bar, xlvAppInfos, false);
    vpFlowPager = (ViewPager) flowBar.findViewById(R.id.vp_pager);
    mCirclePageIndicator = (CirclePageIndicator) flowBar.findViewById(R.id.cpi_indicator);
    View popluarApppInfoView = lif.inflate(R.layout.oc_popular_app_info, xlvAppInfos, false);
    popularListView = (HorizontalListView) popluarApppInfoView.findViewById(R.id.hlv_popular_app);
    xlvAppInfos.addHeaderView(flowBar);
    xlvAppInfos.addHeaderView(popluarApppInfoView);
    xlvAppInfos.setOverScrollMode(View.OVER_SCROLL_NEVER);
    TextView edit = (TextView) contentView.findViewById(R.id.home_search_edit);
    edit.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        Intent i = new Intent();
        i.setClass(getActivity().getApplicationContext(), SearchAppActivity.class);
        startActivity(i);
      }
    });
    
    ImageView icSearch = (ImageView)contentView.findViewById(R.id.home_search_icon);
    icSearch.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        Intent i = new Intent();
        i.setClass(getActivity().getApplicationContext(), SearchAppActivity.class);
        startActivity(i);
      }
    });
    
  }
}
