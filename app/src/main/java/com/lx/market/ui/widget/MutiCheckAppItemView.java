package com.lx.market.ui.widget;

import market.lx.com.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MutiCheckAppItemView extends FrameLayout {

  private CheckBox       cb;
  private TextView       tv;
  private ImageView      iv;
  private RelativeLayout rl;

  public MutiCheckAppItemView(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater.from(context).inflate(R.layout.muti_check_app_item, this);
    getView();
    setListener();
  }

  private void getView() {
    rl = (RelativeLayout) findViewById(R.id.rl_muti_check);
    cb = (CheckBox) findViewById(R.id.cb_muti_check);
    tv = (TextView) findViewById(R.id.tv_muti_check);
    iv = (ImageView) findViewById(R.id.iv_muti_check);
  }

  private void setListener() {
    rl.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        cb.setChecked(true);
      }
    });
  }

  public void setItemImageView(int resId) {
    iv.setImageResource(resId);
  }
  public void setItemImageView(Bitmap bitmap) {
    iv.setImageBitmap(bitmap);
  }

  public void setItemTextView(String text) {
    tv.setText(text);
  }

  public void setItemViewListener(OnClickListener listener) {
    rl.setOnClickListener(listener);
  }

}
