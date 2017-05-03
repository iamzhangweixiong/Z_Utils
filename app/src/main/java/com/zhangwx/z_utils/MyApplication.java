package com.zhangwx.z_utils;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * <p>
 * 全局获取 Context
 * </p>
 * <p>
 * Mainfest 中需要更改 android:name=".MyApplication"
 * </p>
 * Created by zhangwx on 2016/7/13.
 */

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Stetho.initializeWithDefaults(this);
    }
}

