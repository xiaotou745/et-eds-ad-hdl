/*
 * File Name: BaiduMapActivityDeom.java 
 * History:
 * Created by Administrator on 2015-4-22
 */
package com.eds.supermanc.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.eds.supermanc.Constants;
import com.eds.supermanc.beans.AppInfoBean;
import com.eds.supermanc.beans.Gps;
import com.eds.supermanc.mapoverlay.MyDrivingRouteOverlay;
import com.eds.supermanc.mapoverlay.MyTransitRouteOverlay;
import com.eds.supermanc.mapoverlay.MyWalingRouteOverlay;
import com.eds.supermanc.utils.MapUtils;
import com.eds.supermanc.utils.Utils;
import com.eds.supermanc.views.MapDialog;
import com.eds.supermanc.views.MarkerBaiduMapView;
import com.eds.supermanc.views.MarkerBaiduMapView.MarkerClickInfo;
import com.supermanc.R;

/**
 * 百度地图定位 (Description)
 * 
 * @author zaokafei
 * @version 1.0
 * @date 2015-4-22
 */
public class BaiduMapActivityDeom extends BaseActivity implements MarkerClickInfo {
    public Context mContext;
    private ProgressDialog dialog;
    private boolean is_mapLoaded = false;// 地图是否加载王城

    public LinearLayout layout_title;
    public ImageView mTilteLoge;
    public TextView mTitleContent;
    private boolean bol = false; // 显示地图和文字控制
    public ImageView mImgQiehuan;
    public RelativeLayout layout_bottom;
    public LinearLayout layout_text;
    public TextView tv_route_title;
    public TextView tv_route_info;
    public MapView mMapView;
    public BaiduMap mBaidumap = null;

    // 地理编码
    private GeoCoder mSearch_geocoder;// 地理编译
    private OnGetGeoCoderResultListener listener_geocode;
    private String endCity;// 目标城市
    private String endName;// 名称
    private String endAddress;// 目标地址
    private double endLatitude = 0.0;// 纬度
    private double endLongitude = 0.0;// 经度
    private String startCity;// 起始城市
    private String startAddress;// 起始地址
    private double startLatitude = 0.0;// 纬度
    private double startLongitude = 0.0;// 经度
    public BitmapDescriptor mIconMakerEnd;// 终点标注

