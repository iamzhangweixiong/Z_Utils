package com.zhangwx.z_utils.Z_DB.config;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by zhangwx on 2017/3/23 0023.
 */

public class SyncConfigFile implements IConfigFile {
    private Context mContext;
    private ContentResolver mContentResolver;
    private String mConfigName;

    public SyncConfigFile(Context context, String configName) {
        mContext = context.getApplicationContext();
        mContentResolver = context.getContentResolver();
        mConfigName = configName;
    }

    @Override
    public String getConfigName() {
        return mConfigName;
    }

    public void setBooleanValue(String key, boolean value) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ConfigProvider.EXTRA_TYPE, ConfigProvider.TYPE_BOOLEAN);
        contentValues.put(ConfigProvider.EXTRA_KEY, key);
        contentValues.put(ConfigProvider.EXTRA_VALUE, value);

        Uri uri = ConfigProvider.convertUri(mContext, mConfigName);
        mContentResolver.update(uri, contentValues, null, null);
    }

    public void setLongValue(String key, long value) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ConfigProvider.EXTRA_TYPE, ConfigProvider.TYPE_LONG);
        contentValues.put(ConfigProvider.EXTRA_KEY, key);
        contentValues.put(ConfigProvider.EXTRA_VALUE, value);

        Uri uri = ConfigProvider.convertUri(mContext, mConfigName);
        mContentResolver.update(uri, contentValues, null, null);
    }

    public void setIntValue(String key, int value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigProvider.EXTRA_TYPE, ConfigProvider.TYPE_INT);
        contentValues.put(ConfigProvider.EXTRA_KEY, key);
        contentValues.put(ConfigProvider.EXTRA_VALUE, value);

        Uri uri = ConfigProvider.convertUri(mContext, mConfigName);
        mContentResolver.update(uri, contentValues, null, null);
    }

    public void setStringValue(String key, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigProvider.EXTRA_TYPE, ConfigProvider.TYPE_STRING);
        contentValues.put(ConfigProvider.EXTRA_KEY, key);
        contentValues.put(ConfigProvider.EXTRA_VALUE, value);

        Uri uri = ConfigProvider.convertUri(mContext, mConfigName);
        mContentResolver.update(uri, contentValues, null, null);
    }

    public void removes(List<String> keys) {
        if (keys == null || keys.size() == 0) {
            return;
        }
        String[] keyArray = new String[keys.size()];
        keyArray = keys.toArray(keyArray);
        Uri uri = ConfigProvider.convertUri(mContext, mConfigName);
        mContentResolver.delete(uri, null, keyArray);
    }

    public long getLongValue(String key, long defValue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigProvider.EXTRA_TYPE, ConfigProvider.TYPE_LONG);
        contentValues.put(ConfigProvider.EXTRA_KEY, key);
        contentValues.put(ConfigProvider.EXTRA_VALUE, defValue);

        Uri uri = ConfigProvider.convertUri(mContext, mConfigName);
        Uri result = mContentResolver.insert(uri, contentValues);

        if (result == null) {
            return defValue;
        }
        String r = ConfigProvider.splitConfigParam(result);
        if (TextUtils.isEmpty(r) || TextUtils.equals(r, "null")) {
            return defValue;
        } else {
            return Long.valueOf(r);
        }
    }

    public boolean getBooleanValue(String key, boolean defValue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigProvider.EXTRA_TYPE, ConfigProvider.TYPE_BOOLEAN);
        contentValues.put(ConfigProvider.EXTRA_KEY, key);
        contentValues.put(ConfigProvider.EXTRA_VALUE, defValue);

        Uri uri = ConfigProvider.convertUri(mContext, mConfigName);
        Uri result = mContentResolver.insert(uri, contentValues);

        if (result == null) {
            return defValue;
        }
        String r = ConfigProvider.splitConfigParam(result);
        if (TextUtils.isEmpty(r) || TextUtils.equals(r, "null")) {
            return defValue;
        } else {
            return Boolean.valueOf(r);
        }
    }

    public int getIntValue(String key, int defValue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigProvider.EXTRA_TYPE, ConfigProvider.TYPE_INT);
        contentValues.put(ConfigProvider.EXTRA_KEY, key);
        contentValues.put(ConfigProvider.EXTRA_VALUE, defValue);

        Uri uri = ConfigProvider.convertUri(mContext, mConfigName);
        Uri result = mContentResolver.insert(uri, contentValues);

        if (result == null) {
            return defValue;
        }
        String r = ConfigProvider.splitConfigParam(result);
        if (TextUtils.isEmpty(r) || TextUtils.equals(r, "null")) {
            return defValue;
        } else {
            return Integer.valueOf(r);
        }
    }

    public String getStringValue(String key, String defValue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigProvider.EXTRA_TYPE, ConfigProvider.TYPE_STRING);
        contentValues.put(ConfigProvider.EXTRA_KEY, key);
        contentValues.put(ConfigProvider.EXTRA_VALUE, defValue);

        Uri uri = ConfigProvider.convertUri(mContext, mConfigName);
        Uri result = mContentResolver.insert(uri, contentValues);

        if (result == null) {
            return defValue;
        }

        String r = ConfigProvider.splitConfigParam(result);
        if (TextUtils.isEmpty(r) || TextUtils.equals(r, "null")) {
            return defValue;
        } else {
            return String.valueOf(r);
        }
    }
}
