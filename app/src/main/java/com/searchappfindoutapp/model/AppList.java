package com.searchappfindoutapp.model;

/**
 * Created by inx-android on 21/3/18.
 */

public class AppList {

    private int app_id;
    private String app_package_name;
    private String app_name;
    private byte[] app_icon;


    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public String getApp_package_name() {
        return app_package_name;
    }

    public void setApp_package_name(String app_package_name) {
        this.app_package_name = app_package_name;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public byte[] getApp_icon() {
        return app_icon;
    }

    public void setApp_icon(byte[] app_icon) {
        this.app_icon = app_icon;
    }
}
