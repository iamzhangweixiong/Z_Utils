package com.zhangwx.z_utils.Z_Intent;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * Created by zhangwx
 * on 2017/6/11.
 */

public class NotificationHelper {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    // 判断用户是否开启了 Notification access
    private boolean isEnabled(Context context) {
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
}
