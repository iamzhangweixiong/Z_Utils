package com.zhangwx.z_utils.Z_DB.preference;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhangwx.z_utils.BuildConfig;

/**
 * Created by zhangwx on 2017/7/17.
 */

public class PreferenceContentProvider extends ContentProvider{

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    public static final String PATH = "/preference";

    public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
