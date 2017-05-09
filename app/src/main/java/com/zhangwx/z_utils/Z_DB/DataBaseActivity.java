package com.zhangwx.z_utils.Z_DB;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_UI.ViewUtils;

/**
 * Created by zhangwx on 2017/4/17.
 */

public class DataBaseActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        ViewUtils.$(this, R.id.db_add).setOnClickListener(this);
        ViewUtils.$(this, R.id.db_delete).setOnClickListener(this);
        ViewUtils.$(this, R.id.db_update).setOnClickListener(this);
        ViewUtils.$(this, R.id.db_query).setOnClickListener(this);

        ViewUtils.$(this, R.id.DataList);
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

    }

    private void actionUpdate() {

    }

    private void actionDelete() {

    }

    private void actionAdd() {
        ContentValues value = new ContentValues();
    }
}
