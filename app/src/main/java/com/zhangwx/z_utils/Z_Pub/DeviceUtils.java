package com.zhangwx.z_utils.Z_Pub;


import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.Locale;

public class DeviceUtils {
    private static final int UUID_LENGTH = 32;
    private static final Locale mPhoneLocale = null;

    public static Locale getPhoneLocale(Context context) {
        if (mPhoneLocale == null) {
            return context.getResources().getConfiguration().locale;
        }
        return mPhoneLocale;
    }

    public static String getAndroidID(Context context) {
        try {
            ContentResolver cr = context.getContentResolver();
            String androidID = Settings.Secure.getString(cr, Settings.Secure.ANDROID_ID);
            if (androidID == null) {
                androidID = "";
            }
            return androidID;
        } catch (Exception e) {
            return "";
        }
    }
    public static String getUUID(Context context) {
        if (context == null)
            return null;
        String phoneDeviceId = getAndroidID(context);//getIMEI(context);
        int deviceIdLength = 0;
        if (phoneDeviceId != null) {
            deviceIdLength = phoneDeviceId.length();
        }
        StringBuilder builder = new StringBuilder();
        if (deviceIdLength == 0) {
            for (int i = 0; i < UUID_LENGTH; i++) {
                builder.append('0');
            }
        } else {
            builder.append(phoneDeviceId);
        }
        return builder.toString();
    }

    public static String getMCC(Context context) {
        if (context == null)
            return "";
        String mcc_mnc = getSimOperator(context);
        StringBuilder mcc = null;
        if (null != mcc_mnc && mcc_mnc.length() >= 3) {
            mcc = new StringBuilder();
            mcc.append(mcc_mnc, 0, 3);
            return mcc.toString();
        }
        return "";
    }

    public static String getMNC(Context context) {
        if (context == null)
            return "";
        String mcc_mnc = getSimOperator(context);
        StringBuilder mnc = null;
        if (null != mcc_mnc && mcc_mnc.length() >= 5) {
            mnc = new StringBuilder();
            mnc.append(mcc_mnc, 3, 5);
            return mnc.toString();
        }
        return "";
    }

    private static String getSimOperator(Context context) {
        String mcc_mnc = null;
        try {
            final TelephonyManager tm = (TelephonyManager)context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            mcc_mnc = tm.getSimOperator();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mcc_mnc;
    }

}
