package com.zhangwx.z_utils.Z_Pub;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.zhangwx.z_utils.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by zhangwx on 2016/9/5.
 */
public class BatteryStatusUtil {
    private static final boolean DEG = false;
    private static final String TAG = "BatteryStatusUtil";

    public static final int DEFAULT_LEVEL = 50;
    public static final int LOW_LEVEL = 20;
    public static final int DEFAULT_PLUGGED = 0;
    public static final int DEFAULT_STATUS = BatteryManager.BATTERY_STATUS_UNKNOWN;

    private static int sLastLevel = -1;
    private static int sLastPlugged = -1;
    private static int sLastStatus = -1;

    // 机型信息，用于获取容量
    static final String SAMSUNG_SCH_I909 = "SCH-i909";
    static final String SAMSUNG_SCH_W899 = "SCH-W899";
    static final String SAMSUNG_SCH_I535 = "SCH-I535";

    public static final String MI2_CAPACITY_FILE = "/sys/class/power_supply/battery/energy_full";
    public static  final String MI2_DEFAULT_CAPACITY_STR = "2000000000";
    public static final long MI2_DEFAULT_CAPACITY_NUMBER = 2000000000;

    /**
     * 更新最新一回拿到的电池相关信息，作为以后的备份
     * @param lastLevel lastLevel
     * @param lastPlugged lastPlugged
     * @param lastStatus lastStatus
     */
    public static void updateLastBatteryData(int lastLevel, int lastPlugged, int lastStatus) {
        sLastLevel = lastLevel;
        sLastPlugged = lastPlugged;
        sLastStatus = lastStatus;
        if (DEG) {
            Log.d(TAG, "lastLevel=" + lastLevel + "&lastPlugged=" + lastPlugged + "&lastStatus=" + lastStatus);
        }
    }

    /**
     * 更新最新一回拿到的手机电量百分比，作为以后取不到后的备份
     * @param lastLevel lastLevel
     */
    public static void updateLastBatteryLevel(int lastLevel) {
        sLastLevel = lastLevel;
    }

    /**
     * 更新最新一回拿到的手机插电情况，作为以后取不到后的备份
     * @param lastPlugged lastPlugged
     */
    public static void updateLastBatteryPlugged(int lastPlugged) {
        sLastPlugged = lastPlugged;
    }

    /**
     * 更新最新一回拿到的电池充电情况，作为以后取不到后的备份
     * @param lastStatus lastStatus
     */
    public static void updateLastBatteryStatus(int lastStatus) {
        sLastStatus = lastStatus;
    }

