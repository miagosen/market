package com.lx.market.activity;

import java.util.ArrayList;
import java.util.List;

import market.lx.com.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lx.market.MarketApplication;
import com.lx.market.adapter.AppListAdapter;
import com.lx.market.adapter.FlowBarAdapter;
import com.lx.market.adapter.PopularAppInfoAdapter;
import com.lx.market.adapter.TabPageIndicatorAdapter;
import com.lx.market.constants.GlobalConstants;
import com.lx.market.fragment.BaseFragment;
import com.lx.market.fragment.DownloadFragment;
import com.lx.market.fragment.MyFavoriteFragment;
import com.lx.market.fragment.NewAppsFragment;
import com.lx.market.fragment.TopListFragment;
import com.lx.market.fragment.UpdateAppsManagerFragment;
import com.lx.market.listener.OnViewChangeListener;
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
import com.lx.market.ui.widget.HorizontalListView;
import com.lx.market.ui.widget.ScrollLayout;
import com.lx.market.utils.AppInfoUtils;
import com.lx.market.utils.DialogUtils;
import com.lx.market.utils.Logger;
import com.lx.market.utils.PhoneInfoUtils;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by Antikvo.Miao on 2014/7/23.
 */
public class HomeActivity extends BaseActivity implements OnViewChangeListener, View.OnTouchListener {

  /*------------------ views ------------------*/
  /* =====================推荐页========================== */
  /**
   * 中间热门推广
   */
  // private PopularAppInfoLayout popularAppInfoLayout;
  private HorizontalListView      popularListView;
  private ScrollLayout            mainScrollLayout;
  /**
   * 推荐页1*N列表
   */
  private ListView                xlvAppInfos;
  /**
   * 圆点
   */
  private CirclePageIndicator     mCirclePageIndicator;
  /**
   * 加载进度显示
   */
  private ProgressBar             pbLoding;
  /**
   * 顶部轮播
   */
  private ViewPager               vpFlowPager;
  /**
   * 首页（推荐页）
   */
  private PullToRefreshListView   pullToRefreshListView;
  /**
   * 搜索
   */
  private ImageButton             ibtnSearch;
  /**
   * 设置
   */
  private ImageButton             ibtnSetting;

  /* =====================推荐页========================== */
  /* =====================逛逛页========================== */
  private TabPageIndicator        tbiStroll;
  private ViewPager               vpStroll;
  /* =====================逛逛页========================== */

  /* =====================我的页========================== */
  private TabPageIndicator        tbiMine;
  private ViewPager               vpMine;
  /* =====================我的页========================== */
  private Dialog                  loadingDialog;
  private RadioButton             rbRecommend;
  private RadioButton             rbStroll;
  private RadioButton             rbPersonal;
  /*------------------ views ------------------*/

  /*------------------ constants ------------------*/
  public static final int         HOME_PAGE   = 0;
  public static final int         SCEND_PAGE  = 1;
  public static final int         THREED_PAGE = 2;
  /*------------------ constants ------------------*/

  private TabPageIndicatorAdapter strollTabPageIndicatorAdapter;
  private TabPageIndicatorAdapter mineTabPageIndicatorAdapter;
  private FlowBarAdapter          flowBarAdapter;
  private PopularAppInfoAdapter   popularAppInfoAdapter;
  private AppListAdapter          appListAdapter;
  private int                     currentScreen;                            //

  private ArrayList<BaseFragment> strollFragments;
  private ArrayList<BaseFragment> mineFragments;

  /*------------------ data ------------------*/

  private List<AppInfoBto>        flowBarData = new ArrayList<AppInfoBto>();
  private List<AppInfoBto>        popularData = new ArrayList<AppInfoBto>();
  private List<AppInfoBto>        appListData = new ArrayList<AppInfoBto>();
  // 应用列表栏目
  private AssemblyInfoBto         appListAssemblyInfo;
  private String                  marketId    = "";

