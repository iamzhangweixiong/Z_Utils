package com.zhangwx.z_utils.Z_DB;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_UI.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwx on 2017/4/17.
 */

public class DataBaseActivity extends Activity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private DBAdapter mDbAdapter;
    private List<String> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        ViewUtils.$(this, R.id.db_add).setOnClickListener(this);
        ViewUtils.$(this, R.id.db_delete).setOnClickListener(this);
        ViewUtils.$(this, R.id.db_update).setOnClickListener(this);
        ViewUtils.$(this, R.id.db_query).setOnClickListener(this);
        mRecyclerView = ViewUtils.$(this, R.id.DBDataList);
        mDbAdapter = new DBAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mDbAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.db_add:
                actionAdd();
                break;
            case R.id.db_delete:
                actionDelete();
                break;
            case R.id.db_update:
                actionUpdate();
                break;
            case R.id.db_query:
//                actionQuery();
                providerQuery();
                break;
        }
    }

    /**
     * query 中的 projection 参数指定要返回哪些列的内容，传 null 表示返回所有的列
     * 无法理解为什么这里的 "／" 要自己加上
     */
    private void providerQuery() {
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(UserInfoContentProvider.CONTENT_URI + "/10");
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                mDataList.clear();
                do {
                    int pos = cursor.getPosition();
                    mDataList.add(pos, cursor.getString(cursor.getColumnIndex(UserInfoTable.COLUMN_NAME)) + "  "
                            + cursor.getString(cursor.getColumnIndex(UserInfoTable.COLUMN_AGE)) + "  "
                            + cursor.getString(cursor.getColumnIndex(UserInfoTable.COLUMN_SEX)));

                } while (cursor.moveToNext());
            }
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
            }
        }
        mDbAdapter.notifyDataSetChanged();
    }

    private void actionQuery() {
        Cursor cursor = null;
        try {
            cursor = UserInfoTable.query(null, UserInfoTable.COLUMN_SEX + " = ? ", new String[]{"man"});
            if (cursor != null && cursor.moveToFirst()) {
                mDataList.clear();
                do {
                    int pos = cursor.getPosition();
                    mDataList.add(pos, cursor.getString(cursor.getColumnIndex(UserInfoTable.COLUMN_NAME)) + "  "
                            + cursor.getString(cursor.getColumnIndex(UserInfoTable.COLUMN_AGE)) + "  "
                            + cursor.getString(cursor.getColumnIndex(UserInfoTable.COLUMN_SEX)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
            }
        }
        mDbAdapter.notifyDataSetChanged();
    }

    private void actionUpdate() {
        ContentValues value = new ContentValues();
        value.put(UserInfoTable.COLUMN_AGE, 10);
        value.put(UserInfoTable.COLUMN_NAME, "weixiong");
        value.put(UserInfoTable.COLUMN_SEX, "man");
        UserInfoTable.update(value, UserInfoTable.COLUMN_AGE + " = ? ", new String[]{"22"});
        actionQuery();
    }

    private void actionDelete() {
        UserInfoTable.delete(UserInfoTable.COLUMN_AGE + " = ? ", new String[]{"22"});
        actionQuery();
    }

    private void actionAdd() {
        ContentValues value = new ContentValues();
        value.put(UserInfoTable.COLUMN_AGE, 22);
        value.put(UserInfoTable.COLUMN_NAME, "ZHANG");
        value.put(UserInfoTable.COLUMN_SEX, "man");
        UserInfoTable.insert(value);
        actionQuery();
    }

    private class DBAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dbquerycard, parent, false);
            DBViewHolder viewHolder = new DBViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((DBViewHolder) holder).getTextView().setText(mDataList.get(position));
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public class DBViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;

            public TextView getTextView() {
                return textView;
            }

            public DBViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.db_result);
            }
        }
    }
}
