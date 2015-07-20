package com.lx.market.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lx.market.MarketApplication;
import com.lx.market.constants.BundleConstants;
import com.lx.market.constants.GlobalConstants;
import com.lx.market.download.AppManagerCenter;
import com.lx.market.download.UIDownLoadListener;
import com.lx.market.model.DownloadInfo;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.connection.OcHttpConnection;
import com.lx.market.network.model.AppDetailInfoBto;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.protocol.GetApkDetailReq;
import com.lx.market.network.protocol.GetApkDetailResp;
import com.lx.market.network.protocol.GetRecommendAppReq;
import com.lx.market.network.protocol.GetRecommendAppResp;
import com.lx.market.network.utils.TerminalInfoUtil;
import com.lx.market.ui.widget.ProgressButton;
import com.lx.market.utils.DensityUtil;
import com.lx.market.utils.FileUtils;
import com.lx.market.utils.Logger;
import com.lx.market.utils.UIUtils;

import java.util.List;

import market.lx.com.R;

/**
 * Created by yys on 2014/7/28.
 */
@ContentView(R.layout.oc_app_detail_activity)
public class AppDetailActivity extends BaseActivity {
  public final static String Tag                    = "AppDetailActivity";
  public final static int    REFRESH_DETAIL_VIEW    = 0;
  public final static int    REFRESH_RECOMMEND_VIEW = 1;
  public final static String DETAIL_APPID           = "appid";
  public final static String DETAIL_BRIEFDESC       = "briefdesc";
  public final static String DETAIL_LABEL           = "label";

  @ViewInject(R.id.ib_app_detail_btn_back)
  private ImageButton        actionBarBack;

  @ViewInject(R.id.detail_content)
  private RelativeLayout     detail_content;

  @ViewInject(R.id.pb_loading_view)
  private ProgressBar        pb_loading_view;

  @ViewInject(R.id.app_imgae_icon)
  private ImageView          appIcon;

  @ViewInject(R.id.detail_app_name)
  private TextView           appName;

  @ViewInject(R.id.detail_app_size)
  private TextView           appSize;

  @ViewInject(R.id.detail_people)
  private TextView           appPeople;

  @ViewInject(R.id.app_image_scroll)
  private LinearLayout       imagelin;

  @ViewInject(R.id.detail_preference_content)
  private LinearLayout       preferencelin;

  @ViewInject(R.id.detail_description)
  private TextView           appDescription;

  @ViewInject(R.id.app_download_btn)
  private ProgressButton     appDownload;

  private GetApkDetailResp   detailResp;
  private List<AppInfoBto>   recommendlist          = null;
  private String             label;
  private AppInfoBto         info;
  public static final String APP_DETAIL_IMAGES      = "app_detail_images";
  private String[]           imgs                   = new String[0];

  private final Handler      mHandler               = new Handler() {
                                                      @Override
                                                      public void handleMessage(Message msg) {
                                                        switch (msg.what) {
                                                        case REFRESH_DETAIL_VIEW:
                                                          refreshDetailView(detailResp.getAppDetailInfo());
                                                          break;
                                                        case REFRESH_RECOMMEND_VIEW:
                                                          refreshDetailRecommedView();
                                                          break;
                                                        }
                                                        super.handleMessage(msg);
                                                      }
                                                    };
  private UIDownLoadListener uiDownLoadListener     = null;

