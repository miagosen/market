package com.lx.market.fragment;

import java.util.ArrayList;
import java.util.List;

import market.lx.com.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lx.market.MarketApplication;
import com.lx.market.adapter.AppListAdapter;
import com.lx.market.constants.GlobalConstants;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.connection.OcHttpConnection;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.model.AssemblyInfoBto;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.model.TopicInfoBto;
import com.lx.market.network.protocol.GetApkListByPageReq;
import com.lx.market.network.protocol.GetApkListByPageResp;
import com.lx.market.network.protocol.GetTopicReq;
import com.lx.market.network.protocol.GetTopicResp;
import com.lx.market.network.utils.TerminalInfoUtil;
import com.lx.market.utils.AppInfoUtils;
import com.lx.market.utils.PhoneInfoUtils;

/**
 * Created by Antikvo.Miao on 2014/8/24.
 */
public class TopListFragment extends BaseFragment {
  private View                  view;
  private PullToRefreshListView xlvTopList;
  private AppListAdapter        appListAdapter;
  private List<AppInfoBto>      appInfoBtos = new ArrayList<AppInfoBto>();
  private LinearLayout          ll_loading_view;
  private TextView              tvEmpty;
  private static final int      PAGE_SIZE   = 20;
  private GetTopicResp          resp;
  private TopicInfoBto          topicInfoBto;
  
  

  public void setTopicInfoBto(TopicInfoBto topicInfoBto) {
	this.topicInfoBto = topicInfoBto;
}

@Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    loadingData();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (view == null) {
      view = inflater.inflate(R.layout.top_list_fragment, container, false);
      xlvTopList = (PullToRefreshListView) view.findViewById(R.id.xlv_top_list);
      ll_loading_view = (LinearLayout) view.findViewById(R.id.ll_loading_view);
      tvEmpty = (TextView) view.findViewById(R.id.tv_empty);
      appListAdapter = new AppListAdapter(mContext, appInfoBtos);
      xlvTopList.setAdapter(appListAdapter);
      xlvTopList.setOnItemClickListener(appListAdapter);
      xlvTopList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
      xlvTopList.setOverScrollMode(View.OVER_SCROLL_NEVER);
      ListView listView = xlvTopList.getRefreshableView();
      listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
      xlvTopList.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
        @Override
        public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
          onLoadMore();
        }
      });
      ll_loading_view.setVisibility(View.VISIBLE);
    } else {
      ViewGroup p = (ViewGroup) view.getParent();
      if (p != null) {
        p.removeAllViewsInLayout();
      }
    }
    return view;
  }

  private void loadingData() {
    if (topicInfoBto != null) {
      GetTopicReq req = new GetTopicReq();
      req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(mContext));
      req.setStart(0);
      req.setTopicId(topicInfoBto.getTopicId());
      req.setInstallList(AppInfoUtils.getInstalledPageInfos(mContext));
      req.setUnInstallList(AppInfoUtils.getUninstalledPageInfos());
      req.setFixedLength(0);
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
                appInfoBtos.addAll(resp.getAssemblyList().get(0).getAppInfoList());
              }
            }
          } else {
            xlvTopList.setEmptyView(tvEmpty);
          }
          appListAdapter.notifyDataSetChanged();
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
                  // xlvTopList.stopLoadMore(false);
                } else {
                  // xlvTopList.stopLoadMore(true);
                }
              } else {
                // xlvTopList.stopLoadMore(false);
              }
            } else {
              // xlvTopList.stopLoadMore(true);
            }
          }
        });
      }
    }
  }
}
