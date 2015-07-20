package com.lx.market.ui.dialog;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.lx.market.activity.UpdateDialogListener;

public class UpdateDialogFragment extends DialogFragment {

  private final String               title;
  private final String               content;
  private final UpdateDialogListener listener;
  private final Context              context;

  // private Button btnCancel;
  // private Button btnYes;

  public UpdateDialogFragment(Context context, String title, String content, UpdateDialogListener listener) {
    this.title = title;
    this.content = content;
    this.context = context;
    this.listener = listener;
  }

  // @Override
  // public View onCreateView(LayoutInflater inflater, ViewGroup container,
  // Bundle savedInstanceState) {
  // View view = inflater.inflate(R.layout.oc_dialog_fragment, null);
  // btnCancel = (Button) view.findViewById(R.id.btn_dialog_fragment_cancle);
  // btnYes = (Button) view.findViewById(R.id.btn_dialog_fragment_yes);
  // return view;
  // }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Builder builder = new Builder(context);
    builder.setTitle(title).setMessage(content).setPositiveButton("Yse", new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        listener.onDialogPositiveClick();
      }
    }).setNegativeButton("No", new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        listener.onDialogNegativeClick();
      }
    });
    return builder.create();
  }

}