  public AppDetailActivity() {
    uiDownLoadListener = new UIDownLoadListener() {
      @Override
      public void onRefreshUI(String pkgName) {
        appDownload.onUpdateButton();
      }
    };
    setDownlaodRefreshHandle();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    ViewUtils.inject(this);
    Intent intent = this.getIntent();
    info = (AppInfoBto) intent.getSerializableExtra(BundleConstants.BUNDLE_APP_INFO_BTO);
    initView(info);
    initProgreesButton(info);
    actionBarBack.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  private void initProgreesButton(AppInfoBto appInfoBto) {
    appDownload.setAppInfoBto(appInfoBto);
    appDownload.onUpdateButton();
    appDownload.setOnClickListener(new OnProgreesButonClickListener());
  }

  private class OnProgreesButonClickListener implements OnClickListener {

    @Override
    public void onClick(View v) {
      int state = AppManagerCenter.getAppDownlaodState(info.getPackageName(), info.getVersionCode());

      Log.e("--------", "-----onClick--state---" + state);

      switch (state) {
      /* 已經下載完，未安裝 */
      case AppManagerCenter.APP_STATE_DOWNLOADED:
        Log.e("--------", "-----onClick--downloaded---");
        AppManagerCenter.installApk(new DownloadInfo(info, GlobalConstants.POSITION_HOME_APP_LIST));
        break;
      /* 已經安裝 */
      case AppManagerCenter.APP_STATE_INSTALLED:
        Log.e("--------", "-----onClick--installed---");
        UIUtils.startApp(info);
        break;
      /**
       * 应用下载被暂停
       */
      case AppManagerCenter.APP_STATE_DOWNLOAD_PAUSE:
        Log.e("--------", "-----onClick--pause---");
        UIUtils.downloadApp(new DownloadInfo(info, GlobalConstants.POSITION_HOME_APP_LIST));
        Log.e("--------", "-----onClick--pause--after-");
        break;
      /**
       * 应用不存在
       */
      case AppManagerCenter.APP_STATE_UNEXIST:
        UIUtils.downloadApp(new DownloadInfo(info, GlobalConstants.POSITION_HOME_APP_LIST));
        break;
      /**
       * 应用需要更新
       */
      case AppManagerCenter.APP_STATE_UPDATE:
        UIUtils.downloadApp(new DownloadInfo(info, GlobalConstants.POSITION_HOME_APP_LIST));
        break;
      /* 正在下載 */
      case AppManagerCenter.APP_STATE_DOWNLOADING:
        AppManagerCenter.pauseDownload(new DownloadInfo(info, GlobalConstants.POSITION_HOME_APP_LIST), true);
        break;
      /**
       * 应用等待下载
       */
      case AppManagerCenter.APP_STATE_WAIT:
        Log.e("--------", "-----onClick--wait---");
        UIUtils.downloadApp(new DownloadInfo(info, GlobalConstants.POSITION_HOME_APP_LIST));
        Log.e("--------", "-----onClick--wait--after-");
        break;
      }
    }

  }

  public void initView(AppInfoBto info) {
    if (info == null)
      return;
    AppDetailInfoBto dbinfo = null;
    try {
      dbinfo = MarketApplication.getInstance().dbUtils.findFirst(Selector.from(AppDetailInfoBto.class).where(WhereBuilder.b("appid", "=", info.getRefId())));
    } catch (Exception ext) {

    }

    if (dbinfo == null) {
      pb_loading_view.setVisibility(View.VISIBLE);
      detail_content.setVisibility(View.INVISIBLE);
    } else {
      refreshDetailView(dbinfo);
    }
    sendDetailReq(info.getRefId());
    // sendDetailRecommendReq(appid,label);
  }

  public void sendDetailRecommendReq(final int appid, String label) {
    final GetRecommendAppReq req = new GetRecommendAppReq();
    OcHttpConnection httpConnection = new OcHttpConnection(this);
    req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(this));
    req.setResId(appid);
    req.setLabel(label);
    httpConnection.sendRequest(RequestType.MARKET_DATA, req, GetRecommendAppResp.class, new OcHttpReqCallBack() {
      @Override
      public void onResponse(boolean result, Object respOrMsg) {
        if (result) {
          GetRecommendAppResp resp = (GetRecommendAppResp) respOrMsg;
          if (resp.getErrorCode() == 0) {
            Logger.debug(resp.toString());
            try {
              recommendlist = resp.getAppList();
              Message msg = Message.obtain(mHandler, REFRESH_RECOMMEND_VIEW);
              msg.sendToTarget();
            } catch (Exception e) {
              Logger.error("" + e);
            }
          } else {
            Logger.error(detailResp.getErrorMessage());
          }

        } else {
          Logger.error("GetStartPageReq error:" + respOrMsg);
        }
      }
    });
  }

