package com.zhangwx.z_utils.Z_Thread.HandlerThread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class SubThreadCommunication {

    private Handler handler;

    public void test() {
        Thread handleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 100) {
                            System.out.println("get msg");
                        }
                    }
                };
                Looper.loop();
            }
        });
        handleThread.setName("handle");
        handleThread.start();

        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage(100);
                handler.sendMessage(msg);
            }
        });
        sendThread.setName("send");
        sendThread.start();
    }
}
