package com.lx.market.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import market.lx.com.R;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lx.market.MarketApplication;
import com.lx.market.download.AppManagerCenter;
import com.lx.market.download.DownloadTask;
import com.lx.market.download.DownloadTaskMgr;
import com.lx.market.download.UIDownLoadListener;
import com.lx.market.model.DownloadInfo;
import com.lx.market.ui.dialog.DeleteDownloadItemFragment;
import com.lx.market.utils.FileUtils;
import com.lx.market.utils.Logger;
import com.lx.market.utils.UIUtils;

/**
 * Created by Antikvo.Miao on 2014/7/31.
 */
public class DownloadListAdapter extends BaseAdapter {
  private final Context      mContext;
  private List<DownloadTask> downloadTasks      = new ArrayList<DownloadTask>();
  private UIDownLoadListener uiDownLoadListener = null;
  private DownloadInfo       downloadInfo;

  private View               mLastView;
  private int                mLastPosition;
  private int                mLastVisibility;

  private FragmentManager    manager;

  public DownloadListAdapter(Context context, FragmentManager manager) {
    this.mContext = context;
    this.manager = manager;
    uiDownLoadListener = new UIDownLoadListener() {
      @Override
      public void onRefreshUI(String pkgName) {
        initData();
        notifyDataSetChanged();
      }
    };
    initData();
    setDownlaodRefreshHandle();
  }

  private void initData() {
    HashMap<String, DownloadTask> downloadTaskHashMap = DownloadTaskMgr.getInstance().getDownloadTaskHashMap();
    downloadTasks.clear();
    for (DownloadTask downloadTask : downloadTaskHashMap.values()) {
      downloadTasks.add(downloadTask);
    }
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
    MyOnClickListener listener;
    DeleteItemOnClickListener dItemOnClickListener;
    if (view == null) {
      view = LayoutInflater.from(mContext).inflate(R.layout.oc_download_list_item_info, null);
      holder = new ViewHolder();
      holder.btnDownloadStatus = (Button) view.findViewById(R.id.btn_download_status);
      holder.progressBar = (ProgressBar) view.findViewById(R.id.pb_download_item);
      holder.tvDownloadProgress = (TextView) view.findViewById(R.id.tv_download_progress);
      holder.tvItemSize = (TextView) view.findViewById(R.id.tv_download_item_size);
      holder.tvTitle = (TextView) view.findViewById(R.id.tv_download_item_title);
      holder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
      holder.detailItemDelete = (LinearLayout) view.findViewById(R.id.detail_item_delete);
      holder.itemDeleteTask = (Button) view.findViewById(R.id.item_delete_task);
      holder.itemDeleteHistory = (Button) view.findViewById(R.id.item_delete_history);
      listener = new MyOnClickListener(holder.btnDownloadStatus, holder.progressBar, holder.tvItemSize, holder.tvDownloadProgress);
      dItemOnClickListener = new DeleteItemOnClickListener(holder.itemDeleteTask, holder.itemDeleteHistory);
      // holder.btnDownloadStatus.setOnClickListener(listener);
      view.setTag(holder);
      view.setTag(holder.progressBar.getId(), listener);// 对监听对象保存
      view.setTag(holder.itemDeleteTask.getId(), dItemOnClickListener);
    } else {
      holder = (ViewHolder) view.getTag();
      listener = (MyOnClickListener) view.getTag(holder.progressBar.getId());
      dItemOnClickListener = (DeleteItemOnClickListener) view.getTag(holder.itemDeleteTask.getId());
    }
    listener.setPosition(i);
    dItemOnClickListener.setPosition(i);
    holder.itemDeleteTask.setOnClickListener(dItemOnClickListener);
    holder.itemDeleteHistory.setOnClickListener(dItemOnClickListener);
    holder.btnDownloadStatus.setOnClickListener(listener);
    holder.tvItemSize.setText(FileUtils.getFileSizeString(downloadInfo.getFileSize()));
    holder.tvTitle.setText(downloadInfo.getFileName());
    holder.tvDownloadProgress.setText(mContext.getString(R.string.downloaded) + FileUtils.getFileSizeString(downloadTasks.get(i).gameDownloadPostion));
    holder.progressBar.setProgress(AppManagerCenter.getDownloadProgress(downloadInfo.getPackageName()));
    initBtn(holder.btnDownloadStatus, i, holder.progressBar, holder.tvItemSize, holder.tvDownloadProgress);
    try {
      MarketApplication.getInstance().imageLoader.displayImage(downloadInfo.getIconUrl().trim(), holder.ivIcon);
    } catch (Exception e) {
      Logger.p(e);
    }

    view.setOnClickListener(new ListViewItemClickListener(view, i));
    return view;
  }

