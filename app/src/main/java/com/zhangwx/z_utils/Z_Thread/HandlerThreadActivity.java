package com.zhangwx.z_utils.Z_Thread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_Thread.HandlerThread.SubThreadCommunication;

/**
 * Created by zhangex on 2016/12/22.
 */
public class HandlerThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        findViewById(R.id.ThreadTest).setOnClickListener(v -> {
//            new SubThreadCommunication().test();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
