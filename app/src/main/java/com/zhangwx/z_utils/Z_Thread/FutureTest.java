package com.zhangwx.z_utils.Z_Thread;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 *
 * Created by zhangwx on 2017/5/2.
 */

public class FutureTest {


    private static int getNumCores() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                return Pattern.matches("cpu[0-9]", pathname.getName());
            }
        }
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }

}
