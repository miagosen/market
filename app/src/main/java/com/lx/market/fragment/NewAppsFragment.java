package com.lx.market.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lx.market.MarketApplication;
import com.lx.market.activity.SearchAppActivity;
import com.lx.market.adapter.AppListAdapter;
import com.lx.market.constants.GlobalConstants;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.connection.OcHttpConnection;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.model.AssemblyInfoBto;
import com.lx.market.network.model.ChannelInfoBto;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.model.TopicInfoBto;
import com.lx.market.network.protocol.GetApkListByPageReq;
import com.lx.market.network.protocol.GetApkListByPageResp;
import com.lx.market.network.protocol.GetMarketFrameResp;
import com.lx.market.network.protocol.GetTopicReq;
import com.lx.market.network.protocol.GetTopicResp;
import com.lx.market.network.utils.TerminalInfoUtil;
import com.lx.market.utils.AppInfoUtils;
import com.lx.market.utils.Logger;
import com.lx.market.utils.PhoneInfoUtils;

import java.util.ArrayList;
import java.util.List;

import market.lx.com.R;

/**
 * Created by Antikvo.Miao on 2014/8/28.
 */
public class NewAppsFragment extends BaseFragment {
  private View                  contentView;
  private PullToRefreshListView lvNewApps;
  private AppListAdapter        appListAdapter;
  private List<AppInfoBto>      appInfoBtos = new ArrayList<AppInfoBto>();
  private static final int      PAGE_SIZE   = 20;
  private LinearLayout          ll_loading_view;
  private TextView              tvEmpty;
  private GetTopicResp          resp;
  private ListView              listView;
  private TopicInfoBto          topicInfoBto;
  public static final int SCEND_PAGE = 1;
  public Handler mHandler = new Handler(){

    @Override
    public void handleMessage(Message msg) {
      appListAdapter.notifyDataSetChanged();
      ll_loading_view.setVisibility(View.GONE);
      super.handleMessage(msg);
    }};

  public void setTopicInfoBto(TopicInfoBto topicInfoBto) {
    this.topicInfoBto = topicInfoBto;
  }
  
