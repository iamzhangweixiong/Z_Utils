package com.zhangwx.z_utils.Z_DB.config;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ConfigProvider extends ContentProvider {
    private static String CONTENT_URI;

    public static String EXTRA_TYPE = "type";
    public static String EXTRA_KEY = "key";
    public static String EXTRA_VALUE = "value";

    public static final int TYPE_BOOLEAN = 1;
    public static final int TYPE_INT = 2;
    public static final int TYPE_LONG = 3;
    public static final int TYPE_STRING = 4;

    private Map<String, IConfigFile> mConfigMap;

    static Uri convertUri(Context context, String param) {
        return Uri.parse(getContentProviderUri(context) + "/" + param);
    }

    public static String getContentProviderUri(Context context) {
        if (CONTENT_URI == null) {
            CONTENT_URI = ContentResolver.SCHEME_CONTENT + "://" + context.getPackageName() + ".securelogin.config";
        }
        return CONTENT_URI;
    }

    public static void setContentProviderUri(String uri) {
        CONTENT_URI = uri;
    }

    public static String splitConfigParam(Uri uri) {
        return uri.getLastPathSegment();
    }

    @Override
    public boolean onCreate() {
        initConfigMap();
        return true;
    }

    private synchronized void initConfigMap() {
        mConfigMap = new HashMap<>();
        updateConfigMap(mConfigMap);
    }

    protected abstract void updateConfigMap(Map<String, IConfigFile> configMap);

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String configName = splitConfigParam(uri);
        IConfigFile appConfig = mConfigMap.get(configName);
        if (appConfig == null) {
            return null;
        }

        String res = "";
        int nType = values.getAsInteger(EXTRA_TYPE);
        if (nType == TYPE_BOOLEAN) {
            res += appConfig.getBooleanValue(values.getAsString(EXTRA_KEY),
                    values.getAsBoolean(EXTRA_VALUE));
        } else if (nType == TYPE_STRING) {
            res += appConfig.getStringValue(values.getAsString(EXTRA_KEY),
                    values.getAsString(EXTRA_VALUE));
        } else if (nType == TYPE_INT) {
            res += appConfig.getIntValue(values.getAsString(EXTRA_KEY),
                    values.getAsInteger(EXTRA_VALUE));
        } else if (nType == TYPE_LONG) {
            res += appConfig.getLongValue(values.getAsString(EXTRA_KEY),
                    values.getAsLong(EXTRA_VALUE));
        }

        return convertUri(getContext(), res);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (selectionArgs == null || selectionArgs.length == 0) {
            return 0;
        }
        String configName = splitConfigParam(uri);
        IConfigFile appConfig = mConfigMap.get(configName);
        if (appConfig == null) {
            return 0;
        }
        List<String> keyList = new ArrayList<>();
        Collections.addAll(keyList, selectionArgs);
        appConfig.removes(keyList);
        return keyList.size();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String configName = splitConfigParam(uri);
        IConfigFile appConfig = mConfigMap.get(configName);
        if (appConfig == null) {
            return 0;
        }

        int nType = values.getAsInteger(EXTRA_TYPE);
        if (nType == TYPE_BOOLEAN) {
            appConfig.setBooleanValue(values.getAsString(EXTRA_KEY),
                    values.getAsBoolean(EXTRA_VALUE));
        } else if (nType == TYPE_STRING) {
            appConfig.setStringValue(values.getAsString(EXTRA_KEY),
                    values.getAsString(EXTRA_VALUE));
        } else if (nType == TYPE_INT) {
            appConfig.setIntValue(values.getAsString(EXTRA_KEY),
                    values.getAsInteger(EXTRA_VALUE));
        } else if (nType == TYPE_LONG) {
            appConfig.setLongValue(values.getAsString(EXTRA_KEY),
                    values.getAsLong(EXTRA_VALUE));
        }
        return 1;
    }

}
