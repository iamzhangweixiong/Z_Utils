package com.zhangwx.z_utils.Z_ListView;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zhangwx.z_utils.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangweixiong on 2017/10/15.
 */

public class ListViewActivity extends Activity {

    @BindView(R.id.listview)
    ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        ButterKnife.bind(this);
        mListView.setAdapter(new ListViewAdapter(this));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
