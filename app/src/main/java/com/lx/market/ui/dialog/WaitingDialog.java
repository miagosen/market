package com.lx.market.ui.dialog;

import market.lx.com.R;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * 自定义进度条,当发起网络请求时显示该对话框，让用户等待
 */
public class WaitingDialog extends DialogFragment {

  private static final String MESSAGE = "message";

  public static WaitingDialog newInstance(String message) {
    WaitingDialog dialog = new WaitingDialog();
    Bundle bundle = new Bundle();
    bundle.putString(MESSAGE, message);
    dialog.setArguments(bundle);
    return dialog;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle_Waiting);
    dialog.setContentView(R.layout.dlg_waiting);
    dialog.setCanceledOnTouchOutside(false);
    String message = getArguments().getString(MESSAGE);
    if (!TextUtils.isEmpty(message)) {
      ((TextView) dialog.findViewById(R.id.dialog_waiting_message)).setText(message);
    }
    return dialog;
  }
}
