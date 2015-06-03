/*
 * File Name: MyWalingRouteOverlay.java 
 * History:
 * Created by Administrator on 2015-4-28
 */
package com.eds.supermanc.mapoverlay;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;

/**
 * 自定义步行路线 (Description)
 * 
 * @author zaokafei
 * @version 1.0
 * @date 2015-4-28
 */
public class MyWalingRouteOverlay extends WalkingRouteOverlay {

    public MyWalingRouteOverlay(BaiduMap baidumap) {
        super(baidumap);
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        // 开始图标
        return super.getStartMarker();
    }

    @Override
    public int getLineColor() {
        return super.getLineColor();
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        // 结束图标
        return super.getTerminalMarker();
    }
}