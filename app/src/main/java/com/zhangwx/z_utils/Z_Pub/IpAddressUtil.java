package com.zhangwx.z_utils.Z_Pub;

import android.text.TextUtils;

import com.zhangwx.z_utils.MyApplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;


/**
 * Created by zhangwx on 2016/9/5.
 */
public class IpAddressUtil {

//	private static final String TAG = IpAddressUtil.class.getSimpleName();
    /**
     * Google DNS 8.8.8.8
     */
    public static final String GOOGLE_DNS1 = "8.8.8.8";
    /**
     * Google DNS 8.8.4.4
     */
    public static final String GOOGLE_DNS2 = "8.8.4.4";

    /**
     * ping
     *
     * @return true or false
     */
    public static boolean ping(String ip) {
        boolean result = false;
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 -w 5 " + ip);// ping3次
            // PING的状态
            int status = p.waitFor();
            if (status == 0) {
                result = true;
            }
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
        return result;

    }

    /**
     * 判断是否能ping通Google DNS(8.8.8.8 or 8.8.4.4)
     *
     * @return true or false
     */
    public static boolean pingGoogleDNS() {
        if (NetworkTypeUtil.getNetworkType(MyApplication.getContext()) == NetworkTypeUtil.TYPE_NO_NETWORK) {
            return false;
        }
        boolean result1 = IpAddressUtil.ping(IpAddressUtil.GOOGLE_DNS1);
        boolean result2 = true;
        if (!result1) {
            result2 = IpAddressUtil.ping(IpAddressUtil.GOOGLE_DNS2);
        }
        if (result1 || result2) {
            return true;
        }
        return false;
    }

    public static String getIPAddr(String url) {
        String result = "";
        if (TextUtils.isEmpty(url))
            return result;
        try {
            URL mUrl = new URL(url);
            String domain = mUrl.getHost();
            if (TextUtils.isEmpty(domain))
                return result;
            InetAddress inetAddress = InetAddress.getByName(domain);
            if (inetAddress != null) return inetAddress.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError error) {
        }
        return result;
    }

    public static String getHostByUrl(String url) {
        String result = "";
        if (TextUtils.isEmpty(url))
            return result;
        try {
            URL mUrl = new URL(url);
            return mUrl.getHost();
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 返回一个URL的 一级域名
     */
    public static String getOneLevelHostByUrl(String url) {
        String result = "";
        if (TextUtils.isEmpty(url))
            return result;
        String host = getHostByUrl(url);
        if (TextUtils.isEmpty(host))
            return result;
        String[] points = host.split(".");
        if (points == null || points.length == 0) {
            return result;
        }
        result = points[points.length] + "." + points[points.length - 1];
        return result;
    }
}
