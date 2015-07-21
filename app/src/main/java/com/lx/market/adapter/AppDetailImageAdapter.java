package com.lx.market.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lx.market.MarketApplication;
import com.lx.market.ui.photoview.PhotoView;

import market.lx.com.R;

public class AppDetailImageAdapter extends PagerAdapter {
  private final Context           context;
  private final String[]          data;
  private final SparseArray<View> pagers = new SparseArray<View>();

  public AppDetailImageAdapter(Context context, String[] data) {
    this.context = context;
    this.data = data;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    ViewPager pager = (ViewPager) container;
    View view = getView(position, pager);
    pager.addView(view);
    return view;
  }
  @Override
  public void destroyItem(ViewGroup container, int position, Object view) {
    ((ViewPager) container).removeView((View) view);
  }

  public View getView(int position, ViewPager pager) {
    ViewHolder vh;
    View v = pagers.get(position);
    if (v == null) {
      vh = new ViewHolder();
      v = LayoutInflater.from(context).inflate(R.layout.oc_app_detail_image_item, null);
      vh.iv = (PhotoView) v.findViewById(R.id.iv_app_detail_image);
      v.setTag(vh);
      pagers.put(position, v);
    } else {
      vh = (ViewHolder) v.getTag();
    }
    // 需要使用 ControllerBuilder 方式请求图片
    MarketApplication.getInstance().imageLoader.displayImage(data[position], vh.iv);
    return v;
  }

  class ViewHolder {
    PhotoView iv;
  }

  @Override
  public int getCount() {
    return data == null ? 0 : data.length;
  }

  @Override
  public boolean isViewFromObject(View arg0, Object arg1) {
    return arg0 == arg1;
  }

}