    /**
     * @return 当前电池信息的Intent
     */
    public static Intent getBatteryIntent() {
        Context context = MyApplication.getContext();
        if (context != null) {
            try {
                return context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            } catch (Exception e) {
                if (DEG) e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @return 容错后的当前手机电量百分比
     */
    public static int getBatteryLevel() {
        Context context = MyApplication.getContext();
        try {
            Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int[] result = getBatterLevel(intent);
            return result[0];
        } catch (Exception e) {
            if (DEG) e.printStackTrace();
            if (sLastLevel != -1) return sLastLevel;
            else return DEFAULT_LEVEL;
        }
    }

    public static int[] getBatterLevel(Intent intent) {
        int[] result = new int[2];
        if (intent != null) {
            result[0] = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, DEFAULT_LEVEL);
            result[1] = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, DEFAULT_PLUGGED);
            result[0] = formatBatteryLevel(result[0], result[1]);
        } else {
            result[0] = DEFAULT_LEVEL;
            result[1] = DEFAULT_PLUGGED;
        }
        return result;
    }

    /**
     * if battery level < 15, and not charging, return inLowBattery true
     * @return
     */
    public static boolean isInLowBattery() {
        Intent intent = BatteryStatusUtil.getBatteryIntent();
        int[] result = BatteryStatusUtil.getBatterLevel(intent);
        int percent = result[0];
        boolean charging = result[1] != 0;
        return !charging && percent <= 15;
    }

    /**
     * 三星I909电量是用3位计算的 有的I909也不是3位的; 加入摩托罗拉XT1080和三星SCH_I535的条件判断 added by
     * wangkunlun@conew.com
     */
    public static int formatBatteryLevel(int level, int plugged) {
        int fixedLevel = level;
        int chargeLevel = 0;
        int capacityLevel = 0;
        String model = Build.MODEL;
        if (model.equalsIgnoreCase(SAMSUNG_SCH_I909)
                || model.equalsIgnoreCase(SAMSUNG_SCH_I535) // 加入三星SCH_I535的判断
                || model.equalsIgnoreCase(SAMSUNG_SCH_W899)) {
            fixedLevel = level > 100 ? level / 10 : level;
        } else if (model.trim().toUpperCase().contains("XT702")) {
            fixedLevel = level;
        } else if (model.trim().toUpperCase().contains("XT907")) {
            fixedLevel = level;
        } else if (model.trim().toUpperCase().contains("XT1058")) {
            fixedLevel = level;
        } else if (model.trim().toUpperCase().contains("XT1080")) { // 加入摩托罗拉XT1080的判断
            fixedLevel = level;
        } else if (Build.MANUFACTURER.equalsIgnoreCase("motorola")) {
            File localFile = new File("/sys/class/power_supply/battery/charge_counter");
            if (localFile.exists()) {
                String strLevel = getStringFromFile(localFile);
                chargeLevel = Integer.parseInt(strLevel);
            }
            if (chargeLevel <= 0) {
                File localFile2 = new File("/sys/class/power_supply/battery/capacity");
                if (localFile2.exists()) {
                    String strLevel = getStringFromFile(localFile2);
                    capacityLevel = Integer.parseInt(strLevel);
                }
            }
            if (chargeLevel > 100 || chargeLevel <= 0) {
                fixedLevel = level;
            } else if (fixedLevel % 10 == 0) {
                fixedLevel = chargeLevel;
            }
        }
        if (fixedLevel > 100) {
            if (plugged != 0 && fixedLevel < 110) { // 在充电时且小于110
                return 100;
            } else { // 没有充电时或大于110以上则除10
                fixedLevel = fixedLevel / 10;
                while (fixedLevel > 100) {
                    fixedLevel = fixedLevel / 10;
                }
                return fixedLevel;
            }
        } else {
            return fixedLevel;
        }

    }

    /**
     * Power source is an AC charger. BATTERY_PLUGGED_AC = 1;
     * Power source is a USB port.    BATTERY_PLUGGED_USB = 2;
     * Power source is wireless.      BATTERY_PLUGGED_WIRELESS = 4;
     * @return 当前手机插电情况（没插电为0）
     */
    public static int getBatteryPlugged() {
        Context context = MyApplication.getContext();

        try{
            if (context != null) {
                Intent stickyIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                if (stickyIntent != null) {
                    return stickyIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, DEFAULT_PLUGGED);
                }
            } else {
                if (DEG) Log.e(TAG, "context == null !");
            }
        }catch(Exception ignored){ //trac #4036 unexpected exception

        }


        // context == null || stickyIntent == null
        if (sLastPlugged != -1) return sLastPlugged;
        else return DEFAULT_PLUGGED;
    }

    /**
     * @return 当前手机电池充电状况
     */
    public static int getBatteryStatus() {
        Context context = MyApplication.getContext();
        try {
            return context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                    .getIntExtra(BatteryManager.EXTRA_STATUS, DEFAULT_STATUS);
        }catch (Exception e){
            if (DEG) e.printStackTrace();
            if (sLastStatus != -1) return sLastStatus;
            else return DEFAULT_STATUS;
        }
    }

    /**
     * @return 当前手机电池的温度（摄氏度为单位）
     */
    public static float getBatteryTemperature() {
        float defaultTemp = 32f;

        Intent intent = getBatteryIntent();
        if (intent != null) {
            // intent拿到的温度是以0.1°C为单位的，所以要除以10
            float temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, (int) defaultTemp * 10) / 10f;
            // 如果能获取到温度，但是温度为0，则改为32摄氏度。酷派机型较多见
            temperature = temperature <= 0 ? defaultTemp : temperature;
            return temperature;
        }

