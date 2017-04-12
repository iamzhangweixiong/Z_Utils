package com.zhangwx.z_utils.Z_Pub;

import android.text.TextUtils;

import java.io.File;

public class PathUtil {
	// 添加斜杠
	public static String pathAddBackslash(final String path) {
		if (TextUtils.isEmpty(path)) {
			return File.separator;
		}

		if (path.charAt(path.length() - 1) != File.separatorChar) {
			return path + File.separatorChar;
		}

		return path;
	}

	public static String pathRemoveBackslash(String path) {
		if (TextUtils.isEmpty(path) || path.length() == 1) {
			return path;
		}

		if (path.charAt(path.length() - 1) == File.separatorChar) {
			return path.substring(0, path.length() - 1);
		}

		return path;
	}

	public static String pathFindFileName(String path) {
		String fileName = "";

		if (!TextUtils.isEmpty(path)) {
			path = PathUtil.pathRemoveBackslash(path);
			fileName = path.substring(path.lastIndexOf(File.separator) + 1);
		}

		return fileName;
	}

	public static String pathAppend(String path, String fileName) {
		String newPath = path;
		if (!TextUtils.isEmpty(fileName)) {
			newPath = pathAddBackslash(path) + fileName;
		}
		return newPath;
	}

    /**
     * 获取文件后缀
     */
    public static String pathFindExtension(String path) {
        if (path == null)
            return null;
        int index = path.lastIndexOf(".");
        if (index >= 0) {
            return path.substring(index + 1, path.length());
        }
        return null;
    }
}
