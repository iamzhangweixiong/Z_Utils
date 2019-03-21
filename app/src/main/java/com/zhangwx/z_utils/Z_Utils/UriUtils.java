package com.zhangwx.z_utils.Z_Utils;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UriUtils {
    private static final String UTF_8 = "UTF-8";
    
    public static String encodeSafely(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        
        try {
            str = URLEncoder.encode(str, UTF_8);
        } catch (Exception ignore) {
        }
        
        return str;
        
    }
    
    public static String decodeSafely(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        
        try {
            str = URLDecoder.decode(str, UTF_8);
        } catch (Exception ignore) {
        }
        
        return str;
    }
    
    
    @SuppressWarnings("unused")
    public static boolean isValidURL(String urlStr) {
        if (TextUtils.isEmpty(urlStr)) {
            return false;
        }
        
        try {
            URL url = new URL(urlStr);
            String host = url.getHost();
            return !TextUtils.isEmpty(host);
        } catch (MalformedURLException e) {
            return false;
        }
    }

}
