package com.lx.market.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
  private List<Fragment> fragmentList;

  public FragmentViewPagerAdapter(FragmentManager fm,
      List<Fragment> fragmentList) {
    super(fm);
    this.fragmentList = fragmentList;
  }

  @Override
  public Fragment getItem(int arg0) {
    // TODO Auto-generated method stub
    return fragmentList.get(arg0);
  }

  @Override
  public int getCount() {
    // TODO Auto-generated method stub
    return fragmentList.size();
  }

}
