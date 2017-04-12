package com.zhangwx.z_utils.Z_Pub;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class StorageUtil {

    public static boolean isSdcardMountedEx() {
        StorageList storageList = new StorageList();
        ArrayList<String> mountedVolumePaths = storageList.getMountedVolumePaths();
        return !(mountedVolumePaths == null || mountedVolumePaths.isEmpty());
    }

    public static StorageSpace getRemovableSdcardSpace() {
        StorageList storageList = new StorageList();
        ArrayList<String> mountedVolumePaths = storageList.getMountedSdCardVolumePaths();
        if (null == mountedVolumePaths) {
            return null;
        }
        return getStorageSpaceEx(mountedVolumePaths); // 红米等手机,SD卡为空也能读到 的bug优化
    }

    /***
     * 增强模式判断SD卡控件
     */
    private static StorageSpace getStorageSpaceEx(ArrayList<String> mountedVolumePaths) {
        if (mountedVolumePaths == null || mountedVolumePaths.isEmpty()) {
            return null;
        }

        StorageSpace allResult = null;
        for (int idx = 0; idx < mountedVolumePaths.size(); ++idx) {
            StorageSpace tempResult = getStorageSpaceEx(new File(mountedVolumePaths.get(idx))); // !!!

            if (null == tempResult) {
                continue;
            }

            if (null != allResult) {
                allResult.totalSize += tempResult.totalSize;
                allResult.availableSize += tempResult.availableSize;
            } else {
                allResult = tempResult;
            }
        }

        return allResult;
    }

    public static StorageSpace getNormalSdcardSpace() {
        if (EnvUtil.isSdcardMounted()) {
            File sdcardDir = EnvUtil.getExternalStorageDirectory();
            return getStorageSpace(sdcardDir);
        }

        return null;
    }

    /***
     * 增强判断 SD控件. (特别针对红米手机 没有插入SD卡的时候也会计算到空间的bug)
     */
    public static StorageSpace getStorageSpaceEx(File path) {
        StorageSpace space = new StorageSpace();

        boolean retIsStorageOk = false;
        try {
            File tmpFile = new File(path.toString() + "/xxx_" + System.currentTimeMillis());
            retIsStorageOk = tmpFile.createNewFile();
            tmpFile.delete();
        } catch (Exception e) {
            // do nothing
        }
        if (!retIsStorageOk) {
            space.totalSize = 0;
            space.availableSize = 0;
            return space;
        }

        return getStorageSpace(path);
    }

    public static StorageSpace getStorageSpace(File path) {
        StorageSpace space = new StorageSpace();

        StatFs sf = null;
        try {
            sf = new StatFs(path.getPath());
        } catch (Exception e) {
            return null;
        }

        long blockSize = sf.getBlockSize();

        space.totalSize = blockSize * sf.getBlockCount();
        space.availableSize = sf.getAvailableBlocks() * blockSize;

        return space;
    }

    public static class StorageSpace {
        public long totalSize = 0;
        public long availableSize = 0;
    }
    public static final String SD_CARD = "sdCard";
    public static final String EXTERNAL_SD_CARD = "externalSdCard";

    /**
     * @return A map of all storage locations available
     */
    public static Map<String, File> getAllStorageLocations() {
        Map<String, File> map = new HashMap<String, File>(10);
        List<String> mMounts = new ArrayList<String>(10);
        List<String> mVold = new ArrayList<String>(10);

        mMounts.add("/mnt/sdcard");
        mVold.add("/mnt/sdcard");

        try {
            File mountFile = new File("/proc/mounts");
            if (mountFile.exists()) {
                Scanner scanner = new Scanner(mountFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("/dev/block/vold/")) {
                        String[] lineElements = line.split(" ");
                        String element = lineElements[1];

                        // don't add the default mount path
                        // it's already in the list.
                        if (!element.equals("/mnt/sdcard"))
                            mMounts.add(element);
                    }
                }
            }
        } catch (Exception e) {
            // do nothing
        }

        try {
            File voldFile = new File("/system/etc/vold.fstab");
            if (voldFile.exists()) {
                Scanner scanner = new Scanner(voldFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("dev_mount")) {
                        String[] lineElements = line.split(" ");
                        String element = lineElements[2];

                        if (element.contains(":"))
                            element = element.substring(0, element.indexOf(":"));
                        if (!element.equals("/mnt/sdcard"))
                            mVold.add(element);
                    }
                }
            }
        } catch (Exception e) {
            // do nothing
        }

        for (int i = 0; i < mMounts.size(); i++) {
            String mount = mMounts.get(i);
            if (!mVold.contains(mount))
                mMounts.remove(i--);
        }
        mVold.clear();

        List<String> mountHash = new ArrayList<String>(10);

        for (String mount : mMounts) {
            File root = new File(mount);
            if (root.exists() && root.isDirectory() && root.canWrite()) {
                File[] list = root.listFiles();
                String hash = "[";
                if (list != null) {
                    for (File f : list) {
                        hash += f.getName().hashCode() + ":" + f.length() + ", ";
                    }
                }
                hash += "]";
                if (!mountHash.contains(hash)) {
                    String key = SD_CARD + "_" + map.size();
                    if (map.size() == 0) {
                        key = SD_CARD;
                    } else if (map.size() == 1) {
                        key = EXTERNAL_SD_CARD;
                    }
                    mountHash.add(hash);
                    map.put(key, root);
                }
            }
        }

        mMounts.clear();

        if (map.isEmpty()) {
            map.put(SD_CARD, EnvUtil.getExternalStorageDirectory());
        }
        return map;
    }

    public static String getInternalStorageDirectoryPath() {
        if (Build.MODEL.equals("ZTE V955")
                || Build.MODEL.equals("MI-ONE Plus")) {
            StorageList storageList = new StorageList();
            String strPath = storageList.getExternalPath(false);

            try {
                StatFs statFs = new StatFs(strPath);
                long internalSize = statFs.getBlockCount()
                        * statFs.getBlockSize();
                // 内置sdcard 需要大于1M
                if (internalSize >= 1024 * 1024) {
                    return strPath;
                }
            } catch (Exception e) {
                // do nothing
            }
        } else {
            if (Build.VERSION.SDK_INT >= 9) {
                if (EnvUtil.isSdcardMounted()
                        && !Environment.isExternalStorageRemovable()) {
                    return EnvUtil.getExternalStorageDirectory().getPath();
                }
            } else {
                if (EnvUtil.isSdcardMounted()) {
                    String strPath = EnvUtil.getExternalStorageDirectory().getPath();
                    if (!strPath.equalsIgnoreCase(getExternalStorageDirectoryPath())) {
                        return strPath;
                    }
                }
            }
        }

        return null;
    }

    /**
     * 找到第一张外置adcard
     */
    public static String getExternalStorageDirectoryPath() {
        if (Build.MODEL.equals("ZTE V955")
                || Build.MODEL.equals("MI-ONE Plus")) {
            return EnvUtil.getExternalStorageDirectory().getPath();
        } else {
            StorageList storageList = new StorageList();
            return storageList.getExternalPath(true);
        }
    }

}
