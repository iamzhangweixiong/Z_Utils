package com.zhangwx.z_utils.Z_Intent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.zhangwx.z_utils.MainActivity;
import com.zhangwx.z_utils.R;

/**
 * Created by zhangwx
 * on 2017/6/11.
 */

public class NotificationHelper {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final int MAIN_REQUEST_CODE = 1111;
    private static final int OWN_REQUEST_CODE = 1112;
    private static final int NITIFY_ID = 1;

    // 判断用户是否开启了 Notification access
    public static boolean isEnabled(Context context) {
        if (context == null) {
            return false;
        }
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void showNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("title")
                .setSmallIcon(R.drawable.screen3_feed_icon_loading)
                .setContentText("content")
                .setPriority(Notification.PRIORITY_MAX)
                .setTicker("Ticker")
                .setAutoCancel(true);
        Notification notification = builder.build();
        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.notificationlayout);
        notification.contentView = mRemoteViews;

        PendingIntent mainIntent = PendingIntent.getActivity(
                context,
                MAIN_REQUEST_CODE,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.goMain, mainIntent);

        PendingIntent ownIntent = PendingIntent.getActivity(
                context,
                OWN_REQUEST_CODE,
                new Intent(context, IntentActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.goOwn, ownIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NITIFY_ID, notification);
    }
}
