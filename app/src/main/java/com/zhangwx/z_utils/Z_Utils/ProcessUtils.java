package com.zhangwx.z_utils.Z_Utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;

//import com.android.internal.util.MemInfoReader;
import com.zhangwx.z_utils.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangwx on 2016/9/5.
 */
public class ProcessUtils {

    public static String getProcessName(Context context) {
        // Try first way: read from /proc/self/cmdline, FORBIDDEN in Android 5.x
        BufferedReader reader = null;
        try {
            final File cmdFile = new File("/proc/self/cmdline");
            if (!cmdFile.isDirectory()) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(cmdFile)));
                String procName = reader.readLine().trim();
                if (!TextUtils.isEmpty(procName)) {
                    Log.d("Application", "get process=" + procName);
                    return procName;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // Try second way: read by getRunningAppProcesses()
        try {
            final int myPid = android.os.Process.myPid();
            final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == myPid) {
                    return procInfo.processName;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // Try default way: it seems all processes has a SAME process name
        return context.getApplicationInfo().processName;
    }

    private static boolean mDebug = false;


    private static void doAvailMemExcludeCM(ActivityManager.MemoryInfo memoryInfo) {

        int[] pids = new int[]{android.os.Process.myPid()};

        long[] pss = getDetailMem(pids);
        if (pss != null && pss.length == 3) {
            memoryInfo.availMem += pss[2];
        }
    }

    /**
     * 单位是kb。</br>
     * <p>
     * TotalMem可能返回0，会引起Bug，所以先修改为返回1，防止除法为0异常
     */
    public static long getTotalMem() {
        if (sTotalMemOfKB > 1) {
            return sTotalMemOfKB;
        }

        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String strResult;
        int initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            strResult = str2.substring(str2.indexOf(":") + 1, str2.indexOf("kB")).trim();
            if (TextUtils.isEmpty(strResult)) {
                return 1;
            }
            if (!TextUtils.isDigitsOnly(strResult)) {
                return 1;
            }
            initial_memory = Integer.parseInt(strResult);
            localBufferedReader.close();
            sTotalMemOfKB = initial_memory;
            return initial_memory;
        } catch (Exception e) {

        }
        return 1;
    }

    /**
     * 获得可用内存大小，不再调用AMS，单位是字节
     *
     * @return
     */
//    public static int getAvailMem() {
//        MemInfoReader reader = new MemInfoReader();
//        reader.readMemInfo();
//
//        return (int) (reader.getFreeSize() + reader.getCachedSize());
//    }

    private static long sTotalMemOfKB = -1;

//    public static int getUsedMemoryPercentage() {
//        long totalMemoryKB = getTotalMem();
//        if (totalMemoryKB <= 0)
//            totalMemoryKB = 1024 * 1024 * 1024; // default memory is 1GB
//
//        long availMemKB = getAvailMem() / 1024;
//        long usedMemKB = (totalMemoryKB - availMemKB);
//
//        int percentage = (int) (usedMemKB * 100 / totalMemoryKB);
//        return percentage;
//    }


