package com.zhangwx.z_utils.java_base.线程;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.HashMap;

public class HandlerTest {
    private Handler handler;

    HandlerTest() {

    }

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

    public static void main(String[] args) {
        new HandlerTest().test();
    }
}
