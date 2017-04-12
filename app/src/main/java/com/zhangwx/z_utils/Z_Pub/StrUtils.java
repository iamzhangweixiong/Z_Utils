package com.zhangwx.z_utils.Z_Pub;

import android.text.TextUtils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class StrUtils {
	
	public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }  
        return stringBuilder.toString();  
    } 
	
	public static long[] parseStringsToLongs(String[] ids) {
        if (ids == null) {
            return null;
        }
        long[] result = new long[ids.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = -1;
            try {
                result[i] = Long.parseLong(ids[i]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
	
	public static long[] parseStringsToLongs(List<String> ids) {
        if (ids == null) {
            return null;
        }
        int size = ids.size();
        long[] result = new long[size];
        for (int i = 0; i < size; i++) {
            try {
                result[i] = Long.parseLong(ids.get(i));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                result[i] = -1;
            }
        }
        return result;
    }

    public static List<Long> strListToLongList(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<Long>();
        }
        List<Long> result = new ArrayList<Long>();
        for (String str : list) {
            try {
                long num = Long.parseLong(str);
                result.add(num);
            } catch (NumberFormatException ignore) {
            }
        }
        return result;
    }

    /**
     * 判断是否是一个IP
     */
    public static boolean isIPAddr(String IP) {
        if (TextUtils.isEmpty(IP)) return false;
        boolean result = false;
        IP = IP.trim();
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = IP.split("\\.");
            if (Integer.parseInt(s[0]) < 255)
                if (Integer.parseInt(s[1]) < 255)
                    if (Integer.parseInt(s[2]) < 255)
                        if (Integer.parseInt(s[3]) < 255)
                            result = true;
        }
        return result;
    }

    /**
     * Lower case.
     *
     * @param bytes
     * @return
     */
    public static String toHexString(final byte[] bytes) {
        if (bytes == null || bytes.length < 1) {
            return null;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(null == md){
            return null;
        }
        md.update(bytes);
        byte[] tmp = md.digest();
        if(null == tmp || tmp.length <= 0){
            return null;
        }
        StringBuilder sb = new StringBuilder(tmp.length * 2);
        int v = 0;
        String hexV = null;
        for (byte b : tmp) {
            v = b & 0xFF;
            // 0123456789abcdef
            hexV = Integer.toHexString(v);
            if (hexV.length() < 2) {
                sb.append("0");
            }
            sb.append(hexV);
        }
        return sb.toString();
    }

}
