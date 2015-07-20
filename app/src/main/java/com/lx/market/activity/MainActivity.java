package com.lx.market.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.lx.market.adapter.FragmentViewPagerAdapter;
import com.lx.market.fragment.HomeFragment;
import com.lx.market.fragment.NewAppsFragment;
import com.lx.market.fragment.TopicFragment;
import com.lx.market.fragment.UserFragment;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import market.lx.com.R;


/**
 * Created by Antikvo.Miao on 2014/10/20.
 */
public class MainActivity extends BaseActivity implements OnTabChangeListener {

    private FragmentTabHost mTabHost;


    //    private FragmentTabHost tabHost;
    private String currentTabId;
    private View currentView;

    private ViewPager viewPager;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oc_activity_main);
        init();
    }

    private void init() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }
        initTabs();
        initViewPager();
    }

    private void initTabs() {
        MainTab[] tabs = MainTab.values();
        final int size = tabs.length;
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new TopicFragment());
        fragmentList.add(new NewAppsFragment());
        fragmentList.add(new TopicFragment());
        fragmentList.add(new UserFragment());
        for (int i = 0; i < size; i++) {
            MainTab mainTab = tabs[i];
            TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator = inflateView(R.layout.oc_tab_indicator);
            TextView title = (TextView) indicator.findViewById(R.id.tab_titile);
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);
            tab.setContent(new TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });

            mTabHost.addTab(tab, mainTab.getClz(), null);
            mTabHost.setBackgroundColor(getResources().getColor(R.color.app_title_background_color));
            // if (mainTab.equals(MainTab.ME)) {
            // View con = indicator.findViewById(R.id.container);
            // // con.setBackgroundColor(Color.parseColor("#00ff00"));
            // mBvTweet = new BadgeView(this, con);
            // mBvTweet.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            // mBvTweet.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            // mBvTweet.setBackgroundResource(R.drawable.tab_notification_bg);
            // }
        }
        currentTabId = getString(tabs[0].getResName());
//        currentView = tabs[0];
        mTabHost.setOnTabChangedListener(this);
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.itemViewPager);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageSelected(int arg0) {
                mTabHost.setCurrentTab(arg0);
            }

        });

        viewPager.setAdapter(new FragmentViewPagerAdapter(
                getSupportFragmentManager(), fragmentList));
        UnderlinePageIndicator mIndicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);
    }

    @Override
    public void onTabChanged(String tabId) {
//      Log.e("yys", "tabId = "+tabId);
//        final int size = mTabHost.getTabWidget().getTabCount();
//        for (int i = 0; i < size; i++) {
//            View v = mTabHost.getTabWidget().getChildAt(i);
//            if (i == mTabHost.getCurrentTab()) {
//                v.findViewById(R.id.tab_icon).setVisibility(View.VISIBLE);
//            } else {
//                v.findViewById(R.id.tab_icon).setVisibility(View.GONE);
//            }
//        }
        viewPager.setCurrentItem(mTabHost.getCurrentTab());
//        supportInvalidateOptionsMenu();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.main_menu_search:
////      UIHelper.showSearch(this);
//                startActivity(new Intent(MainActivity.this, SearchAppActivity.class));
//                break;
//            case R.id.main_menu_settings:
//                // showMoreOptionMenu(findViewById(R.id.main_menu_more));
//                startActivity(new Intent(MainActivity.this, SettingActivity.class));
//                break;
//            case R.id.main_menu_download:
//                mTabHost.setCurrentTabByTag(getResources().getString(R.string.oc_btm_home));
//                break;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    protected void loadData() {

    }
}
