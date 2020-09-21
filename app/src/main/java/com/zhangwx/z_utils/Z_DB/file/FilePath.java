package com.zhangwx.z_utils.Z_DB.file;

import android.content.Context;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

public class FilePath {

    /**
     * 文件夹类型
     * DIRECTORY_MUSIC：音乐类型
     * DIRECTORY_PICTURES：图片类型
     * DIRECTORY_MOVIES：电影类型
     * DIRECTORY_DCIM：照片类型
     * DIRECTORY_DOWNLOADS：下载文件类型
     * DIRECTORY_DOCUMENTS：文档类型
     * DIRECTORY_RINGTONES：铃声类型
     * DIRECTORY_ALARMS：闹钟提示音类型
     * DIRECTORY_NOTIFICATIONS：通知提示音类型
     * DIRECTORY_PODCASTS：播客音频类型
     *
     * 文件夹状态：
     * MEDIA_UNKNOWN：未知状态
     * MEDIA_REMOVED：移除状态（外部存储不存在）
     * MEDIA_UNMOUNTED：未装载状态（外部存储存在但是没有装载）
     * MEDIA_CHECKING：磁盘检测状态
     * MEDIA_NOFS：外部存储存在，但是磁盘为空或使用了不支持的文件系统
     * MEDIA_MOUNTED：就绪状态（可读、可写）
     * MEDIA_MOUNTED_READ_ONLY：只读状态
     * MEDIA_SHARED：共享状态（外部存储存在且正通过USB共享数据）
     * MEDIA_BAD_REMOVAL：异常移除状态（外部存储还没有正确卸载就被移除了）
     * MEDIA_UNMOUNTABLE：不可装载状态（外部存储存在但是无法被装载，一般是磁盘的文件系统损坏造成的）
     *
     *
     * 实际上支持外置存储卡的手机，有可能存在多个私有外部存储文件夹
     * 这时应该使用 getExternalFilesDirs + getExternalCacheDirs
     * 返回 File[] 数组
     */
    public static List<String> getAllFilePath(Context context) {
        final List<String> pathList = new ArrayList<>();
        pathList.clear();

        pathList.add("系统根文件夹：\n" + Environment.getRootDirectory());// 只读

        /* ---------------- 私有文件夹: ---------------- */
        pathList.add("\n私有文件夹：");
        pathList.add("内部自定义存储文件夹：\n" + context.getDir("ME", Context.MODE_PRIVATE).getPath());
        pathList.add("内部存储文件夹：\n" + context.getFilesDir().getPath());
        pathList.add("内部缓存文件夹：\n" + context.getCacheDir().getPath());

        pathList.add("外部存储文件夹: \n" + context.getExternalFilesDir(null).getPath());
        pathList.add("外部存储文件夹：DICM \n" + context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath());
        pathList.add("外部缓存文件夹：\n" + context.getExternalCacheDir().getPath());

        /* ---------------- 公共文件夹: ---------------- */
        pathList.add("\n公共文件夹：");
        pathList.add("外部存储文件夹：\n" + Environment.getExternalStorageDirectory().getPath());
        pathList.add("外部存储文件夹：DICM \n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath());
        pathList.add("数据根文件夹：\n" + Environment.getDataDirectory().getPath());
        pathList.add("缓存文件夹：\n" + Environment.getDownloadCacheDirectory());


        pathList.add("文件夹状态：\n" + Environment.getExternalStorageState());

        return pathList;
    }
}