    // 定位
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    // 线路规划
    private Button mBtnPre = null;// 上一个节点
    private Button mBtnNext = null;// 下一个节点
    private int nodeIndex = -1;// 节点索引,供浏览节点时使用
    private RouteLine route = null;
    private int iRouteType = -1;// 路线
    private OverlayManager routeOverlay = null;
    private boolean useDefaultIcon = false;
    private TextView popupText = null;// 泡泡view
    private RoutePlanSearch mSearch_routePlan;// 线路规划
    private OnGetRoutePlanResultListener myRoutePlanListener;
    private String strBRouteLineInfo;// 路线的文字信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baidumap_layout_demo);
        mContext = this;
        // 检测gps
        checkoutGPSStatus();
        // 初始化view
        initView();
        // 获取数据
        getInitViewData();
        // 初始化地图
        initMapInfo();
        // 初始化事件
        initClickBaiduMap();
        // 初始化定位参数
        initStartBDLocation();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaidumap.setMapStatus(msu);
        showMyDialog("地图加载中...");
    }

    @Override
    protected void onStart() {
        // 开启图层定位
        mBaidumap.setMyLocationEnabled(true);
        // 开启方向传感器
        super.onStart();
    }

    @Override
    protected void onStop() {
        // 关闭图层定位
        mBaidumap.setMyLocationEnabled(false);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        if (is_mapLoaded) {
            startBDLocation();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopBDLocation();
        mMapView.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIconMakerEnd != null) {
            mIconMakerEnd.recycle();
        }
        if (mSearch_geocoder != null) {
            mSearch_geocoder.destroy();
            mSearch_geocoder = null;
            listener_geocode = null;
        }
        if (mLocationClient != null) {
            mLocationClient = null;
            myListener = null;
        }
        if (mBaidumap != null) {
            mBaidumap = null;
        }
        mMapView.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Constants.OPEN_GPS_SERVER_RESULT_CODE == requestCode) {
            // 手动打开gps,重新定位
            getStartAndEndLocationInfo();
        }
    }

    /**
     * 初始化
     */
    public void initView() {
        layout_title = (LinearLayout) findViewById(R.id.titleLeftLayout);
        mTilteLoge = (ImageView) findViewById(R.id.titleLogo);
        mTitleContent = (TextView) findViewById(R.id.titleContent);
        mImgQiehuan = (ImageView) findViewById(R.id.qiehua);
        layout_bottom = (RelativeLayout) findViewById(R.id.bottomLayout);
        layout_text = (LinearLayout) findViewById(R.id.textLayout);
        tv_route_title = (TextView) findViewById(R.id.tv_route_title);
        tv_route_info = (TextView) findViewById(R.id.tv_route_info);
        mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomBy(13);
        mBaidumap.animateMapStatus(mapStatusUpdate);

        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        mIconMakerEnd = BitmapDescriptorFactory.fromResource(R.drawable.map_map_end);// 终点标注
    }

    /**
     * 获取初始化数据
     */
    public void getInitViewData() {
        Intent mIntent = this.getIntent();
        endCity = mIntent.getStringExtra("city");
        endAddress = mIntent.getStringExtra("detailAddress");
        endName = mIntent.getStringExtra("name");
    }

    /**
     * 检车是否开启gps
     */
    private void checkoutGPSStatus() {
        if (!Utils.isOpenGPS(mContext)) {
            try {
                // 强制开启gps
                Utils.openGPS(mContext);
            } catch (Exception e) {
                // 手动开启gps
                openGPSByHand();
            }
        }
    }

    /*
     * 手动开启gps
     */
    private void openGPSByHand() {
        new AlertDialog.Builder(mContext).setTitle("提示")// 设置标题
                .setMessage("请开启GPS服务")// 设置提示消息
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 设置确定的按键
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, Constants.OPEN_GPS_SERVER_RESULT_CODE);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 设置取消按键
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setCancelable(false)// 设置按返回键是否响应返回，这是是不响应
                .show();// 显示
    }

    /**
     * 初始化地图洗洗
     */
    public void initMapInfo() {
        // 地理编码
        mSearch_geocoder = GeoCoder.newInstance();
        listener_geocode = new MyGetGeoCoderResultListener();
        mSearch_geocoder.setOnGetGeoCodeResultListener(listener_geocode);
        // 定位
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);
        // 线路规划
        mSearch_routePlan = RoutePlanSearch.newInstance();
        myRoutePlanListener = new MyGetRoutePlanResultListener();
        mSearch_routePlan.setOnGetRoutePlanResultListener(myRoutePlanListener);
    }

    /**
     * 初始化起始位置
     */
    public void initStartBDLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 开启gps
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");// 返回百度经纬度坐标
        option.setScanSpan(1000 * 60);
        option.setLocationMode(LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
    }

    /**
     * 终点定位信息
     */
    public void getEndLocationInfo() {
        GeoCodeOption mGeoCodeOption = new GeoCodeOption();
        mGeoCodeOption.city(endCity);
        mGeoCodeOption.address(endAddress);
        mSearch_geocoder.geocode(mGeoCodeOption);
    }

    /**
     * 起始点位置信息
     */
    public void startBDLocation() {
        if (mLocationClient != null) {
            if (mLocationClient.isStarted()) {
                mLocationClient.stop();
            }
            mLocationClient.start();
        }
    }

    /**
     * 结束定位
     */
    public void stopBDLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    /**
     * 定位
     */
    public void getStartAndEndLocationInfo() {
        setDialogMessage("定位中...");
        // 起始位置
        startBDLocation();
        // 终点位置
        getEndLocationInfo();
    }

    public void showMyDialog(String strContent) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = ProgressDialog.show(this, "提示", strContent, false, true);
    }

    public void setDialogMessage(String strContent) {
        if (dialog == null || !dialog.isShowing()) {
            showMyDialog(strContent);
        } else {
            dialog.setMessage(strContent);
        }
    }

    public void dismissMyDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 显示当前覆盖物
     */
    public void showEndOverly(GeoCodeResult result) {
        endLatitude = result.getLocation().latitude;
        endLongitude = result.getLocation().longitude;
        mBaidumap.clear();// 清楚所有的图层
        LatLng latLng = new LatLng(endLatitude, endLongitude);
        MarkerOptions moption = new MarkerOptions();
        moption.position(latLng);
        moption.icon(mIconMakerEnd);
        moption.zIndex(5);
        moption.visible(true);
        Marker marker = (Marker) mBaidumap.addOverlay(moption);
        Bundle endBundle = new Bundle();
        endBundle.putString("marker_category", "end");
        marker.setExtraInfo(endBundle);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);// 设置地图的中心坐标
        mBaidumap.setMapStatus(update);
    }

    /**
     * 显示目标marker
     */
    public void showEndMarker() {
        if (endLatitude == 0.0 || endLongitude == 0.0) {
            return;
        }
        mBaidumap.clear();// 清楚所有的图层
        LatLng latLng = new LatLng(endLatitude, endLongitude);
        MarkerOptions moption = new MarkerOptions();
        moption.position(latLng);
        moption.icon(mIconMakerEnd);
        moption.zIndex(5);
        moption.visible(true);
        Marker marker = (Marker) mBaidumap.addOverlay(moption);
        Bundle endBundle = new Bundle();
        endBundle.putString("marker_category", "end");
        marker.setExtraInfo(endBundle);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);// 设置地图的中心坐标
        mBaidumap.setMapStatus(update);
    }

    /**
     * 保存起始位置西你想
     */
    public void saveStartLocation(BDLocation location) {
        startCity = location.getCity();
        startAddress = location.getAddrStr();
        startLatitude = location.getLatitude();
        startLongitude = location.getLongitude();
    }

    /*
     * 8 单击地图
     */
    public void initClickBaiduMap() {
        mBaidumap.setOnMapClickListener(new OnMapClickListener() {
            public void onMapClick(LatLng arg0) {
                mBaidumap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }
        });
        mBaidumap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                // 地图加载完成
                is_mapLoaded = true;
                myHandler.sendEmptyMessageDelayed(0, 2000);
            }
        });
        mBaidumap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                Bundle mBundle = marker.getExtraInfo();
                if (mBundle != null && "end".equals(mBundle.getString("marker_category", ""))) {
                    // 点击终点坐标
                    setShowEndMarker(marker);
                    return true;
                }
                // else if (marker.getPosition().latitude == endLatitude
                // || marker.getPosition().longitude == endLongitude) {
                // // 最后的marker
                // setShowEndMarker(marker);
                // return true;
                // }
                else {
                    return false;
                }
            }
        });
    }

    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                // 定位和地理解码
                // Toast.makeText(mContext, "地图加载完成", 0).show();
                getStartAndEndLocationInfo();
                this.removeMessages(0);
            }
        }

    };

    /*
     * 8 展示终点marker
     */
    public void setShowEndMarker(Marker marker) {
        MarkerBaiduMapView infoViewBean = new MarkerBaiduMapView(getApplicationContext());
        View infoMarkerView = infoViewBean.getmMarkerInfoView();
        infoViewBean.setNameInfo(endName);
        infoViewBean.setAddressInfo(endAddress);
        LatLng ll = marker.getPosition();
        Point p = mBaidumap.getProjection().toScreenLocation(ll);
        p.y -= 47;
        LatLng llinfo = mBaidumap.getProjection().fromScreenLocation(p);
        InfoWindow mInfoWindow = new InfoWindow(infoMarkerView, llinfo, 1);
        infoMarkerView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v != null) {
                    mBaidumap.hideInfoWindow();
                }
            }
        });
        mBaidumap.showInfoWindow(mInfoWindow);
        infoViewBean.setmMarkerClick(this);
    }

    /**
     * 选择方式
     * 
     * @param view
     */
    public void SearchButtonProcess(View view) {
        // 重置浏览节点
        route = null;
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        // 起始 startCity startAddress startLatitude startLongitude
        if (startLatitude == 0.0 || startLongitude == 0.0) {
            Toast.makeText(mContext, "定位中...", 0).show();
            return;
        }
        mBaidumap.clear();
        LatLng startLatLng = new LatLng(startLatitude, startLongitude);
        PlanNode startNode = PlanNode.withLocation(startLatLng);

        PlanNode endNode;
        if (endLatitude == 0.0 || endLongitude == 0.0) {
            startNode = PlanNode.withCityNameAndPlaceName(startCity, startAddress);
            endNode = PlanNode.withCityNameAndPlaceName(endCity, endAddress);
        } else {
            LatLng endLatLng = new LatLng(endLatitude, endLongitude);
            endNode = PlanNode.withLocation(endLatLng);
        }

        switch (view.getId()) {
        case R.id.drive:
            iRouteType = 0;
            mSearch_routePlan.drivingSearch((new DrivingRoutePlanOption()).from(startNode).to(endNode));
            break;
        case R.id.transit:
            iRouteType = 1;
            mSearch_routePlan.transitSearch((new TransitRoutePlanOption()).from(startNode).city(startCity).to(endNode)
                    .city(endCity));
            break;
        case R.id.walk:
            iRouteType = 2;
            mSearch_routePlan.walkingSearch((new WalkingRoutePlanOption()).from(startNode).to(endNode));
            break;
        }
    }

    public void showMaptextInfo(View view) {
        if (view.getId() == R.id.qiehua) {
            if (bol) {
                mImgQiehuan.setBackgroundResource(R.drawable.text_transfer);
                mMapView.setVisibility(View.VISIBLE);
                layout_text.setVisibility(View.GONE);
                bol = false;
                showTextRouteInfo();
            } else {
                mMapView.setVisibility(View.GONE);
                layout_text.setVisibility(View.VISIBLE);
                bol = true;
                mImgQiehuan.setBackgroundResource(R.drawable.map_transfer);
                mBtnPre.setVisibility(View.INVISIBLE);
                mBtnNext.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 展示文字的线路信息
     */
    private void showTextRouteInfo() {
        switch (iRouteType) {
        case 0:
            tv_route_title.setText("自驾车路线");
            tv_route_info.setText(strBRouteLineInfo);
            break;
        case 1:
            tv_route_title.setText("自驾车路线");
            tv_route_info.setText(strBRouteLineInfo);
            break;
        case 2:
            tv_route_title.setText("自驾车路线");
            tv_route_info.setText(strBRouteLineInfo);
            break;
        }
    }

    /**
     * 节点单击
     * 
     * @param view
     */
    public void nodeClick(View view) {
        if (route == null || route.getAllStep() == null) {
            return;
        }
        if (nodeIndex == -1 && view.getId() == R.id.pre) {
            return;
        }
        // 设置节点
        if (view.getId() == R.id.next) {
            if (nodeIndex < route.getAllStep().size() - 1) {
                nodeIndex++;
            } else {
                return;
            }
        } else if (view.getId() == R.id.pre) {
            if (nodeIndex > 0) {
                nodeIndex--;
            } else {
                return;
            }
        }
        // 获取节点信息
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = route.getAllStep().get(nodeIndex);
        if (step instanceof DrivingRouteLine.DrivingStep) {
            nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace().getLocation();
            nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
        } else if (step instanceof WalkingRouteLine.WalkingStep) {
            nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace().getLocation();
            nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
        } else if (step instanceof TransitRouteLine.TransitStep) {
            nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace().getLocation();
            nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
        }
        if (nodeLocation == null || nodeTitle == null) {
            return;
        }
        // 移动节点到中心点
        mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popu
        setShowNodeLocationView(nodeLocation, nodeTitle);

    }

    /**
     * 线路接地那view
     * 
     * @param title
     */
    public void setShowNodeLocationView(LatLng location, String title) {
        if (popupText == null) {
            popupText = new TextView(mContext);
            popupText.setPadding(10, 10, 10, 10);
            popupText.setBackgroundResource(R.drawable.popup);
            popupText.setTextColor(0xFF000000);
            popupText.setText(title);
        }
        Point p = mBaidumap.getProjection().toScreenLocation(location);// 屏幕坐标
        p.y -= 10;
        LatLng llinfo = mBaidumap.getProjection().fromScreenLocation(p);// 地图坐标
        mBaidumap.showInfoWindow(new InfoWindow(popupText, llinfo, 0));
    }

    /*
     * 地址编
     */
    public class MyGetGeoCoderResultListener implements OnGetGeoCoderResultListener {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            // 地理编码
            dismissMyDialog();
            if (result == null) {
                Toast.makeText(mContext, "抱歉,目标地址定位失败", Toast.LENGTH_LONG).show();
                return;
            } else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                // 显示地图地址
                showEndOverly(result);
            } else {
                Toast.makeText(mContext, "抱歉,目标地址定位失败", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            // 反地理编码
        }

    }

    /**
     * 定位 (Description)
     * 
     * @author zaokafei
     * @version 1.0
     * @date 2015-4-23
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                saveStartLocation(location);
                // 结束定位
                Toast.makeText(mContext, "定位完成", 0).show();
                // stopBDLocation();
            }
        }
    }

    /**
     * 组装
     */
    private void setRouteTextInfo() {
        if (route != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("(当前位置) -> ");
            switch (iRouteType) {
            case 0:
                DrivingRouteLine drl = (DrivingRouteLine) route;
                for (DrivingRouteLine.DrivingStep ostep : drl.getAllStep()) {
                    sb.append("(").append(ostep.getInstructions()).append(")").append(" -> ");
                }
                break;
            case 1:
                TransitRouteLine trl = (TransitRouteLine) route;
                for (TransitRouteLine.TransitStep ostep : trl.getAllStep()) {
                    sb.append("(").append(ostep.getInstructions()).append(")").append(" -> ");
                }
                break;
            case 2:
                WalkingRouteLine wrl = (WalkingRouteLine) route;
                for (WalkingRouteLine.WalkingStep ostep : wrl.getAllStep()) {
                    sb.append("(").append(ostep.getInstructions()).append(")").append(" -> ");
                }
                break;
            }
            sb.append("(目的地: ").append(endAddress).append(" ) ");
            strBRouteLineInfo = sb.toString();
            if (bol) {
                showTextRouteInfo();
            }
        }
    }

    public class MyGetRoutePlanResultListener implements OnGetRoutePlanResultListener {

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            // 驾车
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(mContext, "抱歉，未找到驾车路线", Toast.LENGTH_SHORT).show();
                showEndMarker();
                return;
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起始地点或途径点有歧义
                // result.getSuggestAddrInfo();
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                nodeIndex = -1;
                mBtnPre.setVisibility(View.VISIBLE);
                mBtnNext.setVisibility(View.VISIBLE);
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
                setRouteTextInfo();
            }

        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult result) {
            // 公交
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(mContext, "抱歉，未找到公交路线", Toast.LENGTH_SHORT).show();
                showEndMarker();
                return;
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起始地点或途径点有歧义
                // result.getSuggestAddrInfo();
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                nodeIndex = -1;
                mBtnPre.setVisibility(View.VISIBLE);
                mBtnNext.setVisibility(View.VISIBLE);
                route = result.getRouteLines().get(0);
                TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
                setRouteTextInfo();
            }
        }

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            // 步行
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(mContext, "抱歉，未找到驾车路线", Toast.LENGTH_SHORT).show();
                showEndMarker();
                return;
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起始地点或途径点有歧义
                // result.getSuggestAddrInfo();
                Toast.makeText(mContext, "未找到路线", Toast.LENGTH_SHORT).show();
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                nodeIndex = -1;
                mBtnPre.setVisibility(View.VISIBLE);
                mBtnNext.setVisibility(View.VISIBLE);
                route = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new MyWalingRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
                setRouteTextInfo();
            }
        }
    }

    @Override
    public void showNavigation() {
        mBaidumap.hideInfoWindow();
        ArrayList<AppInfoBean> dataMap = MapDialog.initOpenMapInfo(mContext);
        int imapSize = dataMap.size();
        switch (imapSize) {
        case 0:
            Toast.makeText(mContext, "请安装百度地图客户端!", Toast.LENGTH_SHORT).show();
            break;
        case 1:
            AppInfoBean bean = dataMap.get(0);
            if (bean.getPackage_name().equalsIgnoreCase("com.baidu.BaiduMap")) {
                MapDialog.startBaiduMap(mContext, setMapNavData());
            } else if (bean.getPackage_name().equalsIgnoreCase("com.autonavi.minimap")) {
                MapDialog.startGaodeMap(mContext, setMapNavData());
            }
            break;
        case 2:
            MapDialog openMapDialog = new MapDialog(mContext);
            openMapDialog.setAddressMapInfo(setMapNavData());
            openMapDialog.show();
            break;
        }

    }

    /**
     * 设置导航信息
     * 
     * @return
     */
    private HashMap<String, String> setMapNavData() {
        Gps strGps = MapUtils.bd09_To_Gcj02(startLatitude, startLongitude);
        Gps endGps = MapUtils.bd09_To_Gcj02(endLatitude, endLongitude);
        HashMap<String, String> addressInfo = new HashMap<String, String>();
        addressInfo.put(MapDialog.STR_START_CITY_KEY, startCity);
        addressInfo.put(MapDialog.STR_START_ADDRESS_KEY, startAddress);
        addressInfo.put(MapDialog.STR_START_LAT_KEY, String.valueOf(strGps.getWgLat()));
        addressInfo.put(MapDialog.STR_START_LON_KEY, String.valueOf(strGps.getWgLon()));
        addressInfo.put(MapDialog.STR_END_ADDRESS_KEY, endAddress);
        addressInfo.put(MapDialog.STR_END_LAT_KEY, String.valueOf(endGps.getWgLat()));
        addressInfo.put(MapDialog.STR_END_LON_KEY, String.valueOf(endGps.getWgLon()));
        addressInfo.put(MapDialog.STR_END_CITY_KEY, endCity);
        addressInfo.put(MapDialog.STR_END_NAME_KEY, endName);
        return addressInfo;
    }
}
