package com.search;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class BaseApplication extends Application {

    private static BaseApplication sBaseApplication;


    public static BaseApplication getBaseApplication() {
        return sBaseApplication;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sBaseApplication = BaseApplication.this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/JosefinSans-SemiBold.ttf").build());

    }






}