package com.zhangwx.z_utils.Z_Pub;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by zhangwx on 2017/4/20.
 */

public class DebugUtils {

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
        try {
            File file = new File("/sdcard/androidID.txt");
            FileInputStream is = new FileInputStream(file);
            byte[] b = new byte[is.available()];
            is.read(b);
            result = new String(b);
            Log.e("zhang", "getAndroidID: = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
