package com.zhangwx.z_utils.Z_DB;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhangwx.z_utils.BuildConfig;

import static com.zhangwx.z_utils.Z_DB.UserInfoTable.TABLE_USER_INFO;


/**
 * Created by zhangwx on 2017/7/2.
 * https://developer.android.com/guide/topics/providers/content-provider-basics.html
 * <p>
 * "/userInfo" 选择 userInfo 中的所有内容
 * <p>
 * "/userInfo/#" 选择 userInfo id 为 x 的一条内容
 * <p>
 * 查询 id 为 10 的那一行的 uri ：Uri.parse(UserInfoContentProvider.CONTENT_URI + "/10");
 * 这里的 '／' 需要自己加上，uri.getPathSegments().get(1) 才能拿到
 * 然而添加到 sUriMatcher 中的 PATH_ID 又要加上 '#' ！！！？？？
 * 不知道为什么要这么设计，个人觉得这样太挫逼了
 * </p>
 *
 * <p>
 * /vnd.zhangwx.userInfo
 * MIME 类型标准写法
 * CONTENT_URI：content://com.example.trains/Line2/5
 * --> MIME：ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.example.line2"
 */

public class UserInfoContentProvider extends ContentProvider {
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";

    public static final String PATH = "/userInfo"; // uri.getPathSegments().get(0)
    public static final String PATH_ID = "/userInfo/#"; // uri.getPathSegments().get(1)

    public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.zhangwx.userInfo";
    public static final String CONTENT_ID_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.zhangwx.userInfo";

    public static final int USER_INFO = 1;
    public static final int USER_INFO_ID = 2;

    private static UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, PATH, USER_INFO);
        sUriMatcher.addURI(AUTHORITY, PATH_ID, USER_INFO_ID);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projections, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case USER_INFO:
                break;
            case USER_INFO_ID:
                builder.setTables(TABLE_USER_INFO);
                builder.appendWhere(UserInfoTable.COLUMN_ID + " = " + uri.getPathSegments().get(1));
                return builder.query(
                        DataBaseHelper.getInstance().getWritableDatabase(),
                        projections,
                        selection,
                        selectionArgs,
                        null, null, null);
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // 可以返回空
        switch (sUriMatcher.match(uri)) {
            case USER_INFO:
                return CONTENT_TYPE;
            case USER_INFO_ID:
                return CONTENT_ID_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


}
