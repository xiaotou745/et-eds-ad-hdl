/*
 * File Name: AppInfoBean.java 
 * History:
 * Created by Administrator on 2015-4-27
 */
package com.eds.supermanc.beans;

import android.graphics.drawable.Drawable;

/**
 * app (Description)
 * 
 * @author zaokafei
 * @version 1.0
 * @date 2015-4-27
 */
public class AppInfoBean {
    // ==========================================================================
    // Constants
    // ==========================================================================
    private Drawable app_icon;
    private String app_name;
    private String package_name;
    private boolean is_system;
    private int version_code;
    private String version_name;

    // ==========================================================================
    // Fields
    // ==========================================================================
    public Drawable getApp_icon() {
        return app_icon;
    }

    public String getApp_name() {
        return app_name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public boolean isIs_system() {
        return is_system;
    }

    public void setApp_icon(Drawable app_icon) {
        this.app_icon = app_icon;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public void setIs_system(boolean is_system) {
        this.is_system = is_system;
    }

    public int getVersion_code() {
        return version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

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

    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================
}
