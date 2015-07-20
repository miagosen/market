package com.lx.market.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lx.market.adapter.UpdateAppsAdapter;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.connection.OcHttpConnection;
import com.lx.market.network.model.AppInfoBto;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.protocol.GetLocalAppsUpdateReq;
import com.lx.market.network.protocol.GetLocalAppsUpdateResp;
import com.lx.market.network.utils.TerminalInfoUtil;

import market.lx.com.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Antikvo.Miao on 2014/8/25.
 */
public class UpdateAppsManagerFragment extends BaseFragment {

  private View         view;
  private LinearLayout ll_loading_view;
  private ListView lvUpdateApps;
  private TextView tvNoUpdate;
  private UpdateAppsAdapter updateAppsAdapter;
  
  private GetLocalAppsUpdateResp resp;
  
  private List<AppInfoBto>      appInfoBtos = new ArrayList<AppInfoBto>();
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (view == null) {
      view = inflater.inflate(R.layout.update_apps_fragment, container, false);
      ll_loading_view = (LinearLayout) view.findViewById(R.id.ll_loading_view);
      lvUpdateApps = (ListView) view.findViewById(R.id.lv_update_manager);
      tvNoUpdate = (TextView) view.findViewById(R.id.tv_no_update_apps);
      updateAppsAdapter = new UpdateAppsAdapter(mContext, appInfoBtos);
      lvUpdateApps.setAdapter(updateAppsAdapter);
    } else {
      ViewGroup p = (ViewGroup) view.getParent();
      if (p != null) {
        p.removeAllViewsInLayout();
      }
    }
    loadData();
    ll_loading_view.setVisibility(View.GONE);
    return view;
  }
  
  private void loadData(){	
	  ll_loading_view.setVisibility(View.VISIBLE);
		  GetLocalAppsUpdateReq req = new GetLocalAppsUpdateReq();
		  req.setTerminalInfo(TerminalInfoUtil.getTerminalInfo(mContext));
		  OcHttpConnection http = new OcHttpConnection(mContext);
		  http.sendRequest(RequestType.UPDATE, req, GetLocalAppsUpdateResp.class, new OcHttpReqCallBack() {
			
			@Override
			public void onResponse(boolean result, Object respOrMsg) {
				ll_loading_view.setVisibility(View.GONE);
				if(result){
					resp = (GetLocalAppsUpdateResp)respOrMsg;
					if(resp.getErrorCode() == 0){
						if(resp.getAppList() != null && resp.getAppList().size() > 0){
							appInfoBtos.addAll(resp.getAppList());
						}
					}
				}else{
					tvNoUpdate.setVisibility(View.VISIBLE);
				}
				updateAppsAdapter.notifyDataSetChanged();
			}
		});
  }

}