        return defaultTemp;
    }

    /** 摄氏度转华氏度 */
    public static float centigrade2Fahrenheit(float centigrade) {
        return (((9f / 5f) * centigrade) + 32);
    }

    /**
     *
     * @return String type, unit: 00.00V
     */
    public static String getBatteryVoltage() {
        Intent intent = getBatteryIntent();
        if (null != intent) {
            int voltageMillis = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 4000);
            return formatVoltage(voltageMillis);
        }

        return "N/A";
    }

    public static int formatBatteryMaxLevel(int level) {
        if (Build.MODEL.equals(SAMSUNG_SCH_I909)) {
            return level > 100 ? level / 10 : level;
        }
        return level;
    }

    /**
     *
     * @return
     */
    public static String getBatteryTechnology() {
        Intent intent = getBatteryIntent();
        if (null != intent) {
            return intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        }

        return "Li-ion";
    }

    /**
     *
     * @param voltage
     * @return
     */
    public static String formatVoltage(int voltage) {
        String tempValue;

        //这里这么转换可能是有原因的，从BatteryInfoActivity中抄出来的
        if (voltage > 1000 * 1000) {
            tempValue = String.format("%.1f V", (voltage / 1000000f));
        } else if (voltage > 1000) {
            tempValue = String.format("%.1f V", (voltage / 1000f));
        } else {
            tempValue = voltage + "V";
        }

        return tempValue;
    }

    private static String getStringFromFile(File file) {
        StringBuilder sb = new StringBuilder();
        FileInputStream in = null;
        InputStreamReader ir = null;
        char[] buf = new char[1024];
        int length;
        try {
            in = new FileInputStream(file);
            ir = new InputStreamReader(in);
            while ((length = ir.read(buf)) >= 0) {
                addChar(length, buf, sb);
            }

        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        String s = sb.toString();
        if (TextUtils.isEmpty(s)){
            s = "0";
        }
        return s;
    }

    private static void addChar(int length, char[] buf, StringBuilder sb) {

        for (int i = 0; i < length; i++) {
            if (buf[i] != '\n' && buf[i] != '\r') {
                sb.append(buf[i]);
            }
        }

    }


    /**
     * 用反射的方式，调用系统API获取电池容量
     * @param context
     * @return
     */
    public static int getCapacity(Context context) {
        int capacity = 0;

        if (context == null) return capacity;

        Log.d("stephli", "Model: " + Build.MODEL);

//      因厂商差异，不能使用此方法获取电池容量。请不要再次使用此方法
//		final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        if (capacity <= 0) { //旧的方式获取电池容量，从累积的xml里面获取数据，以防万一
            capacity = getBatteryCapacityOldWay(context);
        }

        return capacity;
    }

    private static int getBatteryChargeFullDesign() {
        String path = "/sys/class/power_supply/battery/charge_full_design";
        File localFiletest = new File(path);
        if (localFiletest.exists()) {
            String strLevel = getStringFromFile(localFiletest);
            int level = Integer.parseInt(strLevel);
            return level;
        }
        return -1;
    }


    /*
	 * 因为xt910 maxx 和xt910从系统读出来的机型都是xt910，所以使用下面的方法来判断机型 对xt912也适用
	 */
    private static int getXT910OrXT910MAXXCapcity() {
        int chargefulldesign = getBatteryChargeFullDesign();
        if (chargefulldesign >= 3000) {
            return 3300; // for xt910 maxx
        } else {
            return 1780; // for xt910
        }
    }

    /**
     * 从文件中读取电池容量
     *
     * @param context
     * @return 电池容量 mah，若匹配不上，返回为0，默认值自行处理
     */
    private static int getBatteryCapacityOldWay(Context context) {
        int capacity = 0;

        if ("XT910".equalsIgnoreCase(Build.MODEL) || "XT912".equalsIgnoreCase(Build.MODEL)) {
            capacity = getXT910OrXT910MAXXCapcity();
        } else if (Build.MODEL.startsWith("MI 2")) {
            BufferedReader reader = null;
            String resultStr = MI2_DEFAULT_CAPACITY_STR;
            long resultNumber = MI2_DEFAULT_CAPACITY_NUMBER;
            try {
                reader = new BufferedReader(new FileReader(MI2_CAPACITY_FILE));
                resultStr = reader.readLine().trim();
                resultNumber = Long.valueOf(resultStr);
            } catch (Exception e) {
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {
                }
            }

            if (resultNumber > 0 && resultNumber <= MI2_DEFAULT_CAPACITY_NUMBER) {
                capacity = 2000; // 米2 薄电
            } else {
                capacity = 3300; // 米2 厚电
            }
        } else if (Build.MODEL.startsWith("MI 3")) {
            capacity = 3050; //http://www.mi.com/hk/mi3/#param
        } else if (Build.MODEL.startsWith("MI 4")) {
            capacity = 3080; //http://www.mi.com/mi4/params/ - 这个就不读那个文件了，觉得意义不大，而且数值跟官网说明的有误差
        } else if (Build.MODEL.startsWith("MI NOTE")) {
            capacity = 3000; //http://www.mi.com/minote/specs/ - 3000mAh
        } else {

        }
        return capacity;
    }
}