  public void sendDetailReq(final int appid) {
    final GetApkDetailReq req = new GetApkDetailReq();
    OcHttpConnection httpConnection = new OcHttpConnection(this);
    req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(this));
    req.setAppid(appid);
    httpConnection.sendRequest(RequestType.MARKET_DATA, req, GetApkDetailResp.class, new OcHttpReqCallBack() {
      @Override
      public void onResponse(boolean result, Object respOrMsg) {
        if (result) {
          detailResp = (GetApkDetailResp) respOrMsg;
          if (detailResp.getErrorCode() == 0) {
            Logger.debug(detailResp.toString());
            try {
              AppDetailInfoBto appdetail = detailResp.getAppDetailInfo();
              if (appdetail != null) {
                MarketApplication.getInstance().dbUtils.delete(AppDetailInfoBto.class, WhereBuilder.b("appid", "=", appid));
                MarketApplication.getInstance().dbUtils.save(appdetail);
              }
              Message msg = Message.obtain(mHandler, REFRESH_DETAIL_VIEW);
              msg.sendToTarget();
            } catch (DbException e) {
              e.printStackTrace();
            }
          } else {
            Logger.error(detailResp.getErrorMessage());
          }

        } else {
          Logger.error("GetStartPageReq error:" + respOrMsg);
        }
      }
    });
  }

  public void refreshDetailView(AppDetailInfoBto info) {

    pb_loading_view.setVisibility(View.INVISIBLE);
    detail_content.setVisibility(View.VISIBLE);
    if (info != null) {
      String imgstr = info.getShotImg();
      if (!TextUtils.isEmpty(imgstr)) {
        imgs = imgstr.split(",");
      }
      int imagew = DensityUtil.dip2px(this, 160);
      int imageh = DensityUtil.dip2px(this, 266);
      int margin = DensityUtil.dip2px(this, 6);

      for (int i = 0; i < imgs.length; i++) {
        ImageView image = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imagew, imageh);
        layoutParams.setMargins(margin, 0, margin, 0);
        image.setBackgroundResource(R.drawable.oc_detail_image_bg);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        MarketApplication.getInstance().imageLoader.displayImage(imgs[i].trim(), image);
        image.setLayoutParams(layoutParams);
        imagelin.addView(image);
        image.setOnClickListener(new OnClickListener() {

          @Override
          public void onClick(View v) {
            Intent intent = new Intent(AppDetailActivity.this, AppDetailImageActivity.class);
            intent.putExtra(APP_DETAIL_IMAGES, imgs);
            startActivity(intent);
          }
        });
      }

      appName.setText(info.getApkName());
      appSize.setText(FileUtils.getFileSizeString(info.getFileSize()));
      appPeople.setText(info.getDownNum() + "人");
      appDescription.setText(info.getDesc());
      MarketApplication.getInstance().imageLoader.displayImage(info.getIconUrl().trim(), appIcon);
      sendDetailRecommendReq(info.getAppid(), info.getLabel());
    }

  }

  public void refreshDetailRecommedView() {
    if (recommendlist != null) {
      for (int i = 0; i < recommendlist.size(); i++) {
        final AppInfoBto info = recommendlist.get(i);
        TextView icon = (TextView) preferencelin.getChildAt(i);
        icon.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent i = new Intent();
            i.setClass(AppDetailActivity.this, AppDetailActivity.class);
            i.putExtra(BundleConstants.BUNDLE_APP_INFO_BTO, info);
            AppDetailActivity.this.startActivity(i);
          }
        });
        icon.setText(info.getName());
        // icon.getDrawingCache()

        // Bitmap bitmap = icon.getDrawingCache();
        // Drawable nav_up=getResources().getDrawable(R.drawable.button_nav_up);
        // nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
        // nav_up.getMinimumHeight());
        // textview1.setCompoundDrawables(null, null, nav_up, null);

        // MarketApplication.getInstance().imageLoader.displayImage(info.getImgUrl().trim(),
        // (ImageAware) icon);
      }
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  /**
   * 取消 下载监听, Activity OnDestroy 时调用
   */
  public void removeDownLoadHandler() {
    AppManagerCenter.removeDownloadRefreshHandle(uiDownLoadListener);
  }

  /**
   * 设置刷新handler,Activity OnResume 时调用
   */
  public void setDownlaodRefreshHandle() {
    AppManagerCenter.setDownloadRefreshHandle(uiDownLoadListener);
  }
}
