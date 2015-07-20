package com.lx.market.utils;

import java.io.File;

import market.lx.com.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.lx.market.MarketApplication;
import com.lx.market.download.AppManagerCenter;
import com.lx.market.model.ClientInfo;
import com.lx.market.model.DownloadInfo;
import com.lx.market.network.model.AppInfoBto;

/**
 * 实现UI的跳转，定义Activity之间跳转最简单接口 目前 重构之外的跳转代码分散到各个Activity中，这样重复劳动太多，容易出现错误
 */
public class UIUtils {

  /**
   * 启动游戏的方法
   * 
   * @param game
   */
  public static void startApp(AppInfoBto game) {
    if (null == game) {
      return;
    }
    startSingleGame(game.getPackageName());
  }

  public static void startApp(String packageName) {
    if (TextUtils.isEmpty(packageName)) {
      return;
    }
    startSingleGame(packageName);
  }

  private static void startSingleGame(String packageName) {
    Intent intent = MarketApplication.curContext.getPackageManager().getLaunchIntentForPackage(packageName);
    if (intent == null) {
      ToastUtils.showToast(R.string.toast_app_no_exits);
    } else {
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      MarketApplication.curContext.startActivity(intent);
    }
  }

  public static void gotoMarket(String packageName, String gameCode) {
    try {
      Uri marketUri = Uri.parse("market://details?id=" + packageName);
      Intent intent = new Intent();
      intent.setData(marketUri);
      MarketApplication.curContext.startActivity(intent);
    } catch (Exception e) {
      ToastUtils.showErrorToast(R.string.errot_no_market);
    }
  }

  public static void downloadApp(DownloadInfo downloadInfo) {
    int netType = ClientInfo.getAPNType(MarketApplication.curContext);
    if ((netType == ClientInfo.MOBILE_3G) || (netType == ClientInfo.MOBILE_2G)) {
      // ToastUtils.showToast(R.string.toast_tip_mobile_data);
    } else if (netType == ClientInfo.NONET) {
      ToastUtils.showToast(R.string.toast_tip_no_net);
      return;
    }
    AppManagerCenter.startDownload(downloadInfo);
  }

  /**
   * 分享文本
   * 
   * @param ctx
   *          ：
   * @param Content
   *          ： 分享的内容
   * @param subject
   *          ：
   * @param title
   *          ： 分享的标题
   */
  public static void shareText(Context ctx, String Content, String subject, String title) {
    Intent intent = new Intent(Intent.ACTION_SEND);
    // intent.setComponent(new ComponentName("com.tencent.mm",
    // "com.tencent.mm.ui.tools.ShareImgUI")); //分享到微信，指定
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
    intent.putExtra(Intent.EXTRA_TEXT, Content);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    ctx.startActivity(Intent.createChooser(intent, title)); // 普通分享
  }

  public static void shareImage(Context ctx, String Content, String subject, String title, String imageType, String path) {
    Intent intent = new Intent(Intent.ACTION_SEND);
    // 图片分享
    intent.setType(imageType);// "image/png"
    // 添加图片
    File f = new File(path);
    Uri uri = Uri.fromFile(f);
    intent.putExtra(Intent.EXTRA_STREAM, uri);

    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
    intent.putExtra(Intent.EXTRA_TEXT, Content);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    ctx.startActivity(Intent.createChooser(intent, title));
  }
}
