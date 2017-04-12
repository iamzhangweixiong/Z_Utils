package com.zhangwx.z_utils.Z_Pub;

import android.database.Cursor;

import java.io.Closeable;
import java.net.Socket;
import java.util.zip.ZipFile;

/**
 * Created by zhangwx on 2016/9/5.
 */
public class CloseUtil {
    public static void close(Closeable close) {
        if (close == null) {
            return;
        }

        try {
            close.close();
        } catch (Exception e) {
            // do nothing
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    /**
     * 注意，以下方法是为了兼容4.0以下版本。因为4.0以下版本没有实现Closeable
     */
    ///////////////////////////////////////////////////////////////////////////////
    public static void close(Cursor close) {
        if (close == null) {
            return;
        }
        try {
            close.close();
        } catch (Exception e) {
            // do nothing
        }
    }

    public static void close(ZipFile close) {
        if (close == null) {
            return;
        }
        try {
            close.close();
        } catch (Exception e) {
            // do nothing
        }
    }

    public static void close(Socket close) {
        if (close == null) {
            return;
        }
        try {
            close.close();
        } catch (Exception e) {
            // do nothing
        }
    }

}
