package com.zhangwx.z_utils.Z_Thread;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import androidx.appcompat.app.AppCompatActivity;
import com.zhangwx.z_utils.R;

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

        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
