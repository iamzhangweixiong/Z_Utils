package com.zhangwx.z_utils.Z_DB.config;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * Created by singun on 2017/3/23 0023.
 */

public class ConfigFile implements IConfigFile {
    private String mFileName = null;
    private SharedPreferences mSharedPref = null;

    public ConfigFile(Context context, String fileName) {
        init(context, fileName, Context.MODE_PRIVATE);
    }

    public ConfigFile(Context context, String fileName, int mode) {
        init(context, fileName, mode);
    }

    private void init(Context context, String fileName, int mode) {
        mSharedPref = context.getSharedPreferences(fileName, mode);
        mFileName = fileName;
    }

    @Override
    public String getConfigName() {
        return mFileName;
    }

    @Override
    public long getLongValue(String key, long defValue) {
        return mSharedPref.getLong(key, defValue);
    }

    @Override
    public int getIntValue(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    @Override
    public boolean getBooleanValue(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    @Override
    public String getStringValue(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    @Override
    public void setLongValue(String key, long value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    @Override
    public void setIntValue(String key, int value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    @Override
    public void setBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @Override
    public void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void removes(List<String> keys) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }
}
