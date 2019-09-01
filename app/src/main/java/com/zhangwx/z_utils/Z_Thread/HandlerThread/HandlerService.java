package com.zhangwx.z_utils.Z_Thread.HandlerThread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * 用 HandlerThreadTest 实现不需要直接更新 UI 的后台操作
 * Created by zhangwx on 2017/5/14.
 */

public class HandlerService {

    private static final String SERVICE_NAME = "HandlerService";

    public static final int MSG_LOAD_DATA = 0x01;

    private Handler mHandler;
    private HandlerThread mHandlerThread;

    private static HandlerService sInstance;

    public HandlerService getInstance() {
        if (sInstance == null) {
            synchronized (HandlerService.class) {
                if (sInstance == null) {
                    sInstance = new HandlerService();
                }
            }
        }
        return sInstance;
    }

    private HandlerService() {
        mHandlerThread = new HandlerThread(SERVICE_NAME, android.os.Process.THREAD_PRIORITY_DEFAULT);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handleAllMessage(msg.what);
            }
        };
    }

    private void handleAllMessage(final int msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                switch (msg) {
                    case MSG_LOAD_DATA:
                        //do load
                        break;
                }
            }
        });
    }

    public void post(int what) {
        mHandler.sendEmptyMessage(what);
    }

    /**
     * 停止 HandlerThreadTest 的 Looper
     */
    public void quit() {
        mHandlerThread.quit();
    }
}
