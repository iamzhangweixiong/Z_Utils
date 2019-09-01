package com.zhangwx.z_utils.Z_Thread.HandlerThread;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import java.lang.ref.WeakReference;
import java.util.Locale;

public class HandlerThreadTest {

    private static final int MSG_UPDATE_INFO = 0x10;
    private int i = 10;

    private Handler mMsgController;
    private android.os.HandlerThread mCheckMsgThread;

    private void initBackThread(Activity activity) {
        WeakReference<Activity> reference = new WeakReference<>(activity);

        mCheckMsgThread = new HandlerThread("check-message-coming");
        mCheckMsgThread.start();
        mMsgController = new Handler(mCheckMsgThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                reference.get().runOnUiThread(() -> {
                    String result = "实时更新：<font color='red'>%d</font>";
                    result = String.format(Locale.getDefault(), result, (int) (Math.random() * 3000 + 1000));
//                    mText.setText(Html.fromHtml(result));
                });
                if (i > 0) {
                    mMsgController.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 1000);
                    i--;
                }
            }
        };
    }
}
