package com.lx.market.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import market.lx.com.R;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lx.market.MarketApplication;
import com.lx.market.activity.AppDetailActivity;
import com.lx.market.activity.UpdateDialogListener;
import com.lx.market.constants.BundleConstants;
import com.lx.market.download.AppManagerCenter;
import com.lx.market.download.DownloadState;
import com.lx.market.download.DownloadTask;
import com.lx.market.download.DownloadTaskMgr;
import com.lx.market.download.UIDownLoadListener;
import com.lx.market.model.ClientInfo;
import com.lx.market.model.DownloadInfo;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.ui.dialog.UpdateDialogFragment;
import com.lx.market.ui.widget.ProgressButton;
import com.lx.market.utils.FileUtils;
import com.lx.market.utils.Logger;
import com.lx.market.utils.UIUtils;

public class DownloadManagerListAdapter extends BaseAdapter {
  private final Context            mContext;
  private final List<DownloadTask> downloadTasks                       = new ArrayList<DownloadTask>();
  private final List<DownloadTask> downloadTasksHistory                = new ArrayList<DownloadTask>();
  private UIDownLoadListener       uiDownLoadListener                  = null;
  private DownloadInfo             downloadInfo;

  private View                     mLastView;
  private int                      mLastPosition;
  private int                      mLastVisibility;
  private int                      theFirstPositionOfDownloadCompleted = -1;
  private boolean                  flag                                = true;

  private final TextView           tvDownloadTaskNum1;
  private final FragmentManager    manager;

  public void setTheFirstPositionOfDownloadCompleted(int theFirstPositionOfDownloadCompleted) {
    if (flag) {
      this.theFirstPositionOfDownloadCompleted = theFirstPositionOfDownloadCompleted;
    }
  }

  public DownloadManagerListAdapter(Context context, TextView tvDownloadTaskNum, FragmentManager manager) {
    mContext = context;
    this.manager = manager;
    tvDownloadTaskNum1 = tvDownloadTaskNum;
    uiDownLoadListener = new UIDownLoadListener() {
      @Override
      public void onRefreshUI(String pkgName) {
        initData();
        notifyDataSetChanged();
        tvDownloadTaskNum1.setText(String.valueOf(getCount()));
      }

      @Override
      protected void onErrorCode(String pkgName, int errorCode) {
        int error = ClientInfo.getAPNType(MarketApplication.curContext);
        switch (error) {
        case DownloadState.ERROR_CODE_NO_NET:
          Toast.makeText(mContext, "请检查网络设置！！！", Toast.LENGTH_SHORT).show();
          break;
        case DownloadState.ERROR_CODE_NO_SDCARD:
          Toast.makeText(mContext, "请检查SDCard是否安装好！！！", Toast.LENGTH_SHORT).show();
          break;
        case DownloadState.ERROR_CODE_NOT_WIFI:
          Toast.makeText(mContext, "注意，正在使用非WiFi下载应用！！！", Toast.LENGTH_LONG).show();
          break;
        }
      }

      @Override
      protected void onFinish(String pkgName) {
        initData();
        notifyDataSetChanged();
        tvDownloadTaskNum1.setText(String.valueOf(getCount()));
      }

    };
    initData();
    setDownlaodRefreshHandle();
    tvDownloadTaskNum1.setText(String.valueOf(getCount()));
  }

