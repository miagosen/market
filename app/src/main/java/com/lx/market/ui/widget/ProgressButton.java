package com.lx.market.ui.widget;

import market.lx.com.R;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lx.market.download.AppManagerCenter;
import com.lx.market.model.DownloadInfo;
import com.lx.market.network.model.AppInfoBto;

/**
 * Created by Antikvo.Miao on 2014/8/8.
 */
public class ProgressButton extends FrameLayout {

  private final Context  mContext;
  private AppInfoBto     appInfoBto;
  private DownloadInfo   downloadInfo;
  private ProgressBar    progressBar;
  private TextView       tvProgress;
  private RelativeLayout rlProgressButton;

  public ProgressButton(Context context) {
    super(context);
    this.mContext = context;
    initView();
  }

  private void initView() {
    View view = LayoutInflater.from(mContext).inflate(R.layout.progress_button, this);
    progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    tvProgress = (TextView) view.findViewById(R.id.tv_download_progress);
    rlProgressButton = (RelativeLayout) view.findViewById(R.id.rl_progress_button);
  }

  public ProgressButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
    initView();
  }

  public ProgressButton(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.mContext = context;
    initView();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return super.onTouchEvent(event);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawButton();
  }

  private void drawButton() {
    int state = 0;
    String packageName = null;
    if (appInfoBto == null && downloadInfo == null) {
      return;
    } else if (appInfoBto != null) {
      Log.e("-----drawbutton----", "------" + appInfoBto.getPackageName() + appInfoBto.getVersionCode());
      state = AppManagerCenter.getAppDownlaodState(appInfoBto.getPackageName(), appInfoBto.getVersionCode());
      packageName = appInfoBto.getPackageName();
    } else if (downloadInfo != null) {
      state = AppManagerCenter.getAppDownlaodState(downloadInfo.getPackageName(), downloadInfo.getVersionCode());
      packageName = downloadInfo.getPackageName();
    }

    progressBar.setVisibility(View.GONE);
    switch (state) {
    case AppManagerCenter.APP_STATE_DOWNLOADED:
      progressBar.setVisibility(View.VISIBLE);
      progressBar.setProgress(100);
      tvProgress.setText(R.string.progress_btn_install);
      break;
    case AppManagerCenter.APP_STATE_INSTALLED:
      tvProgress.setText(R.string.progress_btn_start);

      break;
    case AppManagerCenter.APP_STATE_DOWNLOAD_PAUSE:
      progressBar.setVisibility(View.VISIBLE);
      progressBar.setProgress(AppManagerCenter.getDownloadProgress(packageName));
      tvProgress.setText(R.string.progress_btn_resume);
      break;
    case AppManagerCenter.APP_STATE_UNEXIST:
      tvProgress.setText(R.string.progress_btn_download);
      break;
    case AppManagerCenter.APP_STATE_UPDATE:
      tvProgress.setText(R.string.progress_btn_update);
      break;
    case AppManagerCenter.APP_STATE_DOWNLOADING:
      progressBar.setVisibility(View.VISIBLE);
      int progress = AppManagerCenter.getDownloadProgress(packageName);
      progressBar.setProgress(progress);
      tvProgress.setText(progress + mContext.getString(R.string.progress_btn_progess));
      break;
    case AppManagerCenter.APP_STATE_WAIT:
      int progress1 = AppManagerCenter.getDownloadProgress(packageName);
      if (progress1 == 0) {
        tvProgress.setText(R.string.progress_btn_download);
      } else if (progress1 == 100) {
        tvProgress.setText(R.string.progress_btn_install);
        progressBar.setProgress(100);
      } else {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(AppManagerCenter.getDownloadProgress(packageName));
        tvProgress.setText(R.string.progress_btn_resume);
      }
      break;
    case AppManagerCenter.APP_STATE_INSTALLING:
      tvProgress.setText(R.string.progress_btn_intalling);
      break;
    }
  }

  public void onUpdateButton() {
    drawButton();
  }

  public void setAppInfoBto(AppInfoBto appInfoBto) {
    this.appInfoBto = appInfoBto;
  }

  public void setDownloadInfo(DownloadInfo downloadInfo) {
    this.downloadInfo = downloadInfo;
  }

  @Override
  public void setOnClickListener(OnClickListener l) {
    super.setOnClickListener(l);
    rlProgressButton.setOnClickListener(l);
  }
}