  private void initTopicInfoBto(){
    GetMarketFrameResp resp = MarketApplication.getInstance().marketFrameResp;
    if (resp != null) {
      try {
        
        List<ChannelInfoBto> channelInfoBtos = resp.getChannelList();
        if (channelInfoBtos != null && channelInfoBtos.size() > 0) {
          for (int i = 0; i < channelInfoBtos.size(); i++) {
            if (i == SCEND_PAGE) {
              ChannelInfoBto channelInfoBto = channelInfoBtos.get(SCEND_PAGE);
              if(channelInfoBto != null){
                for (int j = 0; j < channelInfoBto.getTopicList().size(); j++) {
                  TopicInfoBto topicInfo = channelInfoBto.getTopicList().get(j);
                    this.topicInfoBto =topicInfo;
                }
              }
            }
          }
        }
      } catch (Exception e) {
        Logger.p(e);
      }
    }
  }
  

@Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initTopicInfoBto();
    loadingData();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (contentView == null) {
      contentView = inflater.inflate(R.layout.oc_new_apps_fragment, container, false);
      lvNewApps = (PullToRefreshListView) contentView.findViewById(R.id.xlv_new_apps);
      ll_loading_view = (LinearLayout) contentView.findViewById(R.id.ll_loading_view);
      tvEmpty = (TextView) contentView.findViewById(R.id.tv_empty);
      TextView edit = (TextView) contentView.findViewById(R.id.new_search_edit);
      edit.setOnClickListener(new OnClickListener() {
        
        @Override
        public void onClick(View v) {
          Intent i = new Intent();
          i.setClass(getActivity().getApplicationContext(), SearchAppActivity.class);
          startActivity(i);
        }
      });
      
      ImageView icSearch = (ImageView)contentView.findViewById(R.id.new_search_icon);
      icSearch.setOnClickListener(new OnClickListener() {
        
        @Override
        public void onClick(View v) {
          Intent i = new Intent();
          i.setClass(getActivity().getApplicationContext(), SearchAppActivity.class);
          startActivity(i);
        }
      });
      appListAdapter = new AppListAdapter(getActivity(), appInfoBtos);
    } else {
      ViewGroup p = (ViewGroup) contentView.getParent();
      if (p != null) {
        p.removeAllViewsInLayout();
      }
    }
    lvNewApps.setOnItemClickListener(appListAdapter);
    lvNewApps.setOverScrollMode(View.OVER_SCROLL_NEVER);
    lvNewApps.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    lvNewApps.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
      @Override
      public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
        onLoadMore();
      }
    });
    listView = lvNewApps.getRefreshableView();
    listView.setAdapter(appListAdapter);
    listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    listView.setEmptyView(tvEmpty);
    return contentView;
  }

  private void loadingData() {
    ll_loading_view.setVisibility(View.VISIBLE);

    if (topicInfoBto != null) {
      GetTopicReq req = new GetTopicReq();
      req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(mContext));
      req.setStart(0);
      req.setTopicId(topicInfoBto.getTopicId());
      req.setInstallList(AppInfoUtils.getInstalledPageInfos(mContext));
      req.setUnInstallList(AppInfoUtils.getUninstalledPageInfos());
      req.setFixedLength(PAGE_SIZE);
      OcHttpConnection http = new OcHttpConnection(mContext);
      http.sendRequest(RequestType.MARKET_DATA, req, GetTopicResp.class, new OcHttpReqCallBack() {
        @Override
        public void onResponse(boolean result, Object respOrMsg) {
          ll_loading_view.setVisibility(View.GONE);
          if (result) {
            resp = (GetTopicResp) respOrMsg;
            if (resp.getErrorCode() == 0) {
              if (resp.getAssemblyList() != null && resp.getAssemblyList().size() > 0 && resp.getAssemblyList().get(0).getAppInfoList() != null
                  && resp.getAssemblyList().get(0).getAppInfoList().size() > 0) {
                for (AppInfoBto appInfo : resp.getAssemblyList().get(0).getAppInfoList()) {
                  if (!appInfoBtos.contains(appInfo)) {
                    appInfoBtos.add(appInfo);
                  }
                }
                if (PAGE_SIZE > resp.getAssemblyList().get(0).getAppInfoList().size()) {
                  // lvNewApps.stopLoadMore(false);
                } else {
                  // lvNewApps.stopLoadMore(true);
                }
              }

            }
          } else {
            // lvNewApps.stopLoadMore(false);
          }
//          appListAdapter.notifyDataSetChanged();
          mHandler.sendEmptyMessage(0);
        }
      });
    }
  }

  public void onLoadMore() {
    if (resp != null && resp.getAssemblyList() != null && resp.getAssemblyList().size() > 0) {
      List<AssemblyInfoBto> assemblyInfoBtos = resp.getAssemblyList();
      if (assemblyInfoBtos.get(0).getType() == GlobalConstants.ASSEMBLY_TYPE_LIST_1_N && MarketApplication.getInstance().marketFrameResp != null) {
        GetApkListByPageReq req = new GetApkListByPageReq();
        req.setMarketId(MarketApplication.getInstance().marketFrameResp.getMarketId());
        req.setInstallList(AppInfoUtils.getInstalledPageInfos(mContext));
        req.setUninstallList(AppInfoUtils.getUninstalledPageInfos());
        req.setStart(appInfoBtos.size());
        req.setFixedLength(PAGE_SIZE);
        req.setScreenHeight(PhoneInfoUtils.getPhoneScreenHeight(mContext));
        req.setAssemblyId(assemblyInfoBtos.get(0).getAssemblyId());
        OcHttpConnection httpConnection = new OcHttpConnection(mContext);
        httpConnection.sendRequest(RequestType.MARKET_DATA, req, GetApkListByPageResp.class, new OcHttpReqCallBack() {
          @Override
          public void onResponse(boolean result, Object respOrMsg) {
            if (result) {
              GetApkListByPageResp resp = (GetApkListByPageResp) respOrMsg;
              if (resp.getErrorCode() == 0 && resp.getAppList() != null && resp.getAppList().size() > 0) {
                for (AppInfoBto appInfo : resp.getAppList()) {
                  if (!appInfoBtos.contains(appInfo)) {
                    appInfoBtos.add(appInfo);
                  }
                }
                if (PAGE_SIZE > resp.getAppList().size()) {
                  // lvNewApps.stopLoadMore(false);
                } else {
                  // lvNewApps.stopLoadMore(true);
                }
              } else {
                // lvNewApps.stopLoadMore(false);
              }
            } else {
              // lvNewApps.stopLoadMore(true);
            }
            lvNewApps.onRefreshComplete();
          }
        });
      }
    }
  }
}
