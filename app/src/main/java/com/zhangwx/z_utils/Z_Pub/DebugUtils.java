package com.zhangwx.z_utils.Z_Pub;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by zhangwx on 2017/4/20.
 */

public class DebugUtils {

    public static final String TAG_SYNC = "SYNC";

//    public class TestHelper {
//        private boolean isTesting = false;
//        private static TestHelper testHelper = null;
//        private TestAdHelper() {
//            try {
//                File f = new File("/sdcard/_test_file_.txt");
//                if (f.exists()) {
//                    isTesting = true;
//                }
//            } catch (Exception e) {
//                isTesting = false;
//            }
//        }
//        public static TestHelper getInstance() {
//            if (testHelper == null) {
//                testHelper = new TestHelper();
//            }
//            return testHelper;
//        }
//        public boolean getIsTesting() {
//            return isTesting;
//        }
//    }

    public String getFileString() {
        String result = "";
        FileInputStream is = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/androidID.txt");
            is = new FileInputStream(file);
            byte[] b = new byte[is.available()];
            is.read(b);
            result = new String(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
