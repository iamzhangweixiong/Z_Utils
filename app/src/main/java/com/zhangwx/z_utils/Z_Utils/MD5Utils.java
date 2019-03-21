package com.zhangwx.z_utils.Z_Utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by zhangwx on 2016/9/5.
 */
public class MD5Utils {
    public static String getFileMD5(File file) {

        if (!file.isFile()) {

            return null;

        }
        MessageDigest digest = null;
        FileInputStream in = null;

        byte buffer[] = new byte[1024];
        int len;

        try {

            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);

            while ((len = in.read(buffer, 0, 1024)) != -1) {

                digest.update(buffer, 0, len);
            }

            in.close();
        } catch (Exception e) {

            return null;
        }
        return encodeHex(digest.digest());
    }

    public static String getFileMD5Standard(File file) {

        if (!file.isFile()) {

            return null;

        }
        MessageDigest digest = null;
        FileInputStream in = null;

        byte buffer[] = new byte[1024];
        int len;

        try {

            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);

            while ((len = in.read(buffer, 0, 1024)) != -1) {

                digest.update(buffer, 0, len);
            }

            in.close();
        } catch (Exception e) {

            return null;
        }
        return encodeHexStandard(digest.digest());
    }

    public static String getPackageNameMd5(String packageName) {
        if (packageName == null) {
            return null;
        } else {
            return getStringMd5(packageName + "zhangwx");
        }
    }

    /***
     * 路径 md5
     *
     * @param filePath 为空代表SDCard根目录
     */

    public static String getFilePathMd5(String filePath) {

        // 去掉'/'，数据库路径加密不带最开始的'/'
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }

        if (TextUtils.isEmpty(filePath)) {
            return "";
        }

        String[] s = filePath.split("/");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            if (!TextUtils.isEmpty(s[i])) {
                sb.append(getStringMd5(s[i].toLowerCase() + "zhangwx"));
            }
            if (i != s.length - 1)
                sb.append('+');
        }
        return sb.toString();
    }


    public static String getStringMd5(String plainText) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
        } catch (Exception e) {
            return null;
        }
        return encodeHex(md.digest());

    }

    public static String getStreamMD5(InputStream in) {
        if (in == null) {
            return null;
        }
        MessageDigest md = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            md = MessageDigest.getInstance("MD5");
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {

            return null;
        }

        return encodeHex(md.digest());

    }


    public static String encodeHexStandard(byte[] data) {

        if (data == null) {

            return null;
        }

        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int val = ((int) data[i]) & 0xff;
            if (val < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(val));
        }
        return hex.toString();
    }

    public static String encodeHex(byte[] data) {

        if (data == null) {

            return null;
        }

        final String HEXES = "ABCDEF0123456789";
        int len = data.length;
        StringBuilder hex = new StringBuilder(len * 2);

        for(int i = 0; i < len; ++i) {

            hex.append(HEXES.charAt((data[i] & 0xF0) >>> 4));
            hex.append(HEXES.charAt((data[i] & 0x0F)));
        }

        return hex.toString();
    }
}
