package com.search.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inx-android on 7/5/18.
 */

public class AppListModel {
    private List<AppList> appListArrayList=new ArrayList<>();

    public List<AppList> getAppListArrayList() {
        return appListArrayList;
    }

    public void setAppListArrayList(List<AppList> appListArrayList) {
        this.appListArrayList = appListArrayList;
    }
}
