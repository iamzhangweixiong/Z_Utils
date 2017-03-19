package com.zhangwx.z_utils.Z_Pub;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.util.Log;
import android.view.WindowManagerGlobal;

import java.lang.reflect.Method;

/**
 * Created by zhangwx on 2016/9/5.
 */
public class MemoryUtils {
    private static final String TAG = MemoryUtils.class.getSimpleName();
    public static long getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    public static String getDetailMemoryInfo(Context context) {
        String msg = "";
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        int[] pid = new int[]{android.os.Process.myPid()};
        Debug.MemoryInfo[] info = am.getProcessMemoryInfo(pid);
        if (info != null && info.length > 0) {
            msg = "totalPrivateDirty:" + info[0].getTotalPrivateDirty() + "kB dalvikPrivateDirty:" + info[0].dalvikPrivateDirty + "kB availMem:" + mi.availMem / 1024 + "kB";
        }
        return msg;
    }

    public static void trimMemory() {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
//				WindowManagerGlobal.getInstance().trimMemory(80);
                Method trimMethod = WindowManagerGlobal.class.getDeclaredMethod("trimMemory", int.class);
                if (trimMethod != null) {
                    trimMethod.setAccessible(true);
                    trimMethod.invoke(WindowManagerGlobal.getInstance(), 80);
                }
                //OpLog.toFile(TAG, "WindowManagerGlobal trimMemory invoked");
                //Log.e("russell", "WindowManagerGlobal trimMemory invoked");
            } else if (Build.VERSION.SDK_INT >= 17) {
                Method trimMethod = WindowManagerGlobal.class.getDeclaredMethod("startTrimMemory", int.class);
                Method endTrimMethod = WindowManagerGlobal.class.getDeclaredMethod("endTrimMemory");
                if (trimMethod != null && endTrimMethod != null) {
                    trimMethod.setAccessible(true);
                    trimMethod.invoke(WindowManagerGlobal.getInstance(), 80);
                    endTrimMethod.invoke(WindowManagerGlobal.getInstance());
                    //OpLog.toFile(TAG, "HardwareRenderer trimMemory invoked");
                    //Log.e("russell", "HardwareRenderer trimMemory invoked");
                }
            }
        } catch(Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("caught " + e.toString());
            for (StackTraceElement ele : e.getStackTrace()) {
                sb.append(ele.toString() + "\n");
            }
            sb.append("=====================================");
            Log.e(TAG, sb.toString());
        } catch (Error e) {
            StringBuilder sb = new StringBuilder();
            sb.append("caught " + e.toString());
            for (StackTraceElement ele : e.getStackTrace()) {
                sb.append(ele.toString() + "\n");
            }
            sb.append("=====================================");
            Log.e(TAG, sb.toString());
        }
    }
}
