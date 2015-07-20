package com.lx.market.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lx.market.MarketApplication;
import com.lx.market.constants.GlobalConstants;
import com.lx.market.download.AppManagerCenter;
import com.lx.market.download.UIDownLoadListener;
import com.lx.market.model.DownloadInfo;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.ui.widget.CircleProgressView;
import com.lx.market.utils.Logger;
import com.lx.market.utils.UIUtils;

import java.util.List;

import market.lx.com.R;

/**
 * Created by Antikvo.Miao on 2014/7/28.
 */
public class PopularAppInfoAdapter extends BaseAdapter {
  private Context mContext;
  private List<AppInfoBto> mAppList;
  private LayoutInflater mInflater;
  private UIDownLoadListener uiDownLoadListener = null;

  public PopularAppInfoAdapter (Context c, List<AppInfoBto> data) {
	this.mContext = c;
	this.mAppList = data;
	mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	uiDownLoadListener = new UIDownLoadListener() {
	  @Override
	  public void onRefreshUI (String pkgName) {
		PopularAppInfoAdapter.this.notifyDataSetChanged();
	  }
	};
	setDownlaodRefreshHandle();
  }

  @Override
  public int getCount () {
	return mAppList == null ? 0 : mAppList.size();
  }

  @Override
  public Object getItem (int position) {
	return mAppList == null ? null : mAppList.get(position);
  }

  @Override
  public long getItemId (int position) {
	return position;
  }

  /**
   * 取消 下载监听, Activity OnDestroy 时调用
   */
  public void removeDownLoadHandler () {
	AppManagerCenter.removeDownloadRefreshHandle(uiDownLoadListener);
  }

  /**
   * 设置刷新handler,Activity OnResume 时调用
   */
  public void setDownlaodRefreshHandle () {
	AppManagerCenter.setDownloadRefreshHandle(uiDownLoadListener);
  }

  @Override
  public View getView (int position, View view, ViewGroup parent) {
	final ViewHolder holder;
	AppInfoBto appInfo = mAppList.get(position);
	MyOnClickListener myOnClickListener;
	if (view == null) {
	  Logger.error("PopularAppInfoAdapter","---view null-----"+position);
	  holder = new ViewHolder();
	  view = mInflater.inflate(R.layout.oc_grid_app_info, null);
	  holder.circleProgressView = (CircleProgressView) view.findViewById(R.id.pbtn_icon);
	  holder.tvAppName = (TextView) view.findViewById(R.id.tv_app_name);
	  myOnClickListener = new MyOnClickListener();
	  myOnClickListener.setPosition(position);
	  view.setTag(holder);
	  view.setTag(holder.circleProgressView.getId(), myOnClickListener);
	  MarketApplication.getInstance().imageLoader.displayImage(appInfo.getIconUrl().trim(), holder.circleProgressView);
	} else {
	  holder = (ViewHolder) view.getTag();
	  Logger.error("PopularAppInfoAdapter","---have view -----" + holder.toString());
	  myOnClickListener = (MyOnClickListener) view.getTag(holder.circleProgressView.getId());
	}
	holder.tvAppName.setText(mAppList.get(position).getName());
	holder.circleProgressView.setOnClickListener(myOnClickListener);
	updateCircleProgressView(holder.circleProgressView, appInfo);
	return view;
  }

  private void updateCircleProgressView (CircleProgressView circleProgressView, AppInfoBto appInfo) {
	int state = AppManagerCenter.getAppDownlaodState(appInfo.getPackageName(),
	  appInfo.getVersionCode());
	switch (state) {
	  case AppManagerCenter.APP_STATE_DOWNLOADED:
	  case AppManagerCenter.APP_STATE_INSTALLED:
	  case AppManagerCenter.APP_STATE_DOWNLOAD_PAUSE:
		circleProgressView.setDownloading(false);
		break;
	  case AppManagerCenter.APP_STATE_UNEXIST:
	  case AppManagerCenter.APP_STATE_UPDATE:
	  case AppManagerCenter.APP_STATE_DOWNLOADING:
	  case AppManagerCenter.APP_STATE_WAIT:
		circleProgressView.setDownloading(true);
		break;
	}
	int progress = AppManagerCenter.getDownloadProgress(appInfo.getPackageName());
	circleProgressView.setProgress(progress);
  }

  class MyOnClickListener implements View.OnClickListener {
	int position;
	AppInfoBto item;

	public void setPosition (int position) {
	  this.position = position;
	}

	@Override
	public void onClick (View v) {
	  item = mAppList.get(position);
	  int state = AppManagerCenter.getAppDownlaodState(item.getPackageName(),
		item.getVersionCode());
	  switch (state) {
		case AppManagerCenter.APP_STATE_DOWNLOADED:
		  AppManagerCenter.installApk(new DownloadInfo(item, GlobalConstants.POSITION_HOME_APP_LIST));
		  break;
		case AppManagerCenter.APP_STATE_INSTALLED:
		  UIUtils.startApp(item);
		  break;
		case AppManagerCenter.APP_STATE_DOWNLOAD_PAUSE:
		case AppManagerCenter.APP_STATE_UNEXIST:
		case AppManagerCenter.APP_STATE_UPDATE:
		  UIUtils.downloadApp(new DownloadInfo(item, GlobalConstants.POSITION_HOME_APP_LIST));
		  break;
		case AppManagerCenter.APP_STATE_DOWNLOADING:
		case AppManagerCenter.APP_STATE_WAIT:
		  AppManagerCenter.pauseDownload(new DownloadInfo(item, GlobalConstants.POSITION_HOME_APP_LIST), true);
		  break;
	  }
	}
  }

  class ViewHolder {
	CircleProgressView circleProgressView;
	TextView tvAppName;
  }
}
