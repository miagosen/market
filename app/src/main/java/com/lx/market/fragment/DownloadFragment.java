package com.lx.market.fragment;

import market.lx.com.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lx.market.adapter.DownloadListAdapter;

/**
 * Created by Antikvo.Miao on 2014/8/21.
 */

public class DownloadFragment extends BaseFragment {

  private View                view;
  private ListView            lvDownloadManager;
  private DownloadListAdapter downloadListAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    if (view == null) {
      view = inflater.inflate(R.layout.oc_download_manager_list, container, false);
      lvDownloadManager = (ListView) view.findViewById(R.id.lv_download_manager);
      downloadListAdapter = new DownloadListAdapter(getActivity(), getFragmentManager());
      lvDownloadManager.setAdapter(downloadListAdapter);
    } else {
      ViewGroup p = (ViewGroup) view.getParent();
      if (p != null) {
        p.removeAllViewsInLayout();
      }
    }

    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

}
