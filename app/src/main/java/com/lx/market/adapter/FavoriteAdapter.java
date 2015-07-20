package com.lx.market.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lx.market.MarketApplication;
import com.lx.market.activity.AssemblyActivity;
import com.lx.market.constants.BundleConstants;
import com.lx.market.model.CircleLabel;
import com.lx.market.ui.widget.CircleView;

import java.util.List;

/**
 * Created by Antikvo.Miao on 2014/9/11.
 */
public class FavoriteAdapter extends BaseAdapter {
  private List<CircleLabel> circleLabels;
  private Context mContext;

  public FavoriteAdapter (Context context, List<CircleLabel> list) {
	this.circleLabels = list;
	this.mContext = context;
  }

  @Override
  public int getCount () {
	return circleLabels == null ? 0 : circleLabels.size();
  }

  @Override
  public Object getItem (int position) {
	return circleLabels == null ? null : circleLabels.get(position);
  }

  @Override
  public long getItemId (int position) {
	return position;
  }

  ViewHolder viewHolder;

  public List<CircleLabel> getCircleLabels () {
	return circleLabels;
  }

  public void setCircleLabels (List<CircleLabel> circleLabels) {
	this.circleLabels = circleLabels;
  }

  @Override
  public View getView (final int position, View convertView, ViewGroup parent) {
	if (convertView == null) {
	  viewHolder = new ViewHolder();
	  convertView = new CircleView(mContext);
	  viewHolder.circleView = (CircleView) convertView;
	  convertView.setTag(viewHolder);
	} else {
	  viewHolder = (ViewHolder) convertView.getTag();
	}
	viewHolder.circleView.setCircleLabel(circleLabels.get(position));
	viewHolder.circleView.setOnClickListener(new View.OnClickListener() {
	  @Override
	  public void onClick (View v) {
		Intent intent = new Intent(MarketApplication.curContext, AssemblyActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(BundleConstants.BUNDLE_ASSEMBLY_ID, 306);
		mContext.startActivity(intent);
	  }
	});
	return convertView;
  }

  class ViewHolder {
	CircleView circleView;
  }
}
