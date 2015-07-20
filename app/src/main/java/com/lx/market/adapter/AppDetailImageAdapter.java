package com.lx.market.adapter;

import market.lx.com.R;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lx.market.MarketApplication;

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
      vh.iv = (ImageView) v.findViewById(R.id.iv_app_detail_image);
      v.setTag(vh);
      pagers.put(position, v);
    } else {
      vh = (ViewHolder) v.getTag();
    }
    MarketApplication.getInstance().imageLoader.displayImage(data[position], vh.iv);
    return v;
  }

  class ViewHolder {
    ImageView iv;
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
