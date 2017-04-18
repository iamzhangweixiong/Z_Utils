package com.zhangwx.z_utils.Z_DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhangwx on 2017/4/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper implements Operations {


    public static final int VERSION = 1;
    public static final String TABLE_NAME = "zUtils.db";

    public DataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void add() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public void query() {

    }
}
