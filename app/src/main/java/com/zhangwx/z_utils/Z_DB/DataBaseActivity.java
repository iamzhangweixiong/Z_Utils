package com.zhangwx.z_utils.Z_DB;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
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
import java.util.zip.Inflater;

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
                actionQuery();
                break;
        }
    }

    private void actionQuery() {
        Cursor cursor = UserInfoTable.query(new String[]{UserInfoTable.COLUMN_SEX}, UserInfoTable.COLUMN_SEX + " = ? ", new String[]{"22"});
        while (cursor.moveToNext()) {
            final int pos = cursor.getPosition();
            mDataList.add(pos, cursor.getString(cursor.getColumnIndex(UserInfoTable.COLUMN_NAME)) + "  "
                    + cursor.getString(cursor.getColumnIndex(UserInfoTable.COLUMN_AGE)) + "  "
                    + cursor.getString(cursor.getColumnIndex(UserInfoTable.COLUMN_SEX)));
        }
        mDbAdapter.notifyDataSetChanged();
    }

    private void actionUpdate() {
        ContentValues value = new ContentValues();
        value.put(UserInfoTable.COLUMN_AGE, 10);
        value.put(UserInfoTable.COLUMN_NAME, "weixiong");
        value.put(UserInfoTable.COLUMN_SEX, "man");
        UserInfoTable.update(value, UserInfoTable.COLUMN_AGE + " = ? ", new String[]{"22"});
    }

    private void actionDelete() {
        UserInfoTable.delete(UserInfoTable.COLUMN_AGE + " = ? ", new String[]{"22"});
    }

    private void actionAdd() {
        ContentValues value = new ContentValues();
        value.put(UserInfoTable.COLUMN_AGE, 22);
        value.put(UserInfoTable.COLUMN_NAME, "ZHANG");
        value.put(UserInfoTable.COLUMN_SEX, "man");
        UserInfoTable.insert(value);
    }

    private class DBAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dbquerycard, null);
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
