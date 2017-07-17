package com.zhangwx.z_utils.Z_DB.preference;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.zhangwx.z_utils.Z_DB.UserInfoContentProvider;
import com.zhangwx.z_utils.Z_DB.UserInfoTable;

import java.util.List;

/**
 * Created by zhangwx on 2017/7/17.
 */

public class PreferenceManager implements IPreferenceAction {

    private Context mContext;
    public void PreferenceManager(Context context){
        this.mContext = context;
    }

    @Override
    public long getLongValue(String key, long defValue) {
        return 0;
    }

    @Override
    public boolean getBooleanValue(String key, boolean defValue) {
        return false;
    }

    @Override
    public int getIntValue(String key, int defValue) {
        return 0;
    }

    @Override
    public String getStringValue(String key, String defValue) {
        Cursor cursor = null;
        try {
            Uri uri = PreferenceContentProvider.CONTENT_URI;
            cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                } while (cursor.moveToNext());
            }
        } finally {
            closeCursor(cursor);
        }
        return null;
    }

    @Override
    public void setBooleanValue(String key, boolean value) {


    }

    @Override
    public void setLongValue(String key, long value) {

    }

    @Override
    public void setIntValue(String key, int value) {

    }

    @Override
    public void setStringValue(String key, String value) {

    }

    @Override
    public void removes(List<String> keys) {

    }

    private void closeCursor(Cursor cursor){
        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
        }
    }
}
