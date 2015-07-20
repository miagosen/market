package com.lx.market.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import market.lx.com.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lx.market.MarketApplication;
import com.lx.market.adapter.FavoriteAdapter;
import com.lx.market.model.CircleLabel;
import com.lx.market.ui.widget.MyFavoriteView;

/**
 * Created by Antikvo.Miao on 2014/8/25.
 */
public class MyFavoriteFragment extends BaseFragment {
  private View              view;
  private MyFavoriteView    myFavoriteView;
  private FavoriteAdapter   adapter;
  private List<CircleLabel> circleLabels = new ArrayList<CircleLabel>();
  private int[]             circleColors;
  private String[]          labels;
  private int[]             radius;
  private Random            random       = new Random();

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (view == null) {
      circleColors = getResources().getIntArray(R.array.circle_colors);
      radius = getResources().getIntArray(R.array.radius);
      labels = getResources().getStringArray(R.array.select_label);
      getMockData();
      view = inflater.inflate(R.layout.my_favorite_fragment, container, false);
      myFavoriteView = (MyFavoriteView) view.findViewById(R.id.my_favorite_view);
      adapter = new FavoriteAdapter(MarketApplication.curContext, circleLabels);
      myFavoriteView.setAdapter(adapter);
    } else {
      ViewGroup p = (ViewGroup) view.getParent();
      if (p != null) {
        p.removeAllViewsInLayout();
      }
    }
    return view;
  }

  private void getMockData() {
    Random r = new Random();
    for (int i = 0; i < 10; i++) {
      CircleLabel circleLabel = new CircleLabel(labels[i], radius[r.nextInt(radius.length)]);
      circleLabel.setCircleColor(circleColors[random.nextInt(circleColors.length)]);
      circleLabels.add(circleLabel);
    }
  }

  public MyFavoriteFragment() {
  }

}