  private void initData() {
    HashMap<String, DownloadTask> downloadTaskHashMap = DownloadTaskMgr.getInstance().getDownloadTaskHashMap();
    downloadTasks.clear();
    downloadTasksHistory.clear();
    for (DownloadTask downloadTask : downloadTaskHashMap.values()) {
      Log.e("------init data downloadTasks--", "加强for" + downloadTask.downloadInfo.getPackageName());
      downloadTasks.add(downloadTask);
      Log.e("------init data downloadTasks--", "加强forfor" + downloadTask.downloadInfo.getPackageName());
      int state = AppManagerCenter.getAppDownlaodState(downloadTask.downloadInfo.getPackageName(), downloadTask.downloadInfo.getVersionCode());
      if (state == AppManagerCenter.APP_STATE_DOWNLOADED || state == AppManagerCenter.APP_STATE_INSTALLED) {
        Log.e("------init data downloadTasks--", "加强forforfor" + downloadTask.downloadInfo.getPackageName());
        downloadTasksHistory.add(downloadTask);
      }
    }

    downloadTasks.removeAll(downloadTasksHistory);
    setTheFirstPositionOfDownloadCompleted(downloadTasks.size());
    Log.e("------first position--", "----" + downloadTasks.size());

    downloadTasks.addAll(downloadTasks.size(), downloadTasksHistory);
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

  @Override
  public int getCount() {
    return downloadTasks == null ? 0 : downloadTasks.size();
  }

  @Override
  public Object getItem(int i) {
    return downloadTasks == null ? null : downloadTasks.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    final ViewHolder holder;
    downloadInfo = downloadTasks.get(i).downloadInfo;
    DeleteItemOnClickListener dItemOnClickListener;
    DownloadTitleListener downloadTitleListener;
    MyOnClickListener listener;
    ListViewItemClickListener listViewItemClickListener;
    if (view == null) {
      view = LayoutInflater.from(mContext).inflate(R.layout.oc_activity_download_item, null);
      holder = new ViewHolder();
      holder.ivIcon = (ImageView) view.findViewById(R.id.iv_download_item_icon);
      holder.tvTitle = (TextView) view.findViewById(R.id.tv_download_item_app_title);
      holder.tvItemSize = (TextView) view.findViewById(R.id.tv_download_item_size);
      holder.tvDownloadProgress = (TextView) view.findViewById(R.id.tv_download_item_download_progress_and_total_size);
      holder.vItemLine = view.findViewById(R.id.download_list_item_line);
      holder.pButton = (ProgressButton) view.findViewById(R.id.pb_download_item);
      holder.llDetailItemAction = (LinearLayout) view.findViewById(R.id.download_list_actions_layout);
      holder.btnAppDetail = (Button) view.findViewById(R.id.app_detail_btn);
      holder.btnCancelTask = (Button) view.findViewById(R.id.app_cancel_btn);
      holder.rlDownloadItemTitle = (RelativeLayout) view.findViewById(R.id.download_item_title);
      holder.tvDoewnloadItemTitleLeft = (TextView) view.findViewById(R.id.tv_download_item_title_left);
      holder.tvDoewnloadItemTitleRight = (TextView) view.findViewById(R.id.tv_download_item_title_right);
      dItemOnClickListener = new DeleteItemOnClickListener(holder.btnAppDetail, holder.btnCancelTask);
      listener = new MyOnClickListener();
      downloadTitleListener = new DownloadTitleListener(holder.tvDoewnloadItemTitleRight);
      view.setTag(holder);
      view.setTag(holder.btnAppDetail.getId(), dItemOnClickListener);
      view.setTag(holder.pButton.getId(), listener);
      view.setTag(holder.tvDoewnloadItemTitleRight.getId(), downloadTitleListener);
    } else {
      holder = (ViewHolder) view.getTag();
      dItemOnClickListener = (DeleteItemOnClickListener) view.getTag(holder.btnAppDetail.getId());
      listener = (MyOnClickListener) view.getTag(holder.pButton.getId());
      downloadTitleListener = (DownloadTitleListener) view.getTag(holder.tvDoewnloadItemTitleRight.getId());
    }
    listener.setPosition(i);
    holder.pButton.setDownloadInfo(downloadInfo);
    holder.pButton.setOnClickListener(listener);
    holder.pButton.onUpdateButton();

    holder.ivIcon.setOnClickListener(listener);
    holder.tvDoewnloadItemTitleRight.setOnClickListener(downloadTitleListener);

    dItemOnClickListener.setPosition(i);
    holder.btnAppDetail.setOnClickListener(dItemOnClickListener);
    holder.btnCancelTask.setOnClickListener(dItemOnClickListener);
    // holder.tvItemSize.setText(FileUtils.getFileSizeString(downloadInfo.getFileSize()));
    holder.tvTitle.setText(downloadInfo.getFileName());
    holder.tvDownloadProgress.setText(mContext.getString(R.string.downloaded) + FileUtils.getFileSizeString(downloadTasks.get(i).gameDownloadPostion) + "/"
        + FileUtils.getFileSizeString(downloadInfo.getFileSize()));

    try {
      MarketApplication.getInstance().imageLoader.displayImage(downloadInfo.getIconUrl().trim(), holder.ivIcon);
    } catch (Exception e) {
      Logger.p(e);
    }
    listViewItemClickListener = new ListViewItemClickListener(view, i);
    /*
     * 1.只有正下载the = -1 2.只有下载历史the != -1 the = 0 3.都有the != -1 the != 0
     */
    Log.e("-----------------", " the = " + theFirstPositionOfDownloadCompleted + " i = " + i);
    if (theFirstPositionOfDownloadCompleted == -1) {
      if (i == 0) {
        holder.rlDownloadItemTitle.setVisibility(View.VISIBLE);
        holder.tvDoewnloadItemTitleLeft.setText(R.string.download_task);
        holder.tvDoewnloadItemTitleRight.setVisibility(View.INVISIBLE);
        downloadTitleListener.setPosition(2);
        dItemOnClickListener.setIsDownloadHistory(false);
        listViewItemClickListener.setIsDownloadHistory(false);
      }
    } else if (theFirstPositionOfDownloadCompleted != -1) {
      if (theFirstPositionOfDownloadCompleted == 0 && i == 0) {
        holder.rlDownloadItemTitle.setVisibility(View.VISIBLE);
        holder.tvDoewnloadItemTitleLeft.setText(R.string.download_history);
        holder.tvDoewnloadItemTitleRight.setVisibility(View.VISIBLE);
        holder.tvDoewnloadItemTitleRight.setText(R.string.download_history_clear);
        downloadTitleListener.setPosition(1);
        dItemOnClickListener.setIsDownloadHistory(true);
        listViewItemClickListener.setIsDownloadHistory(true);
      } else if (theFirstPositionOfDownloadCompleted != 0) {
        if (i == 0) {
          holder.rlDownloadItemTitle.setVisibility(View.VISIBLE);
          holder.tvDoewnloadItemTitleLeft.setText(R.string.download_task);
          holder.tvDoewnloadItemTitleRight.setVisibility(View.INVISIBLE);
          downloadTitleListener.setPosition(2);
          dItemOnClickListener.setIsDownloadHistory(false);
          listViewItemClickListener.setIsDownloadHistory(false);
        } else if (i != 0 && i == theFirstPositionOfDownloadCompleted) {
          holder.rlDownloadItemTitle.setVisibility(View.VISIBLE);
          holder.tvDoewnloadItemTitleLeft.setText(R.string.download_history);
          holder.tvDoewnloadItemTitleRight.setVisibility(View.VISIBLE);
          holder.tvDoewnloadItemTitleRight.setText(R.string.download_history_clear);
          downloadTitleListener.setPosition(1);
          dItemOnClickListener.setIsDownloadHistory(true);
          listViewItemClickListener.setIsDownloadHistory(true);
        } else {
          holder.rlDownloadItemTitle.setVisibility(View.GONE);
        }
      } else {
        holder.rlDownloadItemTitle.setVisibility(View.GONE);
      }
    }
    view.setOnClickListener(listViewItemClickListener);
    holder.llDetailItemAction.setVisibility(View.GONE);
    return view;
  }

  class DownloadTitleListener implements OnClickListener {
    private int            position;
    private int            i = 2;
    private final TextView tv;

    public DownloadTitleListener(TextView tv) {
      this.tv = tv;
    }

    public void setPosition(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
      case R.id.tv_download_item_title_right:
        if (position == 1) {
          showDialog();
        } else if (position == 2) {
          if (position == i) {
            i++;
            tv.setText(R.string.download_all_pause);
            AppManagerCenter.continueAllDownload();
          } else {
            i--;
            tv.setText(R.string.download_all_start);
            AppManagerCenter.pauseAllDownload();
          }
        } else {
          return;
        }
        break;
      }
    }
  }

  private void showDialog() {
    String dialogTitle = mContext.getResources().getString(R.string.delete_download_history_title);
    String dialogContent = mContext.getResources().getString(R.string.delete_download_history_content);
    UpdateDialogFragment fragment = new UpdateDialogFragment(mContext, dialogTitle, dialogContent, new UpdateDialogListener() {

      @Override
      public void onDialogPositiveClick() {
        Toast.makeText(mContext, "yes", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < downloadTasksHistory.size(); i++) {
          Log.e("downloadHistory", "----" + downloadTasksHistory.get(i).downloadInfo.getPackageName());
        }
        List<DownloadInfo> list = new ArrayList<DownloadInfo>();
        for (int i = 0; i < downloadTasksHistory.size(); i++) {
          list.add(downloadTasksHistory.get(i).downloadInfo);
        }
        DownloadTaskMgr.getInstance().clearAllDownloadHistory(list);
      }

      @Override
      public void onDialogNegativeClick() {
        Toast.makeText(mContext, "no", Toast.LENGTH_SHORT).show();
      }
    });
    fragment.show(manager, "ShowDialog");
  }

  class ListViewItemClickListener implements OnClickListener {
    private final View view;
    private final int  i;
    private boolean    isDownloadHistory = true;

    public ListViewItemClickListener(View view, int i) {
      this.view = view;
      this.i = i;
    }

