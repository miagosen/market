package com.lx.market.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.lx.market.adapter.PopularAppInfoAdapter;

/**
 * Created by Antikvo.Miao on 2014/7/28.
 */
public class PopularAppInfoLayout extends LinearLayout {

  private PopularAppInfoAdapter adapter;

  public PopularAppInfoLayout (Context context) {
	super(context);
  }

  public PopularAppInfoLayout (Context context, AttributeSet attrs) {
	super(context, attrs);
  }

  public void setAdapter (PopularAppInfoAdapter adapter) {
	this.adapter = adapter;
	if (adapter != null) {
	  for (int i = 0; i < adapter.getCount(); i++) {
		View view = adapter.getView(i,null,null);
		this.addView(view, new LinearLayout.LayoutParams(
		  LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	  }
	}
  }

  @Override
  public boolean onTouchEvent (MotionEvent paramMotionEvent) {
	if ((paramMotionEvent.getAction() == MotionEvent.ACTION_MOVE)
	  || (paramMotionEvent.getAction() == MotionEvent.ACTION_DOWN)) {
	  getParent().requestDisallowInterceptTouchEvent(true);
	} else if ((paramMotionEvent.getAction() == MotionEvent.ACTION_CANCEL)
	  || (paramMotionEvent.getAction() == MotionEvent.ACTION_UP)) {
	  getParent().requestDisallowInterceptTouchEvent(false);
	}
	return super.onTouchEvent(paramMotionEvent);
  }

  @Override
  public boolean onInterceptTouchEvent (MotionEvent ev) {
	if ((ev.getAction() == MotionEvent.ACTION_MOVE)
	  || (ev.getAction() == MotionEvent.ACTION_DOWN)) {
	  getParent().requestDisallowInterceptTouchEvent(true);
	}
	return super.onInterceptTouchEvent(ev);
  }

}
