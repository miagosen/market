package com.lx.market.ui.widget;

import market.lx.com.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lx.market.activity.OCMarketAdActivity;
import com.lx.market.utils.FloatWindowManager;

public class FloatWindowBigView extends LinearLayout {

  /**
   * 记录大悬浮窗的宽度
   */
  public static int viewWidth;

  /**
   * 记录大悬浮窗的高度
   */
  public static int viewHeight;

  public FloatWindowBigView(final Context context) {
    super(context);
    LayoutInflater.from(context).inflate(R.layout.oc_float_window_big, this);
    View view = findViewById(R.id.big_window_layout);
    viewWidth = view.getLayoutParams().width;
    viewHeight = view.getLayoutParams().height;
    Button close = (Button) findViewById(R.id.close);
    Button back = (Button) findViewById(R.id.back);
    close.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
        // FloatWindowManager.removeBigWindow(context);
        // FloatWindowManager.removeSmallWindow(context);
        // Intent intent = new Intent(getContext(), FloatWindowService.class);
        // context.stopService(intent);
        Intent intent = new Intent(getContext(), OCMarketAdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
      }
    });
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        // 点击返回的时候，移除大悬浮窗，创建小悬浮窗
        FloatWindowManager.removeBigWindow(context);
        FloatWindowManager.createSmallWindow(context);
      }
    });
  }
}
