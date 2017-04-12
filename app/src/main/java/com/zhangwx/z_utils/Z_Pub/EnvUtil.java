package com.zhangwx.z_utils.Z_Pub;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.zhangwx.z_utils.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 环境相关工具类
 *
 *  @author zhangwx
 */
public class EnvUtil {
    private static String sProcessName;

    public static String getPkgName() {
        Context context = MyApplication.getContext();
        if (context != null) {
            ComponentName cn = new ComponentName(context, context.getClass());
            return cn.getPackageName();
        } else {
            return null;
        }
    }

    public static String getFilesDirAbsolutePath() {
        Context context = MyApplication.getContext();
        return getFilesDirAbsolutePath(context);
    }

    public static String getFilesDirAbsolutePath(Context context) {
        if (context != null && context.getFilesDir() != null) {
            return context.getFilesDir().getAbsolutePath();
        }
        String pkg = getPkgName();
        if (TextUtils.isEmpty(pkg))
            return null;
        return File.separator + "data" + File.separator + "data" + File.separator + pkg + File.separator + "files";
    }

    public static String getSharedPrefsAbsolutePath(Context context) {
        String pkg = getPkgName();
        if (TextUtils.isEmpty(pkg))
            return null;
        return File.separator + "data" + File.separator + "data" + File.separator + pkg + File.separator + "shared_prefs";
    }

    public static int getVersionCode(Context context) {
        ComponentName cn = new ComponentName(context, context.getClass());
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    cn.getPackageName(), 0);
            return info.versionCode;
        } catch (/* NameNotFoundException */Exception e) {
            return -1;
        }
    }

    public static boolean hasPackage(Context ctx, String packageName) {
        if (null == ctx || null == packageName)
            return false;

        boolean bHas = true;
        try {
            ctx.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_GIDS);
        } catch (Exception e) {
            // 抛出找不到的异常 ，说明该程序已经被卸载
            bHas = false;
        }
        return bHas;
    }

    public static String getProcessName(Context base){
        File cmdFile = new File("/proc/self/cmdline");

        if ( cmdFile.exists() && !cmdFile.isDirectory() ){
            BufferedReader reader = null;
            try{
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(cmdFile)));
                String procName = reader.readLine();

                if (!TextUtils.isEmpty(procName) )
                    return procName.trim();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(reader != null){
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            //try to fix SELinux limit due to unable access /proc file system
            String processName = getProcessNameByActivityService(base);
            if (processName != null) {
                return processName;
            }
        }

        //Warnning: getApplicationInfo().processName only return package name for some reason, you will not see
        // the real process name, such as com.cleanmaster.mguard:service
        return base.getApplicationInfo().processName;
    }

    public static String getProcessName() {
        if (null == sProcessName) {
            try {
                sProcessName = getProcessNameByActivityService(MyApplication.getContext());
            } catch (Exception ignored) {
            }
        }

        return sProcessName;
    }

    private static String getProcessNameByActivityService(Context base) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager)base.getSystemService(Context.ACTIVITY_SERVICE);
        if (null == am) {
            return null;
        }
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = am.getRunningAppProcesses();
        if (null == appProcessInfoList) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo i : appProcessInfoList) {
            if (i.pid == pid) {
                if (i.processName != null) {
                    return i.processName.trim();
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int getDPI(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static boolean isX86CPU() {
        return Build.CPU_ABI.equals("x86");
    }

    public static String getExternalFilesDirPath() {
        File apkCacheFile = null;
        try{
            apkCacheFile = MyApplication.getContext().getExternalFilesDir(null);
        } catch (NullPointerException e){
        } catch (SecurityException e) {
            //fix http://trace.cm.ijinshan.com/index/dump?version=&date=20140708&thever=0&dumpkey=2769283760&field=%E6%97%A0&field_content=2769283760
        } catch (RuntimeException e) {
            //fix http://trace-abord.cm.ijinshan.com/index/dump?thever=53&field=%E6%97%A0&field_content=&date=20151209&version=4701366&dumpkey=2029026254
        }

        String apkCacheDir = null;
        if (apkCacheFile != null && apkCacheFile.exists()) {
            apkCacheDir = apkCacheFile.getAbsolutePath();
        }

        if (apkCacheDir == null){
            File externalFile = getExternalStorageDirectory();
            if (externalFile != null) {
                apkCacheDir = new File(externalFile, "Android/data/" + getPkgName() + "/files").getAbsolutePath();
            }
        }
        try {
            new File(apkCacheDir + "/").mkdirs(); // 如果没有路径，创建之
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return apkCacheDir;
    }

    public static boolean isSdcardMounted() {
        return Environment.MEDIA_MOUNTED.equals(getExternalStorageState());
    }

    public static String getExternalStorageState() {
        String state = null;
        try {
            state = Environment.getExternalStorageState();
        } catch (Exception e) {
            e.printStackTrace();
            state = "";
        }
        return state;
    }

    public static File getExternalStorageDirectory() {
        File file = null;
        try {
            file = Environment.getExternalStorageDirectory();
        } catch (Exception e) {
            e.printStackTrace();
            file = new File("/sdcard");
        }
        return file;
    }

    public static class VERSION_CODES {
        public static final int LOLLIPOP = 21;
    }
}
