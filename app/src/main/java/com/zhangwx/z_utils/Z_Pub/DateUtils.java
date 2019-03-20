package com.zhangwx.z_utils.Z_Pub;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    /**
     * 前N天的时间
     */
    public static long getDayBeforeCurrent(int distanceDay) {
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.set(Calendar.DAY_OF_YEAR, nowCalendar.get(Calendar.DAY_OF_YEAR) - distanceDay);
        return nowCalendar.getTimeInMillis();
    }

    public static long getMinuteBeforeCurrent(int distanceMinute) {
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.set(Calendar.MINUTE, nowCalendar.get(Calendar.MINUTE) - distanceMinute);
        return nowCalendar.getTimeInMillis();
    }

    public static String getCurrentTime(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(new Date(date));
        return str;
    }
}
