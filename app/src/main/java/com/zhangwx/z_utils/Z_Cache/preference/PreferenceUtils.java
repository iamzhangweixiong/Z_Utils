package com.zhangwx.z_utils.Z_Cache.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhangwx.z_utils.MyApplication;

public class PreferenceUtils {
    private static SharedPreferences mSharedPref = MyApplication.getContext().getSharedPreferences("cache", Context.MODE_PRIVATE);

    public static String getStringValue(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
