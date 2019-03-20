package com.zhangwx.z_utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

/**
 * <p>
 * 全局获取 Context
 * </p>
 * <p>
 * Mainfest 中需要更改 android:name=".MyApplication"
 * </p>
 * Created by zhangwx on 2016/7/13.
 */

public class MyApplication extends MultiDexApplication {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Stetho.initializeWithDefaults(this);
        LeakCanary.install(this);
    }
}