    public void setIsDownloadHistory(boolean isDownloadHistory) {
      this.isDownloadHistory = isDownloadHistory;
    }

    @Override
    public void onClick(View v) {
      changeImageVisable(view, i, isDownloadHistory);
    }

  }

  private void changeImageVisable(View view, int position, boolean isDownloadHistory) {
    if (mLastView != null && mLastPosition != position) {
      ViewHolder holder = (ViewHolder) mLastView.getTag();
      switch (holder.llDetailItemAction.getVisibility()) {
      case View.VISIBLE:
        holder.llDetailItemAction.setVisibility(View.GONE);
        holder.vItemLine.setVisibility(View.GONE);
        mLastVisibility = View.GONE;
        break;
      default:
        break;
      }
    }
    mLastPosition = position;
    mLastView = view;
    ViewHolder holder = (ViewHolder) view.getTag();
    switch (holder.llDetailItemAction.getVisibility()) {
    case View.GONE:
      holder.llDetailItemAction.setVisibility(View.VISIBLE);
      holder.vItemLine.setVisibility(View.VISIBLE);
      mLastVisibility = View.VISIBLE;
      break;
    case View.VISIBLE:
      holder.llDetailItemAction.setVisibility(View.GONE);
      holder.vItemLine.setVisibility(View.GONE);
      mLastVisibility = View.GONE;
      break;
    }

    if (isDownloadHistory) {
      holder.btnCancelTask.setText(R.string.download_history_clear_item);
    }
  }

  public class ViewHolder {

    ImageView      ivIcon;
    TextView       tvTitle;
    TextView       tvItemSize;
    TextView       tvDownloadProgress;
    ProgressButton pButton;
    LinearLayout   llDetailItemAction;
    Button         btnAppDetail;
    Button         btnCancelTask;
    View           vItemLine;
    RelativeLayout rlDownloadItemTitle;
    TextView       tvDoewnloadItemTitleLeft;
    TextView       tvDoewnloadItemTitleRight;
  }

  class DeleteItemOnClickListener implements OnClickListener {
    int     position;
    Button  itemDeleteTask;
    Button  itemDeleteHistory;
    boolean isDownloadHistory = true;

    public DeleteItemOnClickListener(Button itemDeleteTask, Button itemDeleteHistory) {
      this.itemDeleteTask = itemDeleteTask;
      this.itemDeleteHistory = itemDeleteHistory;
    }

    public void setIsDownloadHistory(boolean isDownloadHistory) {
      this.isDownloadHistory = isDownloadHistory;
    }

