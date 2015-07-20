package com.lx.market.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lx.market.fragment.BaseFragment;
import com.viewpagerindicator.IconPagerAdapter;
import market.lx.com.R;

/**
 * Created by Antikvo.Miao on 2014/8/21.
 */
public class TabPageIndicatorAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    private List<BaseFragment> fragments;
    private int id;
    private int[] ICONS_0 = {
    		R.drawable.tab_top_list,            
            R.drawable.tab_the_new,
            R.drawable.tab_my_fav
            
    };

    private int[] ICONS_1 = {
            R.drawable.tab_download,
            R.drawable.tab_update_apps
    };

    public TabPageIndicatorAdapter(int id, FragmentManager fm, ArrayList<BaseFragment> strollFragments) {
        super(fm);
        this.fragments = strollFragments;
        this.id = id;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public void setFragments(ArrayList<BaseFragment> fragments) {
        this.fragments = fragments;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public int getIconResId(int index) {
        if (id == 0)
            return ICONS_0[index];
        else
            return ICONS_1[index];
    }


}
