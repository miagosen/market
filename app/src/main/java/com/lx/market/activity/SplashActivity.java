package com.lx.market.activity;

import market.lx.com.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lx.market.MarketApplication;
import com.lx.market.model.ClientInfo;
import com.lx.market.model.StartPageInfo;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.connection.OcHttpConnection;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.protocol.GetApkSelfUpdateReq;
import com.lx.market.network.protocol.GetApkSelfUpdateResp;
import com.lx.market.network.protocol.GetMarketFrameReq;
import com.lx.market.network.protocol.GetMarketFrameResp;
import com.lx.market.network.protocol.GetStartPageReq;
import com.lx.market.network.protocol.GetStartPageResp;
import com.lx.market.network.utils.TerminalInfoUtil;
import com.lx.market.service.FloatWindowService;
import com.lx.market.utils.AppInfoUtils;
import com.lx.market.utils.Logger;
import com.lx.market.utils.PreferenceUtil;

@ContentView(R.layout.oc_splash)
public class SplashActivity extends Activity {
  @ViewInject(R.id.iv_splash)
  private ImageView     ivSplash;
  private StartPageInfo startPageInfo;
  private long          stayTime = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    startService(new Intent(SplashActivity.this, FloatWindowService.class));
    ClientInfo.initClientInfo();
    stayTime = System.currentTimeMillis();
    ViewUtils.inject(this);
    try {
      startPageInfo = MarketApplication.getInstance().dbUtils.findFirst(StartPageInfo.class);
      if (startPageInfo != null) {
        MarketApplication.getInstance().imageLoader.displayImage(startPageInfo.getImageUrl(), ivSplash);
      } else {
        MarketApplication.getInstance().imageLoader.displayImage("drawable://" + R.drawable.oc_splash, ivSplash);
      }
    } catch (DbException e) {
      Logger.p(e);
    }
    if (ClientInfo.getAPNType(MarketApplication.curContext) == ClientInfo.NONET) {
      startActivity(new Intent(SplashActivity.this, NoneNetworkActivity.class));
      SplashActivity.this.finish();
    } else {
      checkUpdate();
      sendStartPageReq();
      loadData();
    }
  }

  public void doNegativeClick(String tag) {
    jumpToActivity(null);
  }

  protected void loadData() {
    GetMarketFrameReq req = new GetMarketFrameReq();
    req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(this));
    req.setInstallList(AppInfoUtils.getInstalledPageInfos(this));
    req.setUnInstallList(AppInfoUtils.getUninstalledPageInfos());
    OcHttpConnection http = new OcHttpConnection(this);
    http.sendRequest(RequestType.MARKET_DATA, req, GetMarketFrameResp.class, new OcHttpReqCallBack() {
      @Override
      public void onResponse(boolean result, Object respOrMsg) {
        if (result) {
          GetMarketFrameResp resp = (GetMarketFrameResp) respOrMsg;
          if (resp.getErrorCode() == 0) {
            jumpToActivity(resp);
          } else {
            jumpToActivity(null);
            Logger.error("GetMarketFrameReq error result = false" + resp.toString());
          }
        } else {
          jumpToActivity(null);
          Logger.error("GetMarketFrameReq error" + respOrMsg);
        }
      }
    });
  }

  public void jumpToActivity(final GetMarketFrameResp resp) {
    int delayTime = 0;
    if (System.currentTimeMillis() - stayTime < 1000) {
      delayTime = 1000;
    }
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        MarketApplication.getInstance().marketFrameResp = resp;
        Intent intent = new Intent();
        if (PreferenceUtil.getInstance().isFirstStart()) {
          // enterActivity(GuideActivity.class, null);
          PreferenceUtil.getInstance().setIsFirstStart(false);
        } else {
          // enterActivity(MainActivity.class, null);
          intent.setClass(SplashActivity.this, MainActivity.class);
        }
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
      }
    }, delayTime);
  }

  private void sendStartPageReq() {
    GetStartPageReq req = new GetStartPageReq();
    req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(this));
    OcHttpConnection http = new OcHttpConnection(this);
    http.sendRequest(RequestType.MARKET_DATA, req, GetStartPageResp.class, new OcHttpReqCallBack() {
      @Override
      public void onResponse(boolean result, Object respOrMsg) {
        if (result) {
          GetStartPageResp resp = (GetStartPageResp) respOrMsg;
          if (resp.getErrorCode() == 0) {
            Logger.debug(resp.toString());
            StartPageInfo info = new StartPageInfo();
            info.setId(1);
            info.setImageUrl(resp.getImgUrl());
            info.setText(resp.getText());
            MarketApplication.getInstance().imageLoader.loadImageSync(resp.getImgUrl());
            try {
              MarketApplication.getInstance().dbUtils.deleteAll(StartPageInfo.class);
              MarketApplication.getInstance().dbUtils.save(info);
            } catch (DbException e) {
              Logger.p(e);
            }
          } else {
            Logger.error(resp.getErrorMessage());
          }
        } else {
          Logger.error("GetStartPageReq error:" + respOrMsg);
        }
      }
    });
  }

  private void checkUpdate() {
    final GetApkSelfUpdateReq req = new GetApkSelfUpdateReq();
    OcHttpConnection httpConnection = new OcHttpConnection(this);
    req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(this));
    httpConnection.sendRequest(RequestType.UPDATE, req, GetApkSelfUpdateResp.class, new OcHttpReqCallBack() {
      @Override
      public void onResponse(boolean result, Object respOrMsg) {
        if (result) {
          GetApkSelfUpdateResp resp = (GetApkSelfUpdateResp) respOrMsg;
          if (resp.getErrorCode() == 0) {
            Logger.error("GetApkSelfUpdateResp:" + resp.toString());
            // TODO

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
