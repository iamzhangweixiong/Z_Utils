package com.zhangwx.z_utils.Z_Utils;

/**
 * Created by zhangwx on 2016/9/5.
 */

import android.os.Build;
import android.util.Log;

/**
 * 机型工具类
 *
 * @author zhangwx
 *
 */
public class MachineUtils {
    private static final String MODEL = Build.MODEL.toLowerCase();
    private static final String MANUFACTURER = Build.MANUFACTURER.toLowerCase();

    public static boolean isGTP1000() {
        return MODEL.equalsIgnoreCase("gt-p1000");
    }

    public static boolean isZTEU985() {
        return MANUFACTURER.equals("zte") && MODEL.contains("zte u985");
    }

    public static boolean isGts5830() {
        return MODEL.equalsIgnoreCase("gt-s5830");
    }

    public static boolean isGts5838() {
        return MODEL.equalsIgnoreCase("gt-s5838");
    }

    public static boolean isGtS5830i() {
        return MODEL.equalsIgnoreCase("gt-s5830i");
    }

    public static boolean isG11() {
        return MODEL.equals("HTC Incredible S");
    }

    public static boolean isI9000() {
        return MODEL.equalsIgnoreCase("GT-I9000");
    }

    public static boolean isNexus5() {
        return MODEL.equalsIgnoreCase("Nexus 5");
    }

    public static boolean isNexus5X() {
        return MODEL.equalsIgnoreCase("Nexus 5x");
    }

    public static boolean isSamsung() {
        return MANUFACTURER.contains("samsung");
    }

    /* model = E6    ;    brand  == GiONEE*/
    public static boolean isGioneeE6(){
        if(MODEL.equalsIgnoreCase("E6") && "GiONEE".equalsIgnoreCase(Build.BRAND)){
            return true;
        }
        return false;
    }
    public static boolean isGioneeX817(){
        if(MODEL.equalsIgnoreCase("X817") && "GiONEE".equalsIgnoreCase(Build.BRAND)){
            return true;
        }
        return false;
    }

    public static boolean isVivoX1St(){
        if(MODEL.equalsIgnoreCase("vivo X1St") && "vivo".equalsIgnoreCase(Build.BRAND)){
            return true;
        }
        return false;
    }

    public static boolean isVenus_V3_5040() {
        return MODEL.equalsIgnoreCase("Venus_V3_5040") && "Vestel".equalsIgnoreCase(Build.BRAND);
    }

    public static boolean isOneplus_A0001() {
        return MODEL.equalsIgnoreCase("A0001") && "oneplus".equalsIgnoreCase(Build.BRAND);
    }

    public static boolean isVivoNoAccessibility() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1 && "vivo".equalsIgnoreCase(Build.BRAND)) {
            return true;
        }
        return false;
    }

    private static Boolean sIsNote3;
    public static boolean isNote3() {
        if (sIsNote3 == null) {
            sIsNote3 = MODEL.matches("(?i)(SM-N900|SM-N750).*");
        }
        return sIsNote3;
    }

    public static boolean isHTC() {
        return Build.BRAND.toLowerCase().contains("htc");
    }

    public static boolean isHUAWEI() {
        return  Build.BRAND.toLowerCase().contains("huawei")  ||  Build.BRAND.toLowerCase().contains("honor");
    }

    public static boolean isMemAbove2G() {
        int total = (int)(ProcessUtils.getTotalMem() / 1024f / 1024f + 0.3f);
        Log.e("zhangweixiong", "MachineUtils isMemAbove2G : total is : " + total);
        return total >= 2;
    }
}
