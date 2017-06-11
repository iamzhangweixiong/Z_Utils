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
        Log.e(TAG, "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind:");
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        //当系统收到一条通知栏通知时会回调此接口
        Log.e(TAG, "onNotificationPosted: sbn = " + sbn.toString());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
        Log.e(TAG, "onNotificationPosted: sbn = " + sbn.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
