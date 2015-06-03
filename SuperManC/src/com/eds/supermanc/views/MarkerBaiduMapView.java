/*
 * File Name: MarkerBaiduMapView.java 
 * History:
 * Created by Administrator on 2015-4-23
 */
package com.eds.supermanc.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.supermanc.R;

public class MarkerBaiduMapView {
    private TextView mtvName;
    private TextView mtvAddress;
    private Button mBtnNavigation;// 导航
    private MarkerClickInfo mMarkerClick;
    private View mMarkerInfoView;
    private Context mContext;

    public MarkerBaiduMapView(Context context) {
        mContext = context;
        initView(context);
    }

    public void initView(Context context) {
        mMarkerInfoView = LayoutInflater.from(context).inflate(R.layout.marker_end_layout, null, false);
        mtvName = (TextView) mMarkerInfoView.findViewById(R.id.tv_name);
        mtvAddress = (TextView) mMarkerInfoView.findViewById(R.id.tv_address);
        mBtnNavigation = (Button) mMarkerInfoView.findViewById(R.id.btn_navigation);
        mBtnNavigation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMarkerClick.showNavigation();
            }
        });
    }

    public void setNameInfo(String strname) {
        mtvName.setText(strname);
    }

    public void setAddressInfo(String straddress) {
        mtvAddress.setText(straddress);
    }

    public void setShowButton(boolean isShow) {
        if (isShow) {
            mBtnNavigation.setVisibility(View.VISIBLE);
        } else {
            mBtnNavigation.setVisibility(View.GONE);
        }
    }

    public void setmMarkerClick(MarkerClickInfo mMarkerClick) {
        this.mMarkerClick = mMarkerClick;
    }

    public interface MarkerClickInfo {
        public void showNavigation();
    }

    public View getmMarkerInfoView() {
        if (mMarkerInfoView == null) {
            initView(mContext);
        }
        return mMarkerInfoView;
    }

}
