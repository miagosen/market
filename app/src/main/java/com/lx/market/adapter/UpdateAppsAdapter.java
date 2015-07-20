package com.lx.market.adapter;

import java.util.List;

import market.lx.com.R;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lx.market.MarketApplication;
import com.lx.market.constants.GlobalConstants;
import com.lx.market.download.AppManagerCenter;
import com.lx.market.model.DownloadInfo;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.utils.FileUtils;
import com.lx.market.utils.UIUtils;

public class UpdateAppsAdapter extends BaseAppAdapter {

  private final List<AppInfoBto> appList;
  private Context                mContext;

  public UpdateAppsAdapter(Context context, List<AppInfoBto> appList) {
    super(context, appList);
    this.appList = appList;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    AppInfoBto itemInfo = appList.get(position);
    MyOnClickListener listener;
    if (null == convertView) {
      convertView = LayoutInflater.from(mContext).inflate(R.layout.oc_update_app_item, null);
      holder = new ViewHolder();
      holder.mButton = (Button) convertView.findViewById(R.id.btn_download);
      holder.mAppNameView = (TextView) convertView.findViewById(R.id.tv_app_name);
      holder.mIconView = (ImageView) convertView.findViewById(R.id.iv_icon);
      holder.mDownLoadNum = (TextView) convertView.findViewById(R.id.tv_install_num);
      holder.mDescText = (TextView) convertView.findViewById(R.id.tv_app_info_content);
      holder.mFileSize = (TextView) convertView.findViewById(R.id.tv_app_file_size);

      listener = new MyOnClickListener();
      convertView.setTag(holder);
      convertView.setTag(holder.mButton.getId(), listener);
    } else {
      holder = (ViewHolder) convertView.getTag();
      listener = (MyOnClickListener) convertView.getTag(holder.mButton.getId());
    }
    listener.setPosition(position);
    holder.mDescText.setText(itemInfo.getBriefDesc());
    holder.mAppNameView.setText(itemInfo.getName());
    holder.mFileSize.setText(FileUtils.getFileSizeString(itemInfo.getFileSize()));
    holder.mDownLoadNum.setText(getDownloadNum(itemInfo.getDownTimes()));
    if (!TextUtils.isEmpty(itemInfo.getIconUrl())) {
      MarketApplication.getInstance().imageLoader.displayImage(itemInfo.getIconUrl().trim(), holder.mIconView);
    }

    return convertView;
  }

  class MyOnClickListener implements View.OnClickListener {
    int position;

    public void setPosition(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      /*
       * 顯示的文字 下載 安裝 啟動APP 不存在--->下載APP---->已下載，未安裝--->安裝--->已安裝--->啟動APP |
       * 卸載APP *
       */

      AppInfoBto item = mAppList.get(position);
      int state = AppManagerCenter.getAppDownlaodState(item.getPackageName(), item.getVersionCode());

      Log.e("--------", "-----onClick--state---" + state);
      /* 此處state是單一的值，用if判斷，switch也可以 */
      switch (state) {
      /* 已經下載完，未安裝 */
      case AppManagerCenter.APP_STATE_DOWNLOADED:
        Log.e("--------", "-----onClick--downloaded---");
        AppManagerCenter.installApk(new DownloadInfo(item, GlobalConstants.POSITION_HOME_APP_LIST));
        break;
      /* 已經安裝 */
      case AppManagerCenter.APP_STATE_INSTALLED:
        Log.e("--------", "-----onClick--installed---");
        UIUtils.startApp(item);
        break;
      /**
       * 应用下载被暂停
       */
      case AppManagerCenter.APP_STATE_DOWNLOAD_PAUSE:
        UIUtils.downloadApp(new DownloadInfo(item, GlobalConstants.POSITION_HOME_APP_LIST));
        break;
      /**
       * 应用不存在
       */
      case AppManagerCenter.APP_STATE_UNEXIST:
        UIUtils.downloadApp(new DownloadInfo(item, GlobalConstants.POSITION_HOME_APP_LIST));
        break;
      /**
       * 应用需要更新
       */
      case AppManagerCenter.APP_STATE_UPDATE:
        UIUtils.downloadApp(new DownloadInfo(item, GlobalConstants.POSITION_HOME_APP_LIST));
        break;
      /* 正在下載 */
      case AppManagerCenter.APP_STATE_DOWNLOADING:
        AppManagerCenter.pauseDownload(new DownloadInfo(item, GlobalConstants.POSITION_HOME_APP_LIST), true);
        break;
      /**
       * 应用等待下载
       */
      case AppManagerCenter.APP_STATE_WAIT:
        UIUtils.downloadApp(new DownloadInfo(item, GlobalConstants.POSITION_HOME_APP_LIST));
        break;

      }
      UpdateAppsAdapter.this.notifyDataSetChanged();
    }
  }

  private String getDownloadNum(int downNum) {
    String text;
    if (downNum < 10000) {
      text = downNum + "" + mContext.getString(R.string.download_num);
    } else {
      text = downNum / 10000 + mContext.getString(R.string.wan) + mContext.getString(R.string.download_num);
    }
    return text;
  }

  class ViewHolder {
    public ImageView mIconView;
    public TextView  mAppNameView;
    // 用于数据统计
    public int       mPosition;
    // APK大小
    public TextView  mFileSize;
    // 下载人数
    public TextView  mDownLoadNum;
    // 应用描述信息
    public TextView  mDescText;
    public Button    mButton;
  }

}
