package com.zhangwx.z_utils.Z_DB;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by zhangwx on 2017/5/3.
 */

public class UserInfoTable implements BaseColumns {

    private static final String TABLE_USER_INFO = "userInfo";

    private static final String COLUMN_ID = _ID;
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_SEX = "sex";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_USER_INFO + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_AGE + " INTEGER DEFAULT 0, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_SEX + " TEXT " + ")";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER_INFO;

    public static void createTable(SQLiteDatabase database){
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

            }
        }
    }
}
