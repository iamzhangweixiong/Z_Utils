package com.zhangwx.z_utils.Z_Pub;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by zhangwx on 2016/12/12.
 */
public class MSMessageLooper extends HandlerThread {
    private static MSMessageLooper sInstance;
    private static Handler sHandler;

    public MSMessageLooper() {
        super("MSMessageLooper", android.os.Process.THREAD_PRIORITY_DEFAULT);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new MSMessageLooper();
            sInstance.start();
            sHandler = new Handler(sInstance.getLooper());
        }
    }

    public static void post(final Runnable runnable) {
        synchronized (MSMessageLooper.class) {
            ensureThreadLocked();
            sHandler.post(runnable);
        }
    }

    public static void postDelayed(final Runnable runnable, long nDelay) {
        synchronized (MSMessageLooper.class) {
            ensureThreadLocked();
            sHandler.postDelayed(runnable, nDelay);
        }
    }

    public static void removeTask(final Runnable runnable) {
        synchronized (MSMessageLooper.class) {
            ensureThreadLocked();
            sHandler.removeCallbacks(runnable);
        }
    }
}
