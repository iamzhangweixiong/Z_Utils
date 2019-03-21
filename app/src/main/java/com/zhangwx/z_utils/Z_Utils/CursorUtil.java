package com.zhangwx.z_utils.Z_Utils;

import android.database.Cursor;

/**
 * 游标工具
 * @author zhangwx
 */
public class CursorUtil {
    public static long getLongValue(Cursor cursor, String columnName) {
        int idx = cursor.getColumnIndex(columnName);
        if (idx != -1) {
            return cursor.getLong(idx);
        }
        return 0;
    }

    public static int getIntValue(Cursor cursor, String columnName) {
        int idx = cursor.getColumnIndex(columnName);
        if (idx != -1) {
            return cursor.getInt(idx);
        }
        return 0;
    }

    public static String getStringValue(Cursor cursor, String columnName) {
        int idx = cursor.getColumnIndex(columnName);
        if (idx != -1) {
            return cursor.getString(idx);
        }
        return "";
    }
}
