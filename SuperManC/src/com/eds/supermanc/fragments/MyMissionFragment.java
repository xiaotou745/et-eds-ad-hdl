package com.eds.supermanc.fragments;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.eds.supermanc.Constants;
import com.eds.supermanc.adapter.MyMissionAdapter;
import com.eds.supermanc.beans.MissionBean;
import com.eds.supermanc.utils.UserTools;
import com.eds.supermanc.utils.VolleyTool;
import com.eds.supermanc.utils.VolleyTool.HTTPListener;
import com.supermanc.R;

/**
 * 我的任务页面 (Description)
 * 
 * @author zaokafei
 * @version 1.0
 * @date 2015-2-28
 */
public class MyMissionFragment extends Fragment implements HTTPListener {

    private RelativeLayout mFlagmentLayout;
    private RelativeLayout mLoadLayout;
    private ListView listView;
    private TextView info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFlagmentLayout = (RelativeLayout) inflater.inflate(R.layout.my_mission_fragment, container, false);
        initView();
        // refresh();
        return mFlagmentLayout;
    }

    private void initView() {
        listView = (ListView) mFlagmentLayout.findViewById(R.id.listView);
        info = (TextView) mFlagmentLayout.findViewById(R.id.info);
        mLoadLayout = (RelativeLayout) mFlagmentLayout.findViewById(R.id.loadLayout111);
        mLoadLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void initData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", UserTools.getUser(getActivity()).getUserId());
        params.put("status", "2");
        VolleyTool.post(Constants.GET_MY_MISSION_URL, params, this, Constants.GETMYMISSION, MissionBean.class);
    }

    public void refresh() {
        beforeRequest();
        initData();
    }

    @Override
    public <T> void onResponse(T t, int requestCode) {
        afterRequestComplete();
        if (requestCode == Constants.GETMYMISSION) {
            MissionBean mb = (MissionBean) t;
            if (mb != null && mb.getResult().size() > 0) {
                MyMissionAdapter adapter = new MyMissionAdapter(this.getActivity());
                adapter.setData(mb.getResult());
                listView.setAdapter(adapter);
                info.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            } else {
                info.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, int requestCode) {
        afterRequestComplete();
        info.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
    }

    private void beforeRequest() {
        mLoadLayout.setVisibility(View.VISIBLE);
    }

    private void afterRequestComplete() {
        mLoadLayout.setVisibility(View.GONE);
    }

}
