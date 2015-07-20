package com.lx.market.activity;

import com.lx.market.fragment.HomeFragment;
import com.lx.market.fragment.StrollFragment;
import com.lx.market.fragment.UserFragment;

import market.lx.com.R;

public enum MainTab {

  MAIN_TAB(0, R.string.oc_btm_recommend, R.drawable.oc_btm_recommend_bg, HomeFragment.class),
  STROLL_TAB(1, R.string.oc_btm_new, R.drawable.oc_btm_stroll_bg, StrollFragment.class),
  NEW_TOP_TAB(2, R.string.oc_btm_new_top, R.drawable.oc_btm_stroll_bg, StrollFragment.class),
  TOPIC_TAB(3, R.string.oc_btm_topic, R.drawable.oc_btm_stroll_bg, StrollFragment.class),
  MINE_TAB(4, R.string.oc_btm_manager, R.drawable.oc_btm_home_bg, UserFragment.class);

  private int      idx;
  private int      resName;
  private int      resIcon;
  private Class<?> clz;

  private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
    this.idx = idx;
    this.resName = resName;
    this.resIcon = resIcon;
    this.clz = clz;
  }

  public int getIdx() {
    return idx;
  }

  public void setIdx(int idx) {
    this.idx = idx;
  }

  public int getResName() {
    return resName;
  }

  public void setResName(int resName) {
    this.resName = resName;
  }

  public int getResIcon() {
    return resIcon;
  }

  public void setResIcon(int resIcon) {
    this.resIcon = resIcon;
  }

  public Class<?> getClz() {
    return clz;
  }

  public void setClz(Class<?> clz) {
    this.clz = clz;
  }
}
