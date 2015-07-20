package com.lx.market.ui.dialog;

import market.lx.com.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.lx.market.adapter.DownloadListAdapter;
import com.lx.market.download.AppManagerCenter;
import com.lx.market.model.DownloadInfo;

public class DeleteDownloadItemFragment extends DialogFragment {

  private Context             mContext;
  private Button              yesButton;
  private Button              noButton;
  private AlertDialog         dialog;

  private DownloadInfo        downloadInfo;
  private DownloadListAdapter downloadListAdapter;

  public DeleteDownloadItemFragment(Context mContext, DownloadInfo downloadInfo, DownloadListAdapter downloadListAdapter) {
    this.mContext = mContext;
    this.downloadInfo = downloadInfo;
    this.downloadListAdapter = downloadListAdapter;
  }

  class DeleteItemListener implements OnClickListener {

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
      case R.id.dialog_cancel:
        dialog.dismiss();
        break;
      case R.id.dialog_yes:
        AppManagerCenter.cancelDownload(downloadInfo);
        downloadListAdapter.notifyDataSetChanged();
        dialog.dismiss();
        break;
      }
    }
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    dialog = builder.create();
    dialog.show();
    Window window = dialog.getWindow();
    window.setContentView(R.layout.oc_delete_dialog_fragment);
    yesButton = (Button) window.findViewById(R.id.dialog_yes);
    noButton = (Button) window.findViewById(R.id.dialog_cancel);
    yesButton.setOnClickListener(new DeleteItemListener());
    noButton.setOnClickListener(new DeleteItemListener());
    return dialog;
  }

}
