package com.zhangwx.z_utils.Z_DB.preference;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by zhangwx on 2017/7/17.
 */

public class PreferenceTable implements BaseColumns {

    public static final String TABLE_PREFERENCE = "preference";

    public static final String COLUMN_ID = _ID;
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_VALUE = "value";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_PREFERENCE + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_KEY + " TEXT NOT NULL, " +
            COLUMN_TYPE + " TEXT NOT NULL, " +
            COLUMN_VALUE + " TEXT " + ")";


    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }



}
