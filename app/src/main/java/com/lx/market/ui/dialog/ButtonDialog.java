package com.lx.market.ui.dialog;

import market.lx.com.R;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lx.market.activity.BaseActivity;

public abstract class ButtonDialog<T> extends DialogFragment implements OnClickListener {

  /**
   * 标题
   */
  public static final String TITLE                     = "title";
  /**
   * 确认键
   */
  public static final String NEGATIVE_BUTTON           = "negative_button";
  /**
   * 取消键
   */
  public static final String POSITIVE_BUTTON           = "positive_button";

  /**
   * 点击区域外消失
   */
  public static final String CANCELED_ON_TOUCH_OUTSIDE = "canceled_on_touch_outside";

  private Button             ok;
  private Button             cancel;

  @Override
  public final Dialog onCreateDialog(Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    String title = bundle.getString(TITLE);
    String positiveValue = bundle.getString(POSITIVE_BUTTON);
    String negativeValue = bundle.getString(NEGATIVE_BUTTON);
    boolean canceledOnTouchOutside = bundle.getBoolean(CANCELED_ON_TOUCH_OUTSIDE, false);

    Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
    dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    dialog.setContentView(R.layout.dlg_frame);
    // 标题
    TextView titleView = (TextView) dialog.findViewById(R.id.dlg_title_tv);
    titleView.setText(title);
    // 内容区域
    ViewGroup content = (ViewGroup) dialog.findViewById(R.id.dlg_content_rl);
    createContent(content);
    // 按钮1
    ok = (Button) dialog.findViewById(R.id.dlg_ok_btn);
    ok.setOnClickListener(this);
    // 按钮2
    cancel = (Button) dialog.findViewById(R.id.dlg_cancel_btn);
    cancel.setOnClickListener(this);
    setButtonText(positiveValue, negativeValue);
    return dialog;
  }

  /**
   * 设置按钮文字,可以为null
   * 
   * @param positiveValue
   * @param negativeValue
   */
  public void setButtonText(String positiveValue, String negativeValue) {
    if (TextUtils.isEmpty(negativeValue)) {
      cancel.setVisibility(View.GONE);
    } else {
      cancel.setText(negativeValue);
      cancel.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(positiveValue)) {
      ok.setVisibility(View.GONE);
    } else {
      ok.setText(positiveValue);
      ok.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    BaseActivity activity = (BaseActivity) getActivity();
    if (null == activity) {
      return;
    }
    switch (id) {
    case R.id.dlg_ok_btn:
      dismissAllowingStateLoss();
      break;
    case R.id.dlg_cancel_btn:
      dismissAllowingStateLoss();
      break;
    }
  }

  /**
   * 为 Activity 提供返回值
   * 
   * @return
   */
  protected abstract T getResult();

  /**
   * 设置 对话框显示内容<br/>
   * 把要显示的内容 添加到 content 即可
   * 
   * @param content
   */
  protected abstract void createContent(ViewGroup content);

}
