package com.zhangwx.z_utils.Z_Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;


public class NetworkTypeUtil {

    public static final String TAG = "NetworkTypeUtil";
	public static final int TYPE_UNKNOWN = -1;// 未知
    public static final int TYPE_NO_NETWORK = 0;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_2G = 2;
    public static final int TYPE_3G = 3;
    public static final int TYPE_4G = 4;

    public static int getNetworkType(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return getNetworkType(info);
    }

    private static int getNetworkType(NetworkInfo info) {
        int type = TYPE_NO_NETWORK;
        if (info == null) {
            return type;
        }
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                type = TYPE_WIFI;
                break;
            case ConnectivityManager.TYPE_MOBILE:
                type = getMobileNetType(info.getSubtype());
                break;
            default:
                break;
        }
        return type;
    }

    private static int getMobileNetType(int subtype){
        switch (subtype) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return TYPE_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return TYPE_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return TYPE_4G;
        }
        return TYPE_NO_NETWORK;
    }

    public static boolean isMobileNetwork(int type) {
        switch (type) {
        case TYPE_2G:
        case TYPE_3G:
        case TYPE_4G:
            return true;
        }

        return false;
    }

    public static boolean isMobileNetwork(Context context) {
        int type = getNetworkType(context);
        return isMobileNetwork(type);
    }

    public static boolean isWifiNetworkAvailable(Context context) {
        return checkWifiNetworkAvailableByWifiManager(context) || checkWifiNetworkAvailableByNetState(context);
    }

    private static boolean checkWifiNetworkAvailableByWifiManager(Context context) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            if (wifiInfo == null) {
                Log.e(TAG, "checkWifiNetworkAvailableByWifiManager: isWifiNetworkAvailable wifi info is null");
                return false;
            }

            if (mWifiManager.isWifiEnabled()) {
                if (wifiInfo.getIpAddress() != 0) {
                    return true;
                }
            }
            Log.e(TAG, "checkWifiNetworkAvailableByWifiManager: "+"isWifiNetworkAvailable enable " + mWifiManager.isWifiEnabled() + ", " + wifiInfo.getIpAddress() );
        } catch (Exception e) {
           e.printStackTrace();
        }
        return false;
    }

    /**
     * 原来的方法通过WifiInfo.getIpAddress是否为0来判断wifi可用性。但实际上，有些机器有时会出现wifi可用，但getIpAddress为0的情况
     * 这样就会导致判断不准
     */
    private static boolean checkWifiNetworkAvailableByNetState(Context context) {
        // Monitor network connections (Wi-Fi, GPRS, UMTS, etc.)
        // mobile 3G Data Network
        ConnectivityManager conmgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conmgr == null) {
            Log.e(TAG,"checkWifiNetworkAvailableByNetState conn mgr is null");
            return false;
        }

        NetworkInfo info = null;
        try {
            info = conmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info == null) {
                Log.e(TAG, "checkWifiNetworkAvailableByNetState no wifi info");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        NetworkInfo.State wifi = info.getState(); // 显示wifi连接状态
        Log.e(TAG, "checkWifiNetworkAvailableByNetState " + wifi);
        if (wifi != null && (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)) {
            return true;
        }
        return false;
    }

    /**
     * 判断网络是否可用
     * 
     */
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return isNetworkAvailable(info);
    }

    private static boolean isNetworkAvailable(NetworkInfo info) {
        boolean result = false;
        if (info != null && info.isAvailable() && info.isConnected()) {
            result = true;
        }
        return result;
    }

    private static NetworkInfo getActiveNetworkInfo(Context context) {
        NetworkInfo info = null;
        try {
            ConnectivityManager connectMgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectMgr.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (NoSuchMethodError error) {
            error.printStackTrace();
        }
        return info;
    }
}
