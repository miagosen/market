/**
 *
 */
package com.lx.market.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.lx.market.network.model.AppInfoBto;

import java.util.List;

public abstract class BaseAppAdapter extends BaseAdapter {
	protected List<AppInfoBto> mAppList;
	protected Context mContext;
	private boolean isHasHeader;

	public BaseAppAdapter(Context context, List<AppInfoBto> appList) {
		this.mContext = context;
		mAppList = appList;
	}

	public BaseAppAdapter(boolean isHasHeader, Context context,
			List<AppInfoBto> appList) {
		this.isHasHeader = isHasHeader;
		this.mContext = context;
		mAppList = appList;
	}

	@Override
	public final int getCount() {
		return mAppList == null ? 0 : mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAppList == null ? null : mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
