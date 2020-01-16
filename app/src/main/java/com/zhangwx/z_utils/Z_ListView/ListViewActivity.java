package com.zhangwx.z_utils.Z_ListView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.zhangwx.z_utils.R;

/**
 * Created by zhangweixiong on 2017/10/15.
 */

public class ListViewActivity extends Activity {

    ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        mListView = findViewById(R.id.listview);
        mListView.setAdapter(new ListViewAdapter(this));
        mListView.setOnItemClickListener((parent, view, position, id)
                -> Toast.makeText(getApplicationContext(), "onItemClick", Toast.LENGTH_SHORT).show()
        );
    }

}
