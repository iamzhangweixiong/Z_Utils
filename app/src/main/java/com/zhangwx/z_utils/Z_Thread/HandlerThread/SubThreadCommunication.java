package com.zhangwx.z_utils.Z_Thread.HandlerThread;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class SubThreadCommunication {

    private static Handler handler;

    public void test() {
        Thread handleThread = new Thread(() -> {
            Looper.prepare();
            handler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 100) {
                        System.out.println("handler get msg");
                    }
                }
            };
            Looper.loop();
        });
        handleThread.setName("handle");
        handleThread.start();


        Thread sendThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = handler.obtainMessage(100);
            handler.sendMessage(msg);
        });
        sendThread.setName("send");
        sendThread.start();
    }

    // 防止内存泄漏
    public void OnDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handler.getLooper().quitSafely();
        } else {
            handler.getLooper().quit();
        }
        handler.removeCallbacksAndMessages(null);
    }
}
