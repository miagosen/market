package com.lx.market.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lx.market.MarketApplication;
import com.lx.market.activity.AppDetailActivity;
import com.lx.market.activity.WapActivity;
import com.lx.market.constants.BundleConstants;
import com.lx.market.constants.GlobalConstants;
import com.lx.market.download.AppManagerCenter;
import com.lx.market.download.UIDownLoadListener;
import com.lx.market.model.DownloadInfo;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.ui.widget.ProgressButton;
import com.lx.market.utils.UIUtils;

import java.util.List;

import market.lx.com.R;

/**
 * Created by Antikvo.Miao on 2014/8/19.
 */
public class FlowBarAdapter extends PagerAdapter {
  private List<AppInfoBto> mAppList;
  private Context mContext;
  private SparseArray<View> pagers = new SparseArray<View>();

  private UIDownLoadListener uiDownLoadListener = null;

  public FlowBarAdapter (Context context, List<AppInfoBto> appList) {
	this.mContext = context;
	mAppList = appList;
	uiDownLoadListener = new UIDownLoadListener() {
	  @Override
	  public void onRefreshUI (String pkgName) {
		notifyDataSetChanged();
	  }
	};
	setDownlaodRefreshHandle();
  }

  @Override
  public int getItemPosition (Object object) {
	return POSITION_NONE;
  }

  @Override
  public void destroyItem (ViewGroup container, int position, Object view) {
	((ViewPager) container).removeView((View) view);
  }

  public View getView (final int position, final ViewPager pager) {
	final ViewHolder viewHolder;
	View convertView = pagers.get(position);
	AppInfoBto appInfoBto = mAppList.get(position);
	MyOnClickListener myOnClickListener;
	if (convertView == null) {
	  viewHolder = new ViewHolder();
	  convertView = LayoutInflater.from(mContext).inflate(R.layout.oc_flow_bar_item, null);
	  viewHolder.ivFlowPic = (ImageView) convertView.findViewById(R.id.iv_flow_pic);
	  viewHolder.ivFlowSmallPic = (ImageView) convertView.findViewById(R.id.iv_flow_small_pic);
	  viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
	  viewHolder.progressButton = (ProgressButton) convertView.findViewById(R.id.btn_download);
	  viewHolder.tvDownNum = (TextView) convertView.findViewById(R.id.tv_app_info_content);
	  viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
	  viewHolder.rlFlowFlipperItem = (RelativeLayout) convertView.findViewById(R.id.rl_flow_flipper_item);
	  myOnClickListener = new MyOnClickListener();
	  myOnClickListener.setPosition(position);
	  convertView.setTag(viewHolder);
	  convertView.setTag(viewHolder.progressButton.getId(), myOnClickListener);
	  pagers.put(position, convertView);
	} else {
	  viewHolder = (ViewHolder) convertView.getTag();
	  myOnClickListener = (MyOnClickListener) convertView.getTag(viewHolder.progressButton.getId());
	}
	if (appInfoBto != null) {
	  //1:应用2:栏目3:wap
	  if (appInfoBto.getResType() == GlobalConstants.ACTION_TYPE_APK) {
		viewHolder.progressButton.setOnClickListener(myOnClickListener);
		viewHolder.ivFlowSmallPic.setOnClickListener(myOnClickListener);
		handleAppInfoBtoType(viewHolder, appInfoBto);
	  } else if (appInfoBto.getResType() == GlobalConstants.ACTION_TYPE_ASSEMBLE || appInfoBto.getResType() == GlobalConstants.ACTION_TYPE_WAP) {
		viewHolder.ivFlowPic.setVisibility(View.VISIBLE);
		viewHolder.rlFlowFlipperItem.setVisibility(View.GONE);
		viewHolder.ivFlowPic.setOnClickListener(myOnClickListener);
		MarketApplication.getInstance().imageLoader.displayImage(appInfoBto.getIconUrl().trim(), viewHolder.ivIcon);
	  }
	}
	return convertView;
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

  private void handleAppInfoBtoType (final ViewHolder viewHolder, final AppInfoBto appInfoBto) {
	MarketApplication.getInstance().imageLoader.displayImage(appInfoBto.getIconUrl().trim(), viewHolder.ivIcon);
	MarketApplication.getInstance().imageLoader.displayImage(appInfoBto.getImgUrl().trim(), viewHolder.ivFlowSmallPic);
	viewHolder.tvDownNum.setText(getDownloadNum(appInfoBto.getDownTimes()));
	viewHolder.tvTitle.setText(appInfoBto.getName());
	viewHolder.ivFlowPic.setVisibility(View.GONE);
	viewHolder.rlFlowFlipperItem.setVisibility(View.VISIBLE);
	viewHolder.progressButton.setAppInfoBto(appInfoBto);
	viewHolder.progressButton.onUpdateButton();
  }

  private String getDownloadNum (int downNum) {
	int num = 0;
	if (downNum > 10000) {
	  num = downNum / 10000;
	  return num + mContext.getString(R.string.wan) + mContext.getString(R.string.download_num);
	} else {
	  return num + mContext.getString(R.string.download_num);
	}
  }

  @Override
  public Object instantiateItem (ViewGroup container, int position) {
	ViewPager pager = (ViewPager) container;
	View view = getView(position, pager);
	pager.addView(view);
	return view;
  }

  @Override
  public int getCount () {
	return mAppList == null ? 0 : mAppList.size();
  }

  @Override
  public boolean isViewFromObject (View view, Object object) {
	return view == object;
  }

  class ViewHolder {
	ImageView ivFlowPic;//单张大图
	ImageView ivFlowSmallPic;//带有icon和button的图片，略小
	TextView tvTitle;
	TextView tvDownNum;
	ProgressButton progressButton;
	ImageView ivIcon;
	RelativeLayout rlFlowFlipperItem;
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
	  switch (v.getId()) {
		case R.id.rl_progress_button:
		case R.id.btn_download:
		  onClickDownload();
		  FlowBarAdapter.this.notifyDataSetChanged();
		  break;
		case R.id.iv_flow_small_pic:
		  Intent intent = new Intent(mContext, AppDetailActivity.class);
		  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		  intent.putExtra(BundleConstants.BUNDLE_APP_INFO_BTO, item);
		  mContext.startActivity(intent);
		  break;
		case R.id.iv_flow_pic:
		  if (item.getResType() == GlobalConstants.ACTION_TYPE_WAP) {
			Intent wapIntent = new Intent(mContext, WapActivity.class);
			wapIntent.putExtra(BundleConstants.BUNDLE_APP_INFO_BTO, item);
			mContext.startActivity(wapIntent);
		  } else if (item.getResType() == GlobalConstants.ACTION_TYPE_ASSEMBLE) {

		  }
		  break;
	  }
	}

	private void onClickDownload () {
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

}