  /*------------------ data ------------------*/

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // setContentView(R.layout.oc_home_activity);
    findViews();
    initView();
    loadData();
    initAdapter();
    initPageScroll();
    initActionBar();
  }

  private void initView() {
    LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View flowBar = lif.inflate(R.layout.oc_flow_bar, xlvAppInfos, false);
    vpFlowPager = (ViewPager) flowBar.findViewById(R.id.vp_pager);
    mCirclePageIndicator = (CirclePageIndicator) flowBar.findViewById(R.id.cpi_indicator);
    View popluarApppInfoView = lif.inflate(R.layout.oc_popular_app_info, xlvAppInfos, false);
    // popularAppInfoLayout = (PopularAppInfoLayout)
    // popluarApppInfoView.findViewById(R.id.popular_app_info_layout);
    popularListView = (HorizontalListView) popluarApppInfoView.findViewById(R.id.hlv_popular_app);
    xlvAppInfos.addHeaderView(flowBar);
    xlvAppInfos.addHeaderView(popluarApppInfoView);
  }

  private void findViews() {
    mainScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayout);
    pbLoding = (ProgressBar) findViewById(R.id.pb_loading_view);
    pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_home_page);
    xlvAppInfos = pullToRefreshListView.getRefreshableView();
    // ibtnSearch = (ImageButton) findViewById(R.id.action_bar_search);
    // ibtnSetting = (ImageButton) findViewById(R.id.action_bar_setting);
    tbiStroll = (TabPageIndicator) findViewById(R.id.stroll_tpi_indicator);
    vpStroll = (ViewPager) findViewById(R.id.vp_stroll);
    // tbiMine = (TabPageIndicator) findViewById(R.id.mine_tpi_indicator);
    // vpMine = (ViewPager) findViewById(R.id.vp_mine);
    rbRecommend = (RadioButton) findViewById(R.id.rb_recommend);
    rbStroll = (RadioButton) findViewById(R.id.rb_stroll);
    rbPersonal = (RadioButton) findViewById(R.id.rb_personal);
    // mainScrollLayout = (ScrollLayout) findViewById(R.id.main_scrolllayout);
  }

  private void setChannelMy(ChannelInfoBto channelInfoBto) {
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

  private void initAdapter() {
    strollTabPageIndicatorAdapter = null;// new
                                         // TabPageIndicatorAdapter(2,getSupportFragmentManager(),
                                         // getStrollFragments());
    mineTabPageIndicatorAdapter = null;// new
                                       // TabPageIndicatorAdapter(2,getSupportFragmentManager(),
                                       // getMineFragments());
    vpStroll.setAdapter(strollTabPageIndicatorAdapter);
    vpMine.setAdapter(mineTabPageIndicatorAdapter);
    tbiStroll.setViewPager(vpStroll);
    tbiMine.setViewPager(vpMine);
    flowBarAdapter = new FlowBarAdapter(this, flowBarData);
    vpFlowPager.setAdapter(flowBarAdapter);
    mCirclePageIndicator.setViewPager(vpFlowPager, 1);
    popularAppInfoAdapter = new PopularAppInfoAdapter(this, popularData);
    popularListView.setAdapter(popularAppInfoAdapter);
    appListAdapter = new AppListAdapter(this, appListData);
    appListAdapter.setDownlaodRefreshHandle();
    xlvAppInfos.setAdapter(appListAdapter);
    xlvAppInfos.setOnItemClickListener(appListAdapter);
    xlvAppInfos.setFocusable(true);
    mainScrollLayout.setOnViewChangeListener(this);
    pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    pullToRefreshListView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
      @Override
      public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
        onLoadMore();
      }
    });
  }

  protected void loadData() {
    loadingDialog = DialogUtils.createWaitingDialog(this, R.string.dlg_waiting);
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
            } else if (i == SCEND_PAGE) {
              setChannelStroll(channelInfoBtos.get(SCEND_PAGE));
            } else if (i == THREED_PAGE) {
              setChannelMy(channelInfoBtos.get(THREED_PAGE));
            }
          }
        }
      } catch (Exception e) {
        Logger.p(e);
      }
    }
    loadingDialog.dismiss();
  }

  public void onLoadMore() {
    if (appListAssemblyInfo != null) {
      GetApkListByPageReq req = new GetApkListByPageReq();
      req.setScreenHeight(PhoneInfoUtils.getPhoneScreenHeight(this));
      req.setScreenWidth(PhoneInfoUtils.getPhoneScreenWidth(this));
      req.setAssemblyId(appListAssemblyInfo.getAssemblyId());
      req.setStart(appListAssemblyInfo.getAppInfoList().size() + 1);
      req.setInstallList(AppInfoUtils.getInstalledPageInfos(this));
      req.setUninstallList(AppInfoUtils.getUninstalledPageInfos());
      req.setFixedLength(20);
      req.setMarketId(marketId);
      OcHttpConnection httpConnection = new OcHttpConnection(this);
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
              }
            }
          } else {
            // HomeActivity.this.toast(getString(R.string.slow_network));
          }
          pullToRefreshListView.onRefreshComplete();
          appListAdapter.notifyDataSetChanged();
        }
      });
    }
  }
  private void initPageScroll() {
    xlvAppInfos.setOverScrollMode(View.OVER_SCROLL_NEVER);
    mainScrollLayout.setOverScrollMode(View.OVER_SCROLL_NEVER);
    rbRecommend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        currentScreen = HOME_PAGE;
        rbRecommend.setChecked(true);
        mainScrollLayout.scrollToScreen(HOME_PAGE);
      }
    });
    rbStroll.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        currentScreen = SCEND_PAGE;
        rbStroll.setChecked(true);
        mainScrollLayout.scrollToScreen(SCEND_PAGE);
      }
    });
    rbPersonal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        currentScreen = THREED_PAGE;
        rbPersonal.setChecked(true);
        mainScrollLayout.scrollToScreen(THREED_PAGE);
      }
    });
  }

  protected void initActionBar() {
    ibtnSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // enterActivity(SearchAppActivity.class, null);
      }
    });
    ibtnSetting.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // enterActivity(SettingActivity.class, null);

      }
    });
  }

  @Override
  public void OnViewChange(int view) {
    currentScreen = mainScrollLayout.getCurScreen();
    switch (view) {
    case HOME_PAGE:
      rbRecommend.setChecked(true);
      break;
    case SCEND_PAGE:
      rbStroll.setChecked(true);
      onLoadStrollData();
      break;
    case THREED_PAGE:
      rbPersonal.setChecked(true);
      break;
    }
  }

  private void onLoadStrollData() {

  }

  @Override
  public boolean onTouch(View v, MotionEvent paramMotionEvent) {
    if ((paramMotionEvent.getAction() == MotionEvent.ACTION_MOVE) || (paramMotionEvent.getAction() == MotionEvent.ACTION_DOWN)) {
      v.getParent().requestDisallowInterceptTouchEvent(true);
    } else if ((paramMotionEvent.getAction() == MotionEvent.ACTION_CANCEL) || (paramMotionEvent.getAction() == MotionEvent.ACTION_UP)) {
      v.getParent().requestDisallowInterceptTouchEvent(false);
    }
    return super.onTouchEvent(paramMotionEvent);
  }

  public ArrayList<BaseFragment> getStrollFragments() {
    return strollFragments;
  }

  private void setChannelStroll(ChannelInfoBto channelInfoBto) {
    if (strollFragments == null && channelInfoBto != null) {
      strollFragments = new ArrayList<BaseFragment>();

      for (int i = 0; i < channelInfoBto.getTopicList().size(); i++) {
        TopicInfoBto topicInfo = channelInfoBto.getTopicList().get(i);
        if (i == 0) {
          TopListFragment topListFragment = new TopListFragment();
          // topListFragment.setTopicInfoBto(topicInfo);
          strollFragments.add(topListFragment);
        } else if (i == 1) {
          NewAppsFragment newAppsFragment = new NewAppsFragment();
          // newAppsFragment.setTopicInfoBto(topicInfo);
          strollFragments.add(newAppsFragment);
        } else if (i == 2) {
          MyFavoriteFragment myFavoriteFragment = new MyFavoriteFragment();
          // myFavoriteFragment.setTopicInfoBto(topicInfo);
          strollFragments.add(myFavoriteFragment);
        }
      }
    }
  }

  public ArrayList<BaseFragment> getMineFragments() {
    if (mineFragments == null) {
      mineFragments = new ArrayList<BaseFragment>();
      DownloadFragment downloadFragment = new DownloadFragment();
      mineFragments.add(downloadFragment);
      UpdateAppsManagerFragment updateAppsManagerFragment = new UpdateAppsManagerFragment();
      mineFragments.add(updateAppsManagerFragment);
    }
    return mineFragments;
  }
}
