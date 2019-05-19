package com.zhangwx.z_utils.Z_Thread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
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
    private static final int MSG_UPDATE_INFO = 0x10;
    int i = 10;
    private TextView mText;

    private Handler mMsgController;
    private HandlerThread mCheckMsgThread;
    private TimerTest mTimerTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
//        initBackThread();
//        mText = ViewUtils.$(this, R.id.id_textview);
//        mMsgController.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 1000);

        findViewById(R.id.ThreadTest).setOnClickListener(v -> {
            mTimerTest = new TimerTest();
//            mTimerTest.startTimerTest();


        });

//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d("receiver", intent.getAction());
//            }
//        }, new IntentFilter(ALARM_START));
    }

    private void initBackThread() {

        mCheckMsgThread = new HandlerThread("check-message-coming");
        mCheckMsgThread.start();
        mMsgController = new Handler(mCheckMsgThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String result = "实时更新：<font color='red'>%d</font>";
                        result = String.format(Locale.getDefault(), result, (int) (Math.random() * 3000 + 1000));
                        mText.setText(Html.fromHtml(result));
                    }
                });
                if (i > 0) {
                    mMsgController.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 1000);
                    i--;
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mCheckMsgThread.quit();
//        mTimerTest.stopTimer();
    }
}
