package com.zhangwx.z_utils.Z_Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	/**
	 * 得到一个字符串的md5码
	 */
	public static String getStringMD5(String input) {
	    byte[] hash;

	    try {
	        hash = MessageDigest.getInstance("MD5").digest(input.getBytes("UTF-8"));
	    } catch (Exception e) {
            e.printStackTrace();
            return null;
	    } catch (StackOverflowError e) {
	        // fix crash
            e.printStackTrace();
            return null;
	    }

	    return encodeHex(hash);
    }

    public static String getFileMD5(File file) {
        try {
            return getFileMD5FailedWithException(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	public static String getFileMD5FailedWithException(File file) throws Exception {
		if (!file.isFile()) {
			return null;
		}
		byte[] hash;
        byte buffer[] = new byte[1024];
        int len;
        MessageDigest digest = null;
        FileInputStream in = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            hash = digest.digest();
        } finally {
            CloseUtil.close(in);
        }
        return encodeHex(hash);
	}

    public static String getFileMD5Only1024B(File file)  {
		if (file == null || !file.isFile()) {
			return null;
		}
        byte[] hash;
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            if ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            hash = digest.digest();
        } catch (Exception e) {
            return null;
        } finally {
            CloseUtil.close(in);
        }

        return encodeHex(hash);
    }

    public static String getStreamMD5(InputStream inputStream) {
        if (null == inputStream)
            return null;
        byte[] hash;
        byte[] bytes = new byte[1024];
        int len = 0;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            while ((len = inputStream.read(bytes)) > 0) {
                digest.update(bytes, 0, len);
            }
            inputStream.reset();
            hash = digest.digest();
        } catch (Exception e) {
            return null;
        }

        return encodeHex(hash);
    }
    
    public static String encodeHex(byte[] hash) {
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * Computes the MD5 hash of the given data and returns it as an array of
     * bytes.
     */
    public static byte[] computeMD5Hash(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            // should never get here
            throw new IllegalStateException(e);
        }
    }

    public static String toMd5_32(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input);
            byte[] bytes = md.digest();
            StringBuffer buf = new StringBuffer();
            for (byte b : bytes) {
                int i = b;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取context所在应用签名的md5值
     * @param context
     * @return
     */
    @SuppressLint("PackageManagerGetSignatures")
    public static String getAssignMd5(Context context){
        if(context == null) {
            return "";
        }
        PackageManager pm = context.getPackageManager();
        if(pm == null){
            return "";
        }
        try {
            PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (int j = 0; j < pInfo.signatures.length; j++) {
                byte[] signatures = pInfo.signatures[j].toByteArray();
                if (signatures != null) {
                    return StrUtils.toHexString(signatures);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (Exception e) {
        }
        return "";
    }

}
