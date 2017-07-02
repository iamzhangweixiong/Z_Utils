package com.zhangwx.z_utils.Z_DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by zhangwx on 2017/5/3.
 */

public class UserInfoTable implements BaseColumns {

    public static final String TABLE_USER_INFO = "z_userInfo";

    public static final String COLUMN_ID = _ID;
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_SEX = "sex";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_USER_INFO + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_AGE + " INTEGER DEFAULT 0, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_SEX + " TEXT " + ")";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER_INFO;

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        try {
            database.beginTransaction();
            database.execSQL(DROP_TABLE);
            createTable(database);
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                database.endTransaction();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void insert(ContentValues values) {
        DataBaseHelper.getInstance().getWritableDatabase().insert(TABLE_USER_INFO, null, values);
    }

    /**
     * 批量插入
     *
     * @param values
     */
    public static void bulkInsert(ContentValues[] values) {
        SQLiteDatabase database = DataBaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        try {
            for (ContentValues value : values) {
                DataBaseHelper.getInstance().getWritableDatabase().insert(TABLE_USER_INFO, null, value);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public static void delete(String whereClause, String[] whereArgs) {
        DataBaseHelper.getInstance().getWritableDatabase().delete(TABLE_USER_INFO, whereClause, whereArgs);
    }

    public static void update(ContentValues values, String whereClause, String[] whereArgs) {
        DataBaseHelper.getInstance().getWritableDatabase().update(TABLE_USER_INFO, values, whereClause, whereArgs);
    }

    public static Cursor query(String[] columns, String selection, String[] selectionArgs) {
        return DataBaseHelper.getInstance().getWritableDatabase().query(TABLE_USER_INFO, columns, selection, selectionArgs, null, null, null);
    }
}
