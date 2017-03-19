package com.zhangwx.z_utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import java.util.Locale;

import com.zhangwx.z_utils.Z_UI.ViewUtils;

/**
 * Created by Administrator on 2016/12/22.
 */
public class HandlerThreadActivity  extends AppCompatActivity {
    private static final int MSG_UPDATE_INFO = 0x10;
    int i = 10;
    private TextView mText;

    private Handler mMsgController;
    private HandlerThread mCheckMsgThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        initBackThread();
        mText = ViewUtils.$(this, R.id.id_textview);
        mMsgController.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 1000);
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
        mCheckMsgThread.quit();
    }
}
