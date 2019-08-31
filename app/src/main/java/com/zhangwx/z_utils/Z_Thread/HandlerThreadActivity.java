package com.zhangwx.z_utils.Z_Thread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.Locale;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_Thread.Timer.TimerTest;
import com.zhangwx.z_utils.Z_UI.ViewUtils;

import static com.zhangwx.z_utils.Z_Thread.Timer.TimerTest.ALARM_START;

/**
 * Created by zhangex on 2016/12/22.
 */
public class HandlerThreadActivity extends AppCompatActivity {
    private TextView mText;

    private TimerTest mTimerTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
//        initBackThread();
//        mText = ViewUtils.$(this, R.id.id_textview);
//        mMsgController.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 1000);

        findViewById(R.id.ThreadTest).setOnClickListener(v -> {
//            mTimerTest = new TimerTest();
//            mTimerTest.startTimerTest();

//            test();
        });

//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d("receiver", intent.getAction());
//            }
//        }, new IntentFilter(ALARM_START));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mCheckMsgThread.quit();
//        mTimerTest.stopTimer();
    }
}