    public void setPosition(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
      case R.id.app_detail_btn:
        Intent intent = new Intent(mContext, AppDetailActivity.class);
        AppInfoBto appInfoBto = new AppInfoBto();
        appInfoBto.setPackageName(downloadTasks.get(position).downloadInfo.getPackageName());
        appInfoBto.setRefId(downloadTasks.get(position).downloadInfo.getRefId());
        appInfoBto.setVersionCode(downloadTasks.get(position).downloadInfo.getVersionCode());
        appInfoBto.setDownUrl(downloadTasks.get(position).downloadInfo.getDownloadUrl());
        intent.putExtra(BundleConstants.BUNDLE_APP_INFO_BTO, appInfoBto);
        mContext.startActivity(intent);
        break;
      case R.id.app_cancel_btn:
        if (isDownloadHistory) {
          DownloadTaskMgr.getInstance().clearDownloadHistory(downloadTasks.get(position).downloadInfo);
        } else {
          AppManagerCenter.cancelDownload(downloadTasks.get(position).downloadInfo);
          DownloadManagerListAdapter.this.notifyDataSetChanged();
          flag = true;
          setTheFirstPositionOfDownloadCompleted(theFirstPositionOfDownloadCompleted);
          flag = false;
        }

        break;
      }
    }
  }

  class MyOnClickListener implements View.OnClickListener {
    int position;

    public void setPosition(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      if (v.getId() == R.id.iv_download_item_icon) {
        Intent intent = new Intent(mContext, AppDetailActivity.class);
        AppInfoBto appInfoBto = new AppInfoBto();
        appInfoBto.setPackageName(downloadTasks.get(position).downloadInfo.getPackageName());
        appInfoBto.setRefId(downloadTasks.get(position).downloadInfo.getRefId());
        appInfoBto.setVersionCode(downloadTasks.get(position).downloadInfo.getVersionCode());
        appInfoBto.setDownUrl(downloadTasks.get(position).downloadInfo.getDownloadUrl());
        intent.putExtra(BundleConstants.BUNDLE_APP_INFO_BTO, appInfoBto);
        mContext.startActivity(intent);
      } else {
        /*
         * 顯示的文字 下載 安裝 啟動APP 不存在--->下載APP---->已下載，未安裝--->安裝--->已安裝--->啟動APP |
         * 卸載APP *
         */

        DownloadInfo item = downloadTasks.get(position).downloadInfo;
        int state = AppManagerCenter.getAppDownlaodState(item.getPackageName(), item.getVersionCode());

        Log.e("--------", "-----onClick--state---" + state);
        /* 此處state是單一的值，用if判斷，switch也可以 */
        switch (state) {
        /* 已經下載完，未安裝 */
        case AppManagerCenter.APP_STATE_DOWNLOADED:
          Log.e("--------", "-----onClick--downloaded---");
          AppManagerCenter.installApk(item);
          break;
        /* 已經安裝 */
        case AppManagerCenter.APP_STATE_INSTALLED:
          Log.e("--------", "-----onClick--installed---");
          UIUtils.startApp(item.getPackageName());
          break;
        /**
         * 应用下载被暂停
         */
        case AppManagerCenter.APP_STATE_DOWNLOAD_PAUSE:
          UIUtils.downloadApp(item);
          break;
        /**
         * 应用不存在
         */
        case AppManagerCenter.APP_STATE_UNEXIST:
          UIUtils.downloadApp(item);
          break;
        /**
         * 应用需要更新
         */
        case AppManagerCenter.APP_STATE_UPDATE:
          UIUtils.downloadApp(item);
          break;
        /* 正在下載 */
        case AppManagerCenter.APP_STATE_DOWNLOADING:
          AppManagerCenter.pauseDownload(item, true);
          break;
        /**
         * 应用等待下载
         */
        case AppManagerCenter.APP_STATE_WAIT:
          UIUtils.downloadApp(item);
          break;

        }
        DownloadManagerListAdapter.this.notifyDataSetChanged();
      }
    }
  }

}