  class ListViewItemClickListener implements OnClickListener {
    private View view;
    private int  i;

    public ListViewItemClickListener(View view, int i) {
      this.view = view;
      this.i = i;
    }

    @Override
    public void onClick(View v) {
      changeImageVisable(view, i);
    }

  }

  private void changeImageVisable(View view, int position) {
    if (mLastView != null && mLastPosition != position) {
      ViewHolder holder = (ViewHolder) mLastView.getTag();
      switch (holder.detailItemDelete.getVisibility()) {
      case View.VISIBLE:
        holder.detailItemDelete.setVisibility(View.GONE);
        mLastVisibility = View.GONE;
        break;
      default:
        break;
      }
    }
    mLastPosition = position;
    mLastView = view;
    ViewHolder holder = (ViewHolder) view.getTag();
    switch (holder.detailItemDelete.getVisibility()) {
    case View.GONE:
      holder.detailItemDelete.setVisibility(View.VISIBLE);
      mLastVisibility = View.VISIBLE;
      break;
    case View.VISIBLE:
      holder.detailItemDelete.setVisibility(View.GONE);
      mLastVisibility = View.GONE;
      break;
    }
  }

  public class ViewHolder {
    TextView     tvTitle;
    TextView     tvItemSize;
    TextView     tvDownloadProgress;
    ProgressBar  progressBar;
    Button       btnDownloadStatus;
    ImageView    ivIcon;

    LinearLayout detailItemDelete;
    Button       itemDeleteTask;
    Button       itemDeleteHistory;
  }

  private void initBtn(Button btnDownloadStatus, int position, ProgressBar progressBar, TextView tvItemSize, TextView tvDownloadProgress) {
    DownloadInfo item = downloadTasks.get(position).downloadInfo;
    int state = AppManagerCenter.getAppDownlaodState(item.getPackageName(), item.getVersionCode());
    Log.e("-------------------", "-------download--initBtn-----" + state);
    progressBar.setVisibility(View.VISIBLE);
    tvItemSize.setVisibility(View.VISIBLE);
    switch (state) {
    case AppManagerCenter.APP_STATE_DOWNLOADED:
      btnDownloadStatus.setText(R.string.progress_btn_install);
      progressBar.setVisibility(View.INVISIBLE);
      tvItemSize.setVisibility(View.INVISIBLE);
      tvDownloadProgress.setText(mContext.getResources().getString(R.string.download_completed));
      break;
    case AppManagerCenter.APP_STATE_INSTALLED:
      btnDownloadStatus.setText(R.string.progress_btn_start);
      progressBar.setVisibility(View.INVISIBLE);
      tvItemSize.setVisibility(View.INVISIBLE);
      tvDownloadProgress.setText(mContext.getResources().getString(R.string.download_completed));
      break;
    case AppManagerCenter.APP_STATE_DOWNLOAD_PAUSE:
      btnDownloadStatus.setText(R.string.progress_btn_resume);
      break;
    case AppManagerCenter.APP_STATE_UNEXIST:
      btnDownloadStatus.setText(R.string.progress_btn_download);
      break;
    case AppManagerCenter.APP_STATE_UPDATE:
      btnDownloadStatus.setText(R.string.progress_btn_update);
      break;
    case AppManagerCenter.APP_STATE_DOWNLOADING:
      btnDownloadStatus.setText(R.string.progress_pause);
      break;
    case AppManagerCenter.APP_STATE_WAIT:
      btnDownloadStatus.setText(R.string.progress_btn_wait);
      break;
    }
  }

  class DeleteItemOnClickListener implements OnClickListener {
    int    position;
    Button itemDeleteTask;
    Button itemDeleteHistory;

    public DeleteItemOnClickListener(Button itemDeleteTask, Button itemDeleteHistory) {
      this.itemDeleteTask = itemDeleteTask;
      this.itemDeleteHistory = itemDeleteHistory;
    }

