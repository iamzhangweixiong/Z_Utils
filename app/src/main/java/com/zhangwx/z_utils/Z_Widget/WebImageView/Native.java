package com.zhangwx.z_utils.Z_Widget.WebImageView;

import android.graphics.Bitmap;

public class Native {
	static {
		System.loadLibrary("native-lib");
	}

	public static native int gifAllocBuffer(int size);

	public static native void gifFreeBuffer(int buffer);

	public static native int gifOpenFD(int fd);

	public static native void gifClose(int obj);

	public static native int gifGetDuration(int obj);

	public static native int gifGetFrameCount(int obj);

	public static native int gifGetImageWidth(int obj);

	public static native int gifGetImageHeight(int obj);

	public static native void gifSetBkColor(int obj, int color);

	public static native int gifDecodeFrame(int obj, int index, int buffer);

	public static native boolean gifDrawFrame(int obj, int index, int buffer, Bitmap bitmap, Bitmap previous);
}
