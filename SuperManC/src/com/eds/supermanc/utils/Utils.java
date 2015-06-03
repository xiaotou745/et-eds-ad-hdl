/*
 * File Name: Util.java 
 * History:
 * Created by Administrator on 2014-6-16
 */
package com.eds.supermanc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.eds.supermanc.beans.AppInfoBean;
import com.eds.supermanc.picker.CityMode;

/**
 * 公共方法 (Description)
 * 
 * @author kangzhen
 * @version
 */
public class Utils {
    // ==========================================================================
    // Constants
    // ==========================================================================

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
     * 判断是否存在SD卡
     * 
     * @return
     */
    public static boolean isSdcardExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? true : false;
    }

    /**
     * 开放城市过滤
     * 
     * @param originList
     *            原城市
     * @param filterStr
     *            过滤条件
     * @return 符合条件
     */
    public static ArrayList<CityMode> filterCityByName(ArrayList<CityMode> originList, String[] filterStr) {
        ArrayList<CityMode> filterList = new ArrayList<CityMode>();
        if (filterStr == null || filterStr.length == 0) {
            return originList;
        } else {
            try {
                HashMap<String, CityMode> mapCity = new HashMap<String, CityMode>();
                int isize = originList.size();
                for (int i = 0; i < isize; i++) {
                    CityMode bean = originList.get(i);
                    byte[] bKey = bean.getName().toString().trim().getBytes("UTF-8");
                    String strkey = new String(bKey, "UTF-8");
                    mapCity.put(strkey, bean);
                }
                int strLeng = filterStr.length;
                for (int j = 0; j < strLeng; j++) {
                    byte[] bkey = filterStr[j].toString().trim().getBytes("UTF-8");
                    String skey = new String(bkey, "UTF-8");
                    if (mapCity.containsKey(skey)) {
                        CityMode fBean = mapCity.get(skey);
                        if (fBean != null) {
                            filterList.add(fBean);
                        }
                    }
                }
            } catch (Exception e) {

            }
            return filterList;
        }
    }

    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 联通：130、131、132、152、155、156、185、186
         * 电信：133、153、180、189、（1349卫通） 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 判断gps是否开启
     * 
     * @param context
     * @return
     */
    public static final boolean isOpenGPS(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        // if (gps || network) {
        // return true;
        // }
        if (gps) {
            return true;
        }

        return false;
    }

    /**
     * 强制开启gps
     * 
     * @param context
     * @throws CanceledException
     */
    public static final void openGPS(Context context) throws CanceledException {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
    }

    /**
     * 获取设备app列表
     * 
     * @param context
     * @return
     */
    public static final ArrayList<AppInfoBean> getAllAppInfoByDevice(Context context) {
        ArrayList<AppInfoBean> data = new ArrayList<AppInfoBean>();
        PackageManager pman = context.getPackageManager();
        List<PackageInfo> listPackage = pman.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo bean : listPackage) {
            AppInfoBean appBean = new AppInfoBean();
            String packageName = bean.packageName;// 包名
            String version_name = bean.versionName;
            int version_code = bean.versionCode;
            ApplicationInfo appinfo = bean.applicationInfo;// 应用
            Drawable icon_d = appinfo.loadIcon(pman);// 图标
            // Drawable logo_d = appinfo.loadLogo(pman);// logo
            String appName = appinfo.loadLabel(pman).toString();// 应用名称
            boolean is_system = filterApp(appinfo);
            appBean.setApp_icon(icon_d);
            appBean.setApp_name(appName);
            appBean.setIs_system(is_system);
            appBean.setPackage_name(packageName);
            appBean.setVersion_code(version_code);
            appBean.setVersion_name(version_name);
            data.add(appBean);
        }
        return data;
    }

    /**
     * 获取指定package信息
     * 
     * @param context
     * @param packagename
     * @return
     */
    public static final AppInfoBean getAppInfoByPackageName(Context context, String packagename) {
        AppInfoBean data = null;
        try {
            PackageManager pman = context.getPackageManager();
            PackageInfo mPackageInfo = pman.getPackageInfo(packagename, 0);
            String packageName = mPackageInfo.packageName;// 包名
            String version_name = mPackageInfo.versionName;
            int version_code = mPackageInfo.versionCode;
            ApplicationInfo appinfo = mPackageInfo.applicationInfo;// 应用
            Drawable icon_d = appinfo.loadIcon(pman);// 图标
            String appName = appinfo.loadLabel(pman).toString();// 应用名称
            boolean is_system = filterApp(appinfo);
            data = new AppInfoBean();
            data.setApp_icon(icon_d);
            data.setApp_name(appName);
            data.setIs_system(is_system);
            data.setPackage_name(packageName);
            data.setVersion_code(version_code);
            data.setVersion_name(version_name);
        } catch (Exception e) {
            data = null;
        }
        return data;
    }

    /**
     * 检测系统
     * 
     * @param appinfo
     * @return
     */

    public static final boolean filterApp(ApplicationInfo appinfo) {
        if ((appinfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((appinfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        } else {
            return false;
        }
    }

}