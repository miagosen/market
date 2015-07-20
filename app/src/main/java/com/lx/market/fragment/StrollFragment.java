package com.lx.market.fragment;

import java.util.ArrayList;
import java.util.List;

import market.lx.com.R;

import com.lx.market.MarketApplication;
import com.lx.market.adapter.TabPageIndicatorAdapter;
import com.lx.market.network.model.ChannelInfoBto;
import com.lx.market.network.model.TopicInfoBto;
import com.lx.market.network.protocol.GetMarketFrameResp;
import com.lx.market.utils.DialogUtils;
import com.lx.market.utils.Logger;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StrollFragment extends Fragment {

	private TabPageIndicator tbiStroll;
	private ViewPager vpStroll;
	private TabPageIndicatorAdapter strollTabPageIndicatorAdapter;
	private View view;
	private ArrayList<BaseFragment> strollFragments;
	public static final int SCEND_PAGE = 1;
	private Dialog loadingDialog;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {

			view = inflater.inflate(R.layout.oc_stroll, container, false);
			loadData();
			initView(view);

		} else {
			ViewGroup p = (ViewGroup) view.getParent();
			if (p != null) {
				p.removeAllViewsInLayout();
			}
		}
		return view;
	}

	private void initView(View view) {
		tbiStroll = (TabPageIndicator) view
				.findViewById(R.id.stroll_tpi_indicator);
		vpStroll = (ViewPager) view.findViewById(R.id.vp_stroll);
		strollTabPageIndicatorAdapter = new TabPageIndicatorAdapter(0,
				getActivity().getSupportFragmentManager(), getStrollFragments());
		vpStroll.setAdapter(strollTabPageIndicatorAdapter);
		tbiStroll.setViewPager(vpStroll);
	}

	public ArrayList<BaseFragment> getStrollFragments() {
		return strollFragments;
	}

	protected void loadData() {
		loadingDialog = DialogUtils.createWaitingDialog(getActivity(),
				R.string.dlg_waiting);
		loadingDialog.show();
		GetMarketFrameResp resp = MarketApplication.getInstance().marketFrameResp;
		if (resp != null) {
			try {
				
				List<ChannelInfoBto> channelInfoBtos = resp.getChannelList();
				if (channelInfoBtos != null && channelInfoBtos.size() > 0) {
					for (int i = 0; i < channelInfoBtos.size(); i++) {
						if (i == SCEND_PAGE) {
							setChannelStroll(channelInfoBtos.get(SCEND_PAGE));
							
						}
					}
				}
			} catch (Exception e) {
				Logger.p(e);
			}
		}
		loadingDialog.dismiss();
	}

	private void setChannelStroll(ChannelInfoBto channelInfoBto) {
		if (strollFragments == null && channelInfoBto != null) {
			strollFragments = new ArrayList<BaseFragment>();

			for (int i = 0; i < channelInfoBto.getTopicList().size(); i++) {
				TopicInfoBto topicInfo = channelInfoBto.getTopicList().get(i);
				if (i == 0) {
					TopListFragment topListFragment = new TopListFragment();
					topListFragment.setTopicInfoBto(topicInfo);
					strollFragments.add(topListFragment);				

				} else if (i == 1) {
					NewAppsFragment newAppsFragment = new NewAppsFragment();
					newAppsFragment.setTopicInfoBto(topicInfo);
					strollFragments.add(newAppsFragment);
				} else if (i == 2) {
					MyFavoriteFragment myFavoriteFragment = new MyFavoriteFragment();					
					strollFragments.add(myFavoriteFragment);
				}
			}
		}
	}

}
