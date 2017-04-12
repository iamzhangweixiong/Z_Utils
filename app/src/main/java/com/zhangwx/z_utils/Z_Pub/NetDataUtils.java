package com.zhangwx.z_utils.Z_Pub;

import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;

public class NetDataUtils {

    public static long caculateCRC32(byte[] datas) {
        CRC32 crc = new CRC32();
        crc.update(datas);
        return crc.getValue();
    }

    public static String getResponseString(byte[] stringBytes, String charset) {
        try {
            return stringBytes == null ? null : new String(stringBytes, charset);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
