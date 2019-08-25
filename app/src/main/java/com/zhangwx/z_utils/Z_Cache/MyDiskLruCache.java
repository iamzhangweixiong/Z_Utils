package com.zhangwx.z_utils.Z_Cache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.zhangwx.z_utils.MyApplication;
import com.zhangwx.z_utils.Z_Reflect.ReflectHelper;
import com.zhangwx.z_utils.Z_Utils.FileUtil;
import com.zhangwx.z_utils.Z_Utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class MyDiskLruCache {

    private DiskLruCache diskLruCache;
    private static int index = 9;

    MyDiskLruCache() {
        final File file = FileUtils.getCacheDirectory(MyApplication.getContext(), "LruCache");
        try {
            diskLruCache = DiskLruCache.open(file, 1, 1, 12);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putValue() {
        try {
            DiskLruCache.Editor editor = diskLruCache.edit("12345" + index++);
            editor.set(0, "HelloWordHelloWordHelloWordHelloWordHelloWordHelloWordHelloWordHelloWordHelloWord");
            editor.commit();
//            editor.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getValue() {
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get("12345");
            return snapshot.getString(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getJFile() {
        File file = (File) ReflectHelper.getFieldValue(diskLruCache, "journalFile");
        String[] strings = FileUtil.readFileString(file.getPath());
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s);
            builder.append("\n");
        }
        return builder.toString();
    }



}
