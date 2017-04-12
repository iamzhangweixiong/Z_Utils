package com.zhangwx.z_utils.Z_Pub;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by zhangwx on 2016/9/5.
 */
public final class BatteryUtil {

    public static class BatteryInfo {
        public int level = -1;
        public int scale = -1;
        public boolean plugged = false;
    }

    // /< 读取电量百分比
    public static int getBatteryPercent(Context context) {
        try {
            if (null == context)
                return 0;

            Intent batteryInfoIntent = context.registerReceiver(null, 
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            if (null == batteryInfoIntent)
                return 0;

            return batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        } catch (Exception e) {

        }

        return 0;
    }

    // /< 读取电量百分比

    public static BatteryInfo getBatteryInfo(Context context) {
        BatteryInfo info = new BatteryInfo();

        try {
            if (null == context)
                return null;
            Intent intent = context.registerReceiver(null,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            info.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            info.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            info.plugged = plugged == BatteryManager.BATTERY_PLUGGED_AC
                    || plugged == BatteryManager.BATTERY_PLUGGED_USB;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }
    public static boolean isBatteryPlugIn(Context context) {
        BatteryInfo info = getBatteryInfo(context);
        if (info == null) {
            return false;
        }
        return info.plugged;
    }
//    public static float getBatteryValue(Context context) {
//        try {
//            if (null == context)
//                return 0.0f;
//            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//            Intent batteryStatus = context.registerReceiver(null, ifilter);
//            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//            return level / (float) scale;
//        } catch (Exception e) {
//        }
//        return 0.0f;
//    }
}
