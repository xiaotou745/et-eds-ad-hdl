/*
 * File Name: MyTransitRouteOverlay.java 
 * History:
 * Created by Administrator on 2015-4-28
 */
package com.eds.supermanc.mapoverlay;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;

/**
 * 公交 (Description)
 * 
 * @author zaokafei
 * @version 1.0
 * @date 2015-4-28
 */
public class MyTransitRouteOverlay extends TransitRouteOverlay {

    public MyTransitRouteOverlay(BaiduMap baidumap) {
        super(baidumap);
    }

    @Override
    public int getLineColor() {
        return super.getLineColor();
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        // 开始图标
        return super.getStartMarker();
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        // 结束图标
        return super.getTerminalMarker();
    }
}