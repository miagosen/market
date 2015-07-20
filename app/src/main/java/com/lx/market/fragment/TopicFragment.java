package com.lx.market.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import market.lx.com.R;

/**
 * Created by Antikvo.Miao on 2015/7/20.
 */
public class TopicFragment extends BaseFragment {

    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.oc_activity_home, container, false);
            initViews();
            loadData();
        } else {
            ViewGroup p = (ViewGroup) contentView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
        }
        return contentView;
    }


    private void loadData() {
    }

    private void initViews() {
    }
}
