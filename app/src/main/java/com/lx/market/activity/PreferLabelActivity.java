package com.lx.market.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.lx.market.utils.DensityUtil;
import market.lx.com.R;

public class PreferLabelActivity extends Activity implements OnClickListener{

  
  private TableLayout mTableLayout;
  private LayoutInflater mInflater;
  private RelativeLayout mMale_area;
  private RelativeLayout mFemale_area;
  private View mFemale_normal;
  private View mFemale_click;
  private View mMale_normal;
  private View mMale_click;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.oc_prefer_label_activity);
    
    mTableLayout = (TableLayout) this.findViewById(R.id.prefer_label_content);  
    mInflater = this.getLayoutInflater();
    
    
    
    addRows() ;
    
    
  }
  
  private void addRows()  
  {   String s = "hh/fdfdsfdsa/fasd/fs/fgfdgdfgs/fsdfasg/fsadfagdfgdsg/fgasdfa/gshfdsgfd/fdghhsfhj/fjsghfasdgjh/jfgadshfhadsg/hfjdfhdj/jfdhfasgdasfghsgdf/fytdsyufgsdgf/ygfdsfgh/ghg/fgh";
      String[] strs = s.split("/");
      int margin = DensityUtil.dip2px(this, 4);
      int rows = 0;
      if(strs.length % 4 == 0){
        rows = strs.length / 4;
      }else if(strs.length % 4  > 0){
        rows = (int)(strs.length / 4) + 1;
      }
      
      for (int i = 0; i < rows; i++) {
        TableRow tableRow = new TableRow(this); 
        TableLayout.LayoutParams p = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);     
        tableRow.setGravity(Gravity.LEFT);
        p.leftMargin = margin;
        p.rightMargin = margin;
        tableRow.setLayoutParams(p);
        for (int j = 0; j < ((i<rows-1)?4:strs.length % 4); j++) {
          View view = mInflater.inflate(R.layout.oc_prefer_label_item, null);
          TableRow.LayoutParams param = new TableRow.LayoutParams((DensityUtil.getScreenWidth(this)-2*margin)/4-2*margin, TableRow.LayoutParams.WRAP_CONTENT);
          param.leftMargin = margin;
          param.rightMargin = margin;
          view.setLayoutParams(param);
          TextView label = (TextView) view.findViewById(R.id.prefer_label_word);
          label.setText(strs[i*4+j]);
          label.setOnClickListener(this);
          GradientDrawable ps = (GradientDrawable) label.getBackground();
          ps.setColor(Color.RED);
          tableRow.addView(view);
        }
        
        initSexSelectView();
        mTableLayout.addView(tableRow);   
      }
  }  
  
  public void initSexSelectView(){
    mMale_area = (RelativeLayout) this.findViewById(R.id.male_area);
    mFemale_area = (RelativeLayout) this.findViewById(R.id.female_area);
    mFemale_normal = mInflater.inflate(R.layout.oc_prefer_normal, null);
    mFemale_normal.findViewById(R.id.prefer_sex_icon).setBackgroundResource(R.drawable.icon_female);
    ((TextView)(mFemale_normal.findViewById(R.id.prefer_sex_word1))).setText("I AM A GIRL");
    ((TextView)(mFemale_normal.findViewById(R.id.prefer_sex_word2))).setText("���ǹ���");
    

    mFemale_click =mInflater.inflate(R.layout.oc_prefer_click, null);
    mFemale_click.findViewById(R.id.prefer_sex_icon).setBackgroundResource(R.drawable.icon_female);
    ((TextView)(mFemale_click.findViewById(R.id.prefer_sex_word1))).setText("I AM A GIRL");
    ((TextView)(mFemale_click.findViewById(R.id.prefer_sex_word2))).setText("���ǹ���");
    

    mMale_normal =  mInflater.inflate(R.layout.oc_prefer_normal, null);
    mMale_normal.findViewById(R.id.prefer_sex_icon).setBackgroundResource(R.drawable.icon_male);
    ((TextView)(mMale_normal.findViewById(R.id.prefer_sex_word1))).setText("I AM A BOY");
    ((TextView)(mMale_normal.findViewById(R.id.prefer_sex_word2))).setText("����С��");
    
    mMale_click = mInflater.inflate(R.layout.oc_prefer_click, null);
    mMale_click.findViewById(R.id.prefer_sex_icon).setBackgroundResource(R.drawable.icon_male);
    ((TextView)(mMale_click.findViewById(R.id.prefer_sex_word1))).setText("I AM A BOY");
    ((TextView)(mMale_click.findViewById(R.id.prefer_sex_word2))).setText("����С��");
    
    mMale_area.addView(mMale_normal);
    mFemale_area.addView(mFemale_normal);
    
    mMale_area.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        mMale_area.removeAllViews();
        mMale_area.addView(mMale_click);
        if(mFemale_area.getChildAt(0) != null && mFemale_area.getChildAt(0) != mFemale_normal ){
          mFemale_area.removeAllViews();
          mFemale_area.addView(mFemale_normal);
        }
      }
    });
    
    mFemale_area.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        if(mMale_area.getChildAt(0) != null && mMale_area.getChildAt(0) != mMale_normal){
          mMale_area.removeAllViews();
          mMale_area.addView(mMale_normal);
        }
        mFemale_area.removeAllViews();
        mFemale_area.addView(mFemale_click);
      }
    });
  }

  @Override
  public void onClick(View arg0) {
    // TODO Auto-generated method stub
    if(arg0 != null){
      ViewGroup vg =  (ViewGroup)arg0.getParent();
      View view = vg.getChildAt(1);
      if(view != null){
        view.setVisibility(View.VISIBLE);
      }
      
    }
  }
}
