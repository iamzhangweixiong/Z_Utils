package com.zhangwx.z_utils.Z_DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhangwx.z_utils.MyApplication;
import com.zhangwx.z_utils.Z_DB.preference.PreferenceTable;

/**
 * Created by zhangwx on 2017/4/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "zUtils.db";

    private static DataBaseHelper sDataBaseHelper = null;

    public static DataBaseHelper getInstance() {
        if (sDataBaseHelper == null) {
            synchronized (DataBaseHelper.class) {
                if (sDataBaseHelper == null) {
                    return new DataBaseHelper(MyApplication.getContext());
                }
            }
        }
        return sDataBaseHelper;
    }

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        UserInfoTable.createTable(db);
        PreferenceTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserInfoTable.onUpgrade(db, oldVersion, newVersion);
    }

}
