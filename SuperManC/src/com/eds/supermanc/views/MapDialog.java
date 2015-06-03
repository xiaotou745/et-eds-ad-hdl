/*
 * File Name: MapDialog.java 
 * History:
 * Created by Administrator on 2015-4-27
 */
package com.eds.supermanc.views;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eds.supermanc.beans.AppInfoBean;
import com.eds.supermanc.utils.Utils;
import com.supermanc.R;

/*8
 * 地图对话框
 */
public class MapDialog extends Dialog {
    private Context mContext;
    private ListView mapList;
    private CheckBox mapCheckBox;
    private MapDataAdapter mapAdapter;

    private ArrayList<AppInfoBean> openMapList;
    private HashMap<String, String> addressMapInfo;
    private MapDialog mMyDialog;
    public static final String STR_START_CITY_KEY = "STR_START_CITY_KEY";
    public static final String STR_START_ADDRESS_KEY = "STR_START_ADDRESS_KEY";
    public static final String STR_START_LAT_KEY = "STR_START_LAT_KEY";
    public static final String STR_START_LON_KEY = "STR_START_LON_KEY";// 经度度
    public static final String STR_END_ADDRESS_KEY = "STR_END_ADDRESS_KEY";
    public static final String STR_END_LAT_KEY = "STR_END_LAT_KEY";
    public static final String STR_END_LON_KEY = "STR_END_LON_KEY";
    public static final String STR_END_CITY_KEY = "STR_END_CITY_KEY";
    public static final String STR_END_NAME_KEY = "STR_END_NAME_KEY";

    public MapDialog(Context context, int theme) {
        super(context, theme);
    }

    protected MapDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public MapDialog(Context context) {
        super(context);
        mContext = context;
        mMyDialog = this;
    }

    // ==========================================================================
    // Constants
    // ==========================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_map_view);
        initView();
        initVieListener();
        openMapList = initOpenMapInfo(mContext);
        showOpenMap();
    }

    private void initView() {
        this.setTitle("请选择地图");
        mapList = (ListView) findViewById(R.id.list_map);
        mapCheckBox = (CheckBox) findViewById(R.id.check_map_select);
    }

    private void initVieListener() {
        mapCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton view, boolean checked) {
                if (view == mapCheckBox) {

                }
            }
        });
    }

    /**
     * 初始化开发地图
     */
    public static ArrayList<AppInfoBean> initOpenMapInfo(Context con) {
        String[] strMapArray = con.getResources().getStringArray(R.array.map_open_name);
        ArrayList<AppInfoBean> openMapInfoData = new ArrayList<AppInfoBean>();
        for (String strMap : strMapArray) {
            AppInfoBean bean = Utils.getAppInfoByPackageName(con, strMap);
            if (bean != null) {
                openMapInfoData.add(bean);
            }
        }
        return openMapInfoData;
    }

    private void showOpenMap() {
        mapAdapter = new MapDataAdapter(mContext, openMapList);
        mapList.setAdapter(mapAdapter);
        mapAdapter.notifyDataSetChanged();
    }

    /**
     * 设置地址信息
     * 
     * @param mapAddress
     */
    public void setAddressMapInfo(HashMap mapAddress) {
        addressMapInfo = mapAddress;
    }

    // ==========================================================================
    // Fields
    // ==========================================================================

    // ==========================================================================
    // Constructors
    // ==========================================================================

    // ==========================================================================
    // Getters
    // ==========================================================================

    // ==========================================================================
    // Setters
    // ==========================================================================

    // ==========================================================================
    // Methods
    // ==========================================================================
    /**
     * 百度地图导航
     * 
     * @param context
     * @param data
     */
    public static void startBaiduMap(Context context, HashMap<String, String> data) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("intent://map/direction?").append("origin=").append("latlng:")
                    .append(data.get(STR_START_LAT_KEY).toString()).append(",").append(data.get(STR_START_LON_KEY))
                    .append("|name=起点").append("&").append("destination=").append("latlng:")
                    .append(data.get(STR_END_LAT_KEY)).append(",").append(data.get(STR_END_LON_KEY)).append("|name=终点")
                    .append("&").append("mode=").append("driving").append("&").append("region=")
                    .append(data.get(STR_END_CITY_KEY)).append("&").append("coord_type=").append("gcj02").append("&")
                    .append("src=com.supermanc").append("#Intent;")
                    .append("scheme=bdapp;package=com.baidu.BaiduMap;end");
            Intent mIntent = Intent.getIntent(sb.toString());
            context.startActivity(mIntent);
        } catch (Exception e) {
        }
    }

    /**
     * dat=androidamap://route?sourceApplication=softname&slat=36.2&slon=116.1&sname=abc&
     * dlat=36.3&dlon=116.2&dname=def& dev=0&m=0&t=1&showType=1
     */
    public static void startGaodeMap(Context context, HashMap<String, String> data) {
        Intent mIntent = new Intent();
        mIntent.setAction(android.content.Intent.ACTION_VIEW);
        mIntent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
        StringBuffer sb = new StringBuffer();
        sb.append("androidamap://route?").append("sourceApplication=").append("com.supermanc").append("&")
                .append("slat=").append(data.get(STR_START_LAT_KEY)).append("&").append("slon=")
                .append(data.get(STR_START_LON_KEY)).append("&").append("sname=").append("起点").append("&")
                .append("dlat=").append(data.get(STR_END_LAT_KEY)).append("&").append("dlon=")
                .append(data.get(STR_END_LON_KEY)).append("&").append("dname=").append(data.get(STR_END_ADDRESS_KEY))
                .append("&").append("dev=0").append("&").append("m=2").append("&").append("t=2").append("&")
                .append("showType=1");

        mIntent.setData(Uri.parse(sb.toString()));
        context.startActivity(mIntent);
    }

    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================
    class MapDataAdapter extends BaseAdapter {
        Context mCon;
        ArrayList<AppInfoBean> dataAdapter;

        MapDataAdapter(Context con, ArrayList<AppInfoBean> data) {
            mCon = con;
            dataAdapter = data;
        }

        public void setMapData(ArrayList<AppInfoBean> data) {
            if (data == null) {
                dataAdapter = new ArrayList<AppInfoBean>();
            } else {
                dataAdapter = data;
            }
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (dataAdapter == null) {
                return 0;
            } else {
                return dataAdapter.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return dataAdapter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mCon, R.layout.map_list_item, null);
                holder = new Holder();
                holder.ll_map_itemt = (LinearLayout) convertView.findViewById(R.id.linear_map_item);
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.img_map_icon);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_map_name);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            final AppInfoBean bean = (AppInfoBean) getItem(position);
            holder.iv_icon.setImageDrawable(bean.getApp_icon());
            holder.tv_name.setText(bean.getApp_name());
            holder.ll_map_itemt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mMyDialog.dismiss();
                    if (bean.getPackage_name().equalsIgnoreCase("com.baidu.BaiduMap")) {
                        startBaiduMap(mContext, addressMapInfo);
                    } else if (bean.getPackage_name().equalsIgnoreCase("com.autonavi.minimap")) {
                        startGaodeMap(mContext, addressMapInfo);
                    }

                }
            });
            return convertView;
        }
    }

    private class Holder {
        LinearLayout ll_map_itemt;
        ImageView iv_icon;
        TextView tv_name;
    }
}
