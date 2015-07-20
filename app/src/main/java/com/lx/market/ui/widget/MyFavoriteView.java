package com.lx.market.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.lx.market.adapter.FavoriteAdapter;
import com.lx.market.utils.Logger;

import java.util.Random;

/**
 * Created by Antikvo.Miao on 2014/8/31.
 */
public class MyFavoriteView extends ViewGroup {
  private static final String TAG = "MyFavoriteView";
  private Scroller mScroller;
  private VelocityTracker mVelocityTracker;
  private boolean canScroll;
  private FavoriteAdapter adapter;
  private GestureDetector mGestureDetector;
  private Random random = new Random();
  private static final int TOUCH_STATE_REST = 0;
  private static final int TOUCH_STATE_SCROLLING = 1;
  private int mTouchState = TOUCH_STATE_REST;
  private int mTouchSlop;
  private float mLastMotionX;
  private float mLastMotionY;
  private int lastHeight = 0;
  public MyFavoriteView (Context context) {
	super(context);
	mScroller = new Scroller(context);
	mGestureDetector = new GestureDetector(context, new YScrollDetector());
  }

  public MyFavoriteView (Context context, AttributeSet attrs) {
	super(context, attrs);
	mScroller = new Scroller(context);
	mGestureDetector = new GestureDetector(context, new YScrollDetector());
  }

  public void setAdapter (FavoriteAdapter adapter) {
	this.adapter = adapter;
	if (adapter == null) {
	  Logger.e(TAG, "adapter is null");
	  return;
	}
	for (int i = 0; i < adapter.getCount(); i++) {
	  View view = adapter.getView(i, null, null);
	  this.addView(view);
	}
  }

  @Override
  public void computeScroll () {
	if (mScroller.computeScrollOffset()) {
	  scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
	  postInvalidate();
	}
	super.computeScroll();
  }

  @Override
  protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	final int height = MeasureSpec.getSize(heightMeasureSpec);
	setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	final int count = getChildCount();
	for (int i = 0; i < count; i++) {
	  getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
	}
  }
  @Override
  public boolean onTouchEvent (MotionEvent event) {
	if (mVelocityTracker == null) {
	  mVelocityTracker = VelocityTracker.obtain();
	}
	mVelocityTracker.addMovement(event);
	final int action = event.getAction();
	final float x = event.getX();
	final float y = event.getY();
	switch (action) {
	  case MotionEvent.ACTION_DOWN:
		if (!mScroller.isFinished()) {
		  mScroller.abortAnimation();
		}
		mLastMotionX = x;
		mLastMotionY = y;
		getParent().requestDisallowInterceptTouchEvent(true);
		break;
	  case MotionEvent.ACTION_MOVE:
		int deltaX = (int) (mLastMotionX - x);
		int deltaY = (int) (mLastMotionY - y);
		if (Math.abs(deltaX)>20  && Math.abs(deltaX) >Math.abs(deltaY)) {
		  getParent().requestDisallowInterceptTouchEvent(false);
		  return super.onTouchEvent(event);
		}
		mLastMotionY = y;
		mLastMotionX = x;
		int height = getHeight();
		if ((this.getScrollY() <= 0 && deltaY <= 0) || (this.getScrollY() > 0 && (this.getScrollY() + height + deltaY) >= lastHeight + 20)) {
		  break;
		}
		scrollBy(0, deltaY);
		break;
	  case MotionEvent.ACTION_UP:
		final VelocityTracker velocityTracker = mVelocityTracker;
		velocityTracker.computeCurrentVelocity(1000);
		if (mVelocityTracker != null) {
		  mVelocityTracker.recycle();
		  mVelocityTracker = null;
		}
		mTouchState = TOUCH_STATE_REST;
		break;
	  case MotionEvent.ACTION_CANCEL:
		mTouchState = TOUCH_STATE_REST;
		break;
	}
	return  true;
  }
  @Override
  public boolean onInterceptTouchEvent (MotionEvent ev) {
	final int action = ev.getAction();
	if ((action == MotionEvent.ACTION_MOVE)
	  && (mTouchState != TOUCH_STATE_REST)) {
	  return true;
	}
	final float x = ev.getX();
	final float y = ev.getY();
	switch (action) {
	  case MotionEvent.ACTION_MOVE:
		final int xDiff = (int) Math.abs(mLastMotionX - x);
		if (xDiff > mTouchSlop) {
		  mTouchState = TOUCH_STATE_SCROLLING;
		}
		break;
	  case MotionEvent.ACTION_DOWN:
		mLastMotionX = x;
		mLastMotionY = y;
		mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
		  : TOUCH_STATE_SCROLLING;
		break;
	  case MotionEvent.ACTION_CANCEL:
	  case MotionEvent.ACTION_UP:
		mTouchState = TOUCH_STATE_REST;
		break;
	}
	if (ev.getAction() == MotionEvent.ACTION_UP)
	  canScroll = true;
	return  mGestureDetector.onTouchEvent(ev);
  }

  @Override
  protected void onLayout (boolean changed, int l, int t, int r, int b) {
	// 累加每个视图的高度，便于下一个视图获取top顶点值
	int childCount = getChildCount();
	lastHeight = 0;
	int centerX;
	int centerY;
	int baseLine = 0;
	int lastWidth = 0;
	boolean isNewLine;
	for (int i = 0; i < childCount; i++) {
	  View childView = getChildAt(i);
	  int measuredWidth = childView.getMeasuredWidth();
	  int radius = measuredWidth / 2;
	  if (lastWidth == 0 || r - l - lastWidth < 2 * radius) {
		isNewLine = true;
		lastWidth = 0;
	  } else {
		isNewLine = false;
	  }
	  if (isNewLine) {
		centerX = random.nextInt(measuredWidth / 2) + radius;
		centerY = lastHeight + random.nextInt(radius * 2 / 3) + radius;
		childView.layout(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
		baseLine = lastHeight;
		lastHeight = centerY + radius;
		lastWidth = centerX + radius;
	  } else {
		centerX = lastWidth + radius + random.nextInt((r - l - lastWidth - 2 * radius) < 0 ? 0 : (r - l - lastWidth - 2 * radius));
		centerY = baseLine + random.nextInt(radius * 2 / 3) + radius;
		childView.layout(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
		lastHeight = Math.max(lastHeight, centerY + radius);
		lastWidth = centerX + radius;
	  }
	}
  }

  class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
	@Override
	public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	  if (canScroll) {
		if (Math.abs(distanceY) >= Math.abs(distanceX)) {
		  getParent().requestDisallowInterceptTouchEvent(true);
		  canScroll = true;
		} else {
		  getParent().requestDisallowInterceptTouchEvent(false);
		  canScroll = false;
		}
	  }
	  return canScroll;
	}
  }
}