    //把 所有 关联的PID 的内存值加起来
    public static long getProcessMemory(ActivityManager am, ArrayList<Integer> pids) {

        if (pids == null || pids.size() == 0) {
            return 0;
        }

        int pidCount = pids.size();
        int[] ipids = new int[pidCount];
        long memory = 0;

        for (int i = 0; i < pidCount; i++) {
            ipids[i] = pids.get(i);
        }

        try {
            Debug.MemoryInfo[] memoryInfoArray = getMemoryInfosByPids(am, ipids);
            if (memoryInfoArray != null && memoryInfoArray.length > 0) {
                for (int i = 0; i < memoryInfoArray.length; i++) {
                    memory += getTotalPssMemory(memoryInfoArray[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memory * 1024;
    }


    /**
     * 反射获得占用内存大小
     */
    private static int getTotalPssMemory(Debug.MemoryInfo mi) {
        try {
            if (sMethodGetTotalPss == null) {
                sMethodGetTotalPss = mi.getClass().getMethod("getTotalPss");
            }
            return (Integer) sMethodGetTotalPss.invoke(mi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static Method sMethodGetTotalPss = null;

    /**
     * 反射获得MemoryInfo
     */
    private static Debug.MemoryInfo[] getMemoryInfosByPids(ActivityManager am, int[] pids) {

        try {
            if (sMethodGetProcessMemoryInfo == null) {
                sMethodGetProcessMemoryInfo = ActivityManager.class.getMethod("getProcessMemoryInfo", int[].class);
            }
            return (Debug.MemoryInfo[]) sMethodGetProcessMemoryInfo.invoke(am, pids);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static long[] getDetailMem(int[] pids) {
        if (null == pids || pids.length <= 0)
            return null;
        long[] detailMem = new long[3];
        Context context = MyApplication.getContext()
                .getApplicationContext();
        ActivityManager activityMgr = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        Debug.MemoryInfo[] mems = getMemoryInfosByPids(activityMgr, pids);
        for (Debug.MemoryInfo info : mems) {
            detailMem[0] += info.nativePss;
            detailMem[1] += info.dalvikPss;
            detailMem[2] += info.getTotalPss();
        }
        return detailMem;
    }

    private static Method sMethodGetProcessMemoryInfo = null;

    public static class RecentAppInfo {
        Intent intent;
        int index;

        public Intent getIntent() {
            return intent;
        }

        public int getIndex() {
            return index;
        }
    }

    private static final int MAX_RECENT_TASKS = 16;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static HashMap<String, RecentAppInfo> startGetRecentApps() {
        Context mContext = MyApplication.getContext();
        final PackageManager pm = mContext.getPackageManager();
        final ActivityManager am = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);

        int flags = 0;
        if (Build.VERSION.SDK_INT >= 11) {
            flags = ActivityManager.RECENT_IGNORE_UNAVAILABLE;
        }
        final List<ActivityManager.RecentTaskInfo> recentTasks = am.getRecentTasks(
                MAX_RECENT_TASKS, flags);

        HashMap<String, RecentAppInfo> map = new HashMap<String, RecentAppInfo>();
        ActivityInfo homeInfo = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
                .resolveActivityInfo(pm, 0);
        for (int i = 0; i < recentTasks.size(); ++i) {
            final ActivityManager.RecentTaskInfo info = recentTasks.get(i);

            Intent intent = new Intent(info.baseIntent);
            if (info.origActivity != null) {
                intent.setComponent(info.origActivity);
            }

            // Skip the current home activity.
            if (homeInfo != null) {
                if (homeInfo.packageName.equals(intent.getComponent().getPackageName())
                        && homeInfo.name.equals(intent.getComponent().getClassName())) {
                    continue;
                }
            }

            intent.setFlags((intent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            final ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
            if (resolveInfo != null) {
                final ActivityInfo activityInfo = resolveInfo.activityInfo;
                String title = null;
                if (mDebug) {
                    title = activityInfo.loadLabel(pm).toString();
                    Log.d("show", "title = " + title);
                }
                // boolean available = isIntentAvailable(pm, intent);
                // if (available) {
                if (mDebug) {
                    Log.d("show", "available = " + title);
                }
                RecentAppInfo tag = new RecentAppInfo();
                tag.intent = intent;
                tag.index = i;
                map.put(activityInfo.packageName, tag);
                // }
            }
        }
        return map;
    }

    public static boolean isIntentAvailable(PackageManager pm, Intent intent) {
        if (intent == null)
            return false;
        List<ResolveInfo> list = pm
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static Intent getLaunchIntentForPackage(String pkgname) {
        PackageManager pm = MyApplication.getContext().getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(pkgname);
        return intent;
    }

    public static boolean checkPermission(String permission) {
        if (permission == null)
            return false;
        Context mContext = MyApplication.getContext();
        int permissionType = PackageManager.PERMISSION_DENIED;
        try {
            permissionType = mContext.checkCallingOrSelfPermission(permission);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return permissionType != PackageManager.PERMISSION_DENIED;
    }

}
