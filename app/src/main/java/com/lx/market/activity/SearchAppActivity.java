package com.lx.market.activity;

import java.util.ArrayList;
import java.util.List;

import market.lx.com.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lx.market.MarketApplication;
import com.lx.market.adapter.AppListAdapter;
import com.lx.market.entities.LocAppInfoBto;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.connection.OcHttpConnection;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.model.KeyWordInfoBto;
import com.lx.market.network.model.PackageInfoBto;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.protocol.GetStaticSearchAppReq;
import com.lx.market.network.protocol.GetStaticSearchAppResp;
import com.lx.market.network.protocol.SearchAppReq;
import com.lx.market.network.protocol.SearchAppResp;
import com.lx.market.network.utils.TerminalInfoUtil;
import com.lx.market.utils.DensityUtil;
import com.lx.market.utils.Logger;

public class SearchAppActivity extends Activity implements OnClickListener {
    public final static String Tag = "SearchAppActivity";
    public final static int SEARCH_RECOMMEND_APPINFO = 0;
    public final static int SEARCH_REFRESH_RECOMMEND_VIEW = 0;
    public final static int SEARCH_REFRESH_LIST_VIEW = 1;
    private int mRowWidth = -1;
    private int mRowHight = -1;
    private int mRowLeftMargin = -1;
    private int mTextHight = -1;
    private float mTextSize = -1;
    private int mTexTRightMargin = -1;
    private int mTextPadding = -1;
    private int len = 0;
    private int mPosition = -1;


