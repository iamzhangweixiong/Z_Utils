package com.zhangwx.z_utils.Z_Pub;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.zhangwx.z_utils.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class StorageList {
//	private Context mCtx = null;

    private Object mStorageManager = null;
    private Method mMethodGetVolumeList = null;
    private Method mMethodGetPaths = null;
    private Method mMethodGetVolumeState = null;

    private Pattern mFilterPattern00 = null;
    private Pattern mFilterPattern01 = null;
    private Pattern mFilterPattern02 = null;
    private Pattern mFilterPattern03 = null;
    private Pattern mFilterPattern04 = null;
    private Pattern mFilterPattern05 = null;
    private Pattern mFilterPattern06 = null;
    private Pattern mFilterPattern07 = null;
    private Pattern mFilterPattern08 = null;
    private Pattern mFilterPattern09 = null;

    @SuppressWarnings("ResourceType")
    @SuppressLint("WrongConstant")
    public StorageList() {
        Context mCtx = MyApplication.getContext();
        if (mCtx != null && Build.VERSION.SDK_INT >= 14) {
            mStorageManager = mCtx.getSystemService("storage");
            try {
                mMethodGetVolumeList = mStorageManager.getClass().getMethod(
                        "getVolumeList");
                mMethodGetPaths = mStorageManager.getClass().getMethod(
                        "getVolumePaths");
                mMethodGetVolumeState = mStorageManager.getClass().getMethod(
                        "getVolumeState", new Class[]{String.class});
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    /**
     * 取出已挂载的所有存储器路径
     */
    public ArrayList<String> getMountedVolumePaths() {
        if (Build.VERSION.SDK_INT < 14 || mMethodGetVolumeState == null) {
            return getDefualtMountedStoragePaths(null);
        }

        String[] volumePaths = getVolumePaths();
        if (volumePaths == null || volumePaths.length == 0) {
            return getDefualtMountedStoragePaths(null);
        }
        ArrayList<String> mountedVolumePaths = new ArrayList<String>();
        try {
            for (String volumePath : volumePaths) {
                if (volumePath == null)
                    continue;
                if (mMethodGetVolumeState.invoke(mStorageManager,
                        new Object[]{volumePath}).equals("mounted")) {
                    mountedVolumePaths.add(volumePath);
                }
            }
        } catch (Exception e) {
        }
        if (mountedVolumePaths.isEmpty()) {
            return getDefualtMountedStoragePaths(null);
        }
        return mountedVolumePaths;
    }

    /**
     * 取出已挂载的电话内部存储器路径
     */
    public ArrayList<String> getMountedPhoneVolumePaths() {
        return getMountedVolumePaths(false);
    }

    /**
     * 取出已挂载的外插SD卡存储器路径
     */
    public ArrayList<String> getMountedSdCardVolumePaths() {
        return getMountedVolumePaths(true);
    }

    private String[] getVolumePaths() {
        if (null == mMethodGetPaths || null == mStorageManager) {
            return null;
        }

        try {
            return (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 用最原始的方法获取已挂载的存储器路径
     */
    private ArrayList<String> getDefualtMountedStoragePaths() {
        ArrayList<String> result = new ArrayList<String>();

        if (EnvUtil.isSdcardMounted()) {
            result.add(EnvUtil.getExternalStorageDirectory().getPath());
        }

        return !result.isEmpty() ? result : null;
    }

    /**
     * 判断api level低于14的默认SD卡路径是否外插，如果api level低于9，则当作内置处理。
     */
    private boolean isDefaultMountedStorageRemovable() {
        if (Build.VERSION.SDK_INT < 9) {
            return false;
        }

        try {
            Method methodIsExternalStorageRemovable = Environment.class.getMethod("isExternalStorageRemovable");
            if (methodIsExternalStorageRemovable == null) {
                return false;
            }

            return ((Boolean) methodIsExternalStorageRemovable.invoke(Environment.class)).booleanValue();
        } catch (Exception e) {
            return false;
        }

    }


    /**
     * 用最原始的方法获取已挂载的存储器路径，并读取/proc/mounts文件中有可能是存储器的挂载点路径，叠加在result中返回。
     */
    private ArrayList<String> getDefualtMountedStoragePaths(ArrayList<String> result) {
        if (result == null) {
            result = new ArrayList<String>();
        }

        ArrayList<String> resultDefault = getDefualtMountedStoragePaths();
        if (resultDefault != null && !resultDefault.isEmpty()) {
            if (result.isEmpty()) {
                result = resultDefault;
            } else {
                result.addAll(resultDefault);
            }
        }

        ArrayList<String> resultFromMountsFile = getMountedStoragePathsFromMountsFile(result);
        if (resultFromMountsFile != null && !resultFromMountsFile.isEmpty()) {
            if (result.isEmpty()) {
                result = resultFromMountsFile;
            } else {
                result.addAll(resultFromMountsFile);
            }
        }
        return !result.isEmpty() ? result : null;
    }

    /**
     * 对于不低于2.3的系统，区别判断是否可移除的存储器叠加在result中返回。
     */
    private ArrayList<String> getDefualtMountedStoragePaths(ArrayList<String> result, boolean removable) {
        ArrayList<String> filterPaths = getDefualtMountedStoragePaths();
        if (removable == isDefaultMountedStorageRemovable()) {
            if (filterPaths != null && !filterPaths.isEmpty())
                result.addAll(filterPaths);
            if (result.isEmpty())
                result = null;
        }
        if (!removable)
            return result;
        if (filterPaths == null || filterPaths.isEmpty()) {// 从/proc/mounts中读取判为可能的存储设备，算做外部存储。要把我们可以正常取到的路径排除在外
            filterPaths = result;
        } else if (result != null && !result.isEmpty()) {
            for (int idx = 0; idx < result.size(); ++idx) {
                if (!filterPaths.contains(result.get(idx))) {
                    filterPaths.add(result.get(idx));
                }
            }
        }
        ArrayList<String> resultFromMountsFile = getMountedStoragePathsFromMountsFile(filterPaths);
        if (resultFromMountsFile != null && !resultFromMountsFile.isEmpty()) {
            if (result == null || result.isEmpty()) {
                result = resultFromMountsFile;
            } else {
                result.addAll(resultFromMountsFile);
            }
        }
        return result;
    }

    private ArrayList<String> getMountedVolumePaths(boolean removable) {
        ArrayList<String> result = new ArrayList<String>();
        if (Build.VERSION.SDK_INT < 14) {
            return getDefualtMountedStoragePaths(result, removable);
        } else {
            return getMountedStoragePathsForAboveAPI14(removable);
        }

    }
    public ArrayList<String> getMountedStoragePathsForAboveAPI14(boolean removable){
        ArrayList<String> result = new ArrayList<String>();
        if (mStorageManager == null || mMethodGetVolumeList == null || mMethodGetVolumeState == null) {
            return null;
        }
        try {
            Object[] arrayOfStorageVolume = (Object[]) mMethodGetVolumeList.invoke(mStorageManager);
            if (arrayOfStorageVolume == null || arrayOfStorageVolume.length == 0) {
                return null;
            }
            Method methodGetPath = arrayOfStorageVolume[0].getClass().getMethod("getPath");
            Method methodIsRemovable = arrayOfStorageVolume[0].getClass().getMethod("isRemovable");
            Method methodAllowMassStorage = arrayOfStorageVolume[0].getClass().getMethod("allowMassStorage");
            if (methodGetPath == null || methodIsRemovable == null)
                return null;
            String volumePath = null;
            for (Object element : arrayOfStorageVolume) {
                if (removable != ((Boolean) methodIsRemovable.invoke(element)).booleanValue())
                    continue;
                volumePath = (String) methodGetPath.invoke(element);
                if (mMethodGetVolumeState.invoke(mStorageManager, new Object[]{volumePath}).equals("mounted")) {
                    result.add(volumePath);
                } else if (removable && ((Boolean) methodAllowMassStorage.invoke(element)).booleanValue()) {
                    result.add(volumePath);
                }
            }
        } catch (Exception e) {
        }
        return !result.isEmpty() ? result : null;
    }

    /**
     * 从/proc/mounts中读取判断有可能是存储设备，且不在filterPaths当中的挂载路径。
     *
     * @param filterPaths 要过滤掉的地址
     */
    private ArrayList<String> getMountedStoragePathsFromMountsFile(ArrayList<String> filterPaths) {
        ArrayList<String> result = new ArrayList<String>();
        BufferedReader mountsFileReader = readPath("/proc/mounts");
        try {
            String lineInfo = null;
            while (mountsFileReader != null) {
                lineInfo = mountsFileReader.readLine();
                if (lineInfo == null) {
                    CloseUtil.close(mountsFileReader);
                    continue;
                }
                if (shouldBeFiltered(lineInfo))
                    continue;
                String[] arrayOfString = lineInfo.split(" ");
                if (arrayOfString.length >= 4) {
                    String pathName = arrayOfString[1];
                    if ((filterPaths == null || !filterPaths.contains(pathName)) && !result.contains(pathName)) {
                        result.add(pathName);
                    }
                }
            }
        } catch (Exception e) {
        }
        CloseUtil.close(mountsFileReader);
        return !result.isEmpty() ? result : null;
    }

    private BufferedReader readPath(String path){
        BufferedReader mountsFileReader;
        try {
            mountsFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
        } catch (FileNotFoundException e) {
            mountsFileReader = null;
        } catch (OutOfMemoryError e) {
            mountsFileReader = null;
        }
        return mountsFileReader;
    }

    private boolean shouldBeFiltered(String lineInfo) {
        if (lineInfo == null) {
            return true;
        }
        if (!initRegexPattern()) {
            return true;
        }
        if (matcher(lineInfo)) {
            return true;
        }
        return false;
    }
    private boolean initRegexPattern() {
        try {
            if (mFilterPattern00 == null)
                mFilterPattern00 = Pattern.compile("^\\/");
            if (mFilterPattern01 == null)
                mFilterPattern01 = Pattern.compile("\\s(vfat)|(fuse)\\s");
            if (mFilterPattern02 == null)
                mFilterPattern02 = Pattern.compile("\\brw\\b");
            if (mFilterPattern03 == null)
                mFilterPattern03 = Pattern.compile("\\bnoauto_da_alloc\\b");
            if (mFilterPattern04 == null)
                mFilterPattern04 = Pattern.compile("(\\basec)|(asec\\b)");
            if (mFilterPattern05 == null)
                mFilterPattern05 = Pattern.compile("\\buser_id=0\\b");
            if (mFilterPattern06 == null)
                mFilterPattern06 = Pattern.compile("\\bgroup_id=0\\b");
            if (mFilterPattern07 == null)
                mFilterPattern07 = Pattern.compile("\\buid=0\\b");
            if (mFilterPattern08 == null)
                mFilterPattern08 = Pattern.compile("\\bgid=0\\b");
            if (mFilterPattern09 == null)
                mFilterPattern09 = Pattern.compile("\\bbarrier=1\\b");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean matcher(String lineInfo){
        if (!mFilterPattern00.matcher(lineInfo).find())
            return true;
        if (!mFilterPattern01.matcher(lineInfo).find())
            return true;
        if (!mFilterPattern02.matcher(lineInfo).find())
            return true;
        if (mFilterPattern03.matcher(lineInfo).find())
            return true;
        if (mFilterPattern04.matcher(lineInfo).find())
            return true;
        if (mFilterPattern05.matcher(lineInfo).find())
            return true;
        if (mFilterPattern06.matcher(lineInfo).find())
            return true;
        if (mFilterPattern07.matcher(lineInfo).find())
            return true;
        if (mFilterPattern08.matcher(lineInfo).find())
            return true;
        if (mFilterPattern09.matcher(lineInfo).find()) {
            return true;
        }
        return false;
    }



    public String getExternalPath(boolean removable) {
        ArrayList<String> paths = getMountedVolumePaths(removable);
        for (int i = 0; paths != null && i < paths.size(); i++) {
            String path = paths.get(i);
            try {
                new StatFs(path);
            } catch (Exception e) {
                continue;
            }
            return path;
        }
        return null;
    }
}