    public void setPosition(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
      case R.id.item_delete_task:
        Log.e("-----", "-----Item delete task-----");

        DeleteDownloadItemFragment fragment = new DeleteDownloadItemFragment(mContext, downloadTasks.get(position).downloadInfo, DownloadListAdapter.this);
        fragment.show(manager, "---");
        break;

      // case R.id.item_delete_history:
      // Log.e("-----", "-----Item delete history-----");
      // downloadTasks.remove(position);
      // DownloadListAdapter.this.notifyDataSetChanged();
      // break;
      }
    }

  }

  // holder.btnDownloadStatus, holder.progressBar, holder.tvItemSize,
  // holder.tvDownloadProgress
  class MyOnClickListener implements View.OnClickListener {
    int         position;
    Button      btnDownloadStatus;
    ProgressBar progressBar;
    TextView    tvItemSize;
    TextView    tvDownloadProgress;

    public MyOnClickListener(Button btnDownloadStatus, ProgressBar progressBar, TextView tvItemSize, TextView tvDownloadProgress) {
      this.btnDownloadStatus = btnDownloadStatus;
      this.progressBar = progressBar;
      this.tvItemSize = tvItemSize;
      this.tvDownloadProgress = tvDownloadProgress;
      initBtn();
    }

    private void initBtn() {
      DownloadInfo item = downloadTasks.get(position).downloadInfo;
      int state = AppManagerCenter.getAppDownlaodState(item.getPackageName(), item.getVersionCode());
      Log.e("-------------------", "-------download--initBtn-----" + state);
      progressBar.setVisibility(View.VISIBLE);
      tvItemSize.setVisibility(View.VISIBLE);
      switch (state) {
      case AppManagerCenter.APP_STATE_DOWNLOADED:
        btnDownloadStatus.setText(R.string.progress_btn_install);
        progressBar.setVisibility(View.INVISIBLE);
        tvItemSize.setVisibility(View.INVISIBLE);
        tvDownloadProgress.setText(mContext.getResources().getString(R.string.download_completed));
        break;
      case AppManagerCenter.APP_STATE_INSTALLED:
        btnDownloadStatus.setText(R.string.progress_btn_start);
        progressBar.setVisibility(View.INVISIBLE);
        tvItemSize.setVisibility(View.INVISIBLE);
        tvDownloadProgress.setText(mContext.getResources().getString(R.string.download_completed));
        break;
      case AppManagerCenter.APP_STATE_DOWNLOAD_PAUSE:
        btnDownloadStatus.setText(R.string.progress_btn_resume);
        break;
      case AppManagerCenter.APP_STATE_UNEXIST:
        btnDownloadStatus.setText(R.string.progress_btn_download);
        break;
      case AppManagerCenter.APP_STATE_UPDATE:
        btnDownloadStatus.setText(R.string.progress_btn_update);
        break;
      case AppManagerCenter.APP_STATE_DOWNLOADING:
        btnDownloadStatus.setText(R.string.progress_pause);
        break;
      case AppManagerCenter.APP_STATE_WAIT:
        btnDownloadStatus.setText(R.string.progress_btn_wait);
        break;
      }
    }

    public void setPosition(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      DownloadInfo item = downloadTasks.get(position).downloadInfo;
      int state = AppManagerCenter.getAppDownlaodState(item.getPackageName(), item.getVersionCode());
      switch (state) {
      case AppManagerCenter.APP_STATE_DOWNLOADED:
        btnDownloadStatus.setText(R.string.progress_btn_install);
        AppManagerCenter.installApk(item);
        break;
      case AppManagerCenter.APP_STATE_INSTALLED:
        btnDownloadStatus.setText(R.string.progress_btn_start);
        UIUtils.startApp(item.getPackageName());
        break;
      case AppManagerCenter.APP_STATE_DOWNLOAD_PAUSE:
        btnDownloadStatus.setText(R.string.progress_pause);
        UIUtils.downloadApp(item);
        break;
      case AppManagerCenter.APP_STATE_UNEXIST:
        btnDownloadStatus.setText(R.string.progress_btn_download);
        UIUtils.downloadApp(item);
        break;
      case AppManagerCenter.APP_STATE_UPDATE:
        btnDownloadStatus.setText(R.string.progress_btn_update);
        UIUtils.downloadApp(item);
        break;
      case AppManagerCenter.APP_STATE_DOWNLOADING:
        btnDownloadStatus.setText(R.string.progress_btn_resume);
        AppManagerCenter.pauseDownload(item, true);
        break;
      case AppManagerCenter.APP_STATE_WAIT:
        btnDownloadStatus.setText(R.string.progress_btn_wait);
        UIUtils.downloadApp(item);
        break;
      }
      DownloadListAdapter.this.notifyDataSetChanged();
    }
  }

}
