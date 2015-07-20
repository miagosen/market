package com.lx.market.activity;

import market.lx.com.R;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.lx.market.adapter.DownloadManagerListAdapter;

public class DownloadActivity extends ActionBarActivity {

  private ListView                   lvDownloadManager;
  private DownloadManagerListAdapter downloadListAdapter;
  private TextView                   tvDownloadTaskNum;
  private FragmentManager            manager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.oc_activity_download);
    manager = getSupportFragmentManager();
    tvDownloadTaskNum = (TextView) findViewById(R.id.oc_activity_download_title_num);
    lvDownloadManager = (ListView) findViewById(R.id.lv_download_apps);
    downloadListAdapter = new DownloadManagerListAdapter(this, tvDownloadTaskNum, manager);
    lvDownloadManager.setAdapter(downloadListAdapter);
  }

  @Override
  protected void onResume() {
    downloadListAdapter.setDownlaodRefreshHandle();
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    downloadListAdapter.removeDownLoadHandler();
    super.onDestroy();
  }

}
