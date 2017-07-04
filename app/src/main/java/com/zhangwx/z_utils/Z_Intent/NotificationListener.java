package com.zhangwx.z_utils.Z_Intent;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by zhangwx
 * on 2017/6/11.
 */
@SuppressLint("OverrideAbstract")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

    public static final String TAG = "NotificationListener";
    private static WeakReference<NotificationListener> mInstance;

    public NotificationListener() {
        mInstance = new WeakReference<NotificationListener>(this);
    }

    public static NotificationListener getInstance() {
        if (mInstance != null) {
            return mInstance.get();
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: isEnabled = " + NotificationHelper.isEnabled(this));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind:");
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
//        super.onNotificationPosted(sbn);
//        这里的 super 不能调，因为 4.3 系统此方法的实现是 abstract，会报 abstract method not implemented 错误

        //当系统收到一条通知栏通知时会回调此接口
        Log.e(TAG, "onNotificationPosted: sbn = " + sbn.toString());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        super.onNotificationRemoved(sbn);
//        这里的 super 不能调，因为 4.3 系统此方法的实现是 abstract，会报 abstract method not implemented 错误
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