    private LayoutInflater mInflater;
    private ImageView mSearch;
    private EditText mEditText;
    private ImageView mClean;
    private RelativeLayout mRela_content;
    private View mRecommend_content;
    private View mSearch_content;

//    private GetStaticSearchAppResp recommendResp;
    private SearchAppResp searchAppListResp;
    private List<LocAppInfoBto> appLst;
    private  String keys = "";

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SEARCH_REFRESH_RECOMMEND_VIEW:
                    refreshCommendView(appLst,keys);
                    break;
                case SEARCH_REFRESH_LIST_VIEW:
                    refreshAppLstView(searchAppListResp);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.oc_search_app_activity);

        mRowWidth = DensityUtil.getScreenWidth(this) - DensityUtil.dip2px(this, 6);
        mRowHight = DensityUtil.dip2px(this, 44);
        mRowLeftMargin = DensityUtil.dip2px(this, 12);
        mTextHight = DensityUtil.dip2px(this, 30);
        mTextPadding = DensityUtil.dip2px(this, 5);
        mTextSize = 12;
        mTexTRightMargin = mRowLeftMargin;

        mInflater = this.getLayoutInflater();
        initView();
    }

    private void initView() {
        mRela_content = (RelativeLayout) findViewById(R.id.search_content);
        mSearch = (ImageView) findViewById(R.id.search_search_button);
        mEditText = (EditText) findViewById(R.id.search_edit);
        mClean = (ImageView) findViewById(R.id.search_eidt_cancel);
        mClean.setVisibility(View.GONE);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(mEditText.getText().toString())) {
                    mClean.setVisibility(View.VISIBLE);
                } else {
                    mClean.setVisibility(View.GONE);
                    mRela_content.removeAllViews();
                    if(mRecommend_content != null){
                        ((ViewGroup)mRela_content).addView(mRecommend_content);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mClean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
                mRela_content.removeAllViews();
                if(mRecommend_content != null){
                    ((ViewGroup)mRela_content).addView(mRecommend_content);
                }
            }
        });

        mSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mEditText.getText())) {
                    searchApp(mEditText.getText().toString());
                }
            }
        });
        List<LocAppInfoBto> applt = null;
        try {
            applt = MarketApplication.getInstance().dbUtils.findAll(Selector.from(LocAppInfoBto.class).where(WhereBuilder.b("sType","=",SEARCH_RECOMMEND_APPINFO)));
        }catch (Exception ext){

        }
        SharedPreferences spf = this.getSharedPreferences("CommonData", Context.MODE_PRIVATE);
        String keyWords = spf.getString("Search_Recommend_keys" ,"");
        refreshCommendView(applt,keyWords);

        final GetStaticSearchAppReq req = new GetStaticSearchAppReq();
        OcHttpConnection httpConnection = new OcHttpConnection(this);
        req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(this));
        req.setMarketId("34");
        req.setInstallList(new ArrayList<PackageInfoBto>());
        req.setUnInstallList(new ArrayList<PackageInfoBto>());
        httpConnection.sendRequest(RequestType.MARKET_DATA, req, GetStaticSearchAppResp.class, new OcHttpReqCallBack() {
            @Override
            public void onResponse(boolean result, Object respOrMsg) {
               if(result){
                   GetStaticSearchAppResp recommendResp = (GetStaticSearchAppResp)respOrMsg;
                   if(recommendResp.getErrorCode() == 0){
                       Logger.debug(recommendResp.toString());
                       try {
                           MarketApplication.getInstance().dbUtils.delete(LocAppInfoBto.class, WhereBuilder.b("sType","=",SEARCH_RECOMMEND_APPINFO));
                           List<AppInfoBto> applist = recommendResp.getAppList();
                           appLst = new ArrayList<LocAppInfoBto>();
                           for (AppInfoBto info : applist){
                               LocAppInfoBto locInfo = new LocAppInfoBto(info,SEARCH_RECOMMEND_APPINFO);
                               MarketApplication.getInstance().dbUtils.save(locInfo);
                               appLst.add(locInfo);
                           }
                           List<KeyWordInfoBto> keyLst = recommendResp.getKeyList();
                           keys = "";
                           for(KeyWordInfoBto key:keyLst){
                               keys = keys+key.getKey()+"/";
                           }
                           SharedPreferences  spf = SearchAppActivity.this.getSharedPreferences("CommonData", Context.MODE_PRIVATE);
                           spf.edit().putString("Search_Recommend_keys",keys).commit();
                           Message msg = Message.obtain(mHandler,SEARCH_REFRESH_RECOMMEND_VIEW);
                           msg.sendToTarget();
                       } catch (DbException e) {
                           e.printStackTrace();
                       }
                   }else{
                       Logger.error(recommendResp.getErrorMessage());
                   }

               } else {
                   Logger.error("GetStartPageReq error:" + respOrMsg);
               }
            }
        });


    }

    private LinearLayout hotWordsView(String keys) {
        String[] strs = keys.split("/");
        LinearLayout content = new LinearLayout(this);
        LinearLayout.LayoutParams p = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setLayoutParams(p);
        content.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout row = null;
        for (int i = 0; i < strs.length; i++) {
            Button button = new Button(this);
            button.setText(strs[i]);
            button.setTextColor(Color.parseColor("#222222"));
            button.setBackgroundResource(R.drawable.hotwords_bg);
            button.setTextSize(mTextSize);
            button.setId(1000+i);
            button.setTag("key");

            button.setOnClickListener(SearchAppActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mTextHight);
            params.rightMargin = mTexTRightMargin;
            params.gravity = Gravity.CENTER;
            button.setPadding(mTextPadding, 0, mTextPadding, 0);
            button.setGravity(Gravity.CENTER_VERTICAL);
            button.setLayoutParams(params);
            button.measure(0, 0);
            int c = button.getMeasuredWidth() + mTexTRightMargin;
            len = len + c;
            button.setSingleLine();
            button.setEllipsize(TruncateAt.END);
            if (len > mRowWidth || row == null) {
                Log.e("yys", mRowWidth + "   len  " + len + "c  " + c + "button.getPaddingLeft()  " + button.getPaddingLeft() + "button.getPaddingRight()  " + button.getPaddingRight());
                len = c;
                row = new LinearLayout(this);
                p = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, mRowHight);
                row.setGravity(Gravity.CENTER_VERTICAL);
                p.leftMargin = mRowLeftMargin;
                row.setLayoutParams(p);
                content.addView(row);
            }
            row.addView(button, params);
        }
        return content;
    }

    private void refreshCommendView(List<LocAppInfoBto> appList,String keys){
        mRecommend_content = mInflater.inflate(R.layout.oc_search_recommend_content, null);
        LinearLayout icon_content = (LinearLayout)mRecommend_content.findViewById(R.id.search_icon_content);
        if(!TextUtils.isEmpty(keys)) {
            LinearLayout wordsContent = hotWordsView(keys);
            LinearLayout hotWordsContent = (LinearLayout) mRecommend_content.findViewById(R.id.hot_words_cotent);
            hotWordsContent.addView(wordsContent);
        }
        mRela_content.addView(mRecommend_content);
        if(appList != null) {
            for (int i = 0; i < appList.size(); i++) {
                LocAppInfoBto info = appList.get(i);
                RelativeLayout icon = (RelativeLayout) icon_content.getChildAt(i);
                ImageView image = (ImageView) icon.getChildAt(0);
                TextView text = (TextView) icon.getChildAt(1);
                MarketApplication.getInstance().imageLoader.displayImage(info.getImgUrl(), image);
                text.setText(info.getName());
            }
        }
        Logger.e(Tag,"refreshCommendView end");
    }

    private void refreshAppLstView(SearchAppResp resp) {
        List<AppInfoBto> appList = resp.getAppList();
        mSearch_content = mInflater.inflate(R.layout.oc_search_app_list, null);
        mRela_content.removeAllViews();
        if (appList != null) {
//            List<AppInfoBto> list = new ArrayList<AppInfoBto>();
//            for (AppInfoBto info : appList) {
//                AppInfoBto appInfo = new AppInfoBto(info);
//                list.add(appInfo);
//            }
            AppListAdapter appListAdapter = new AppListAdapter(SearchAppActivity.this, appList);
            ListView applist = (ListView) mSearch_content.findViewById(R.id.search_list);
            applist.setAdapter(appListAdapter);
        }
        TextView count = (TextView)mSearch_content.findViewById(R.id.search_count);
        if(appList != null){
            count.setText(""+appList.size());
        }else{
            count.setText("0");
        }
        mRela_content.addView(mSearch_content);
    }

    private void searchApp(String key){
        final SearchAppReq req = new SearchAppReq();
        OcHttpConnection httpConnection = new OcHttpConnection(this);
        req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(this));
        req.setKeyword(key);
        req.setStart(0);
        req.setPageSize(13);
        httpConnection.sendRequest(RequestType.MARKET_DATA, req, SearchAppResp.class, new OcHttpReqCallBack() {
            @Override
            public void onResponse(boolean result, Object respOrMsg) {
                if(result){
                    searchAppListResp = (SearchAppResp)respOrMsg;
                    if(searchAppListResp.getErrorCode() == 0){
                        Logger.debug(searchAppListResp.toString());
                        Message msg = Message.obtain(mHandler,SEARCH_REFRESH_LIST_VIEW);
                        msg.sendToTarget();
                    }else{
                        Logger.error(searchAppListResp.getErrorMessage());
                    }

                } else {
                    Logger.error("GetStartPageReq error:" + respOrMsg);
                }
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        String tag = (String)arg0.getTag();
        if(!TextUtils.isEmpty(tag) && tag.equals("key")){
            int position = arg0.getId()-1000;
            String[] strs = keys.split("/");
            searchApp(strs[position]);
            if(mEditText != null){
                mEditText.setText(strs[position]);
                Editable etext = mEditText.getText();
                Selection.setSelection(etext, etext.length());
            }
        }
    }
}
