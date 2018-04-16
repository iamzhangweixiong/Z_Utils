package com.zhangwx.z_utils.Z_Widget.WebImageView.cache;

import android.content.Context;

import com.zhangwx.z_utils.Z_Widget.WebImageView.ImageDrawable;

public class ImageCache extends LruHashMap<String, ImageDrawable> {
	public ImageCache(int maxSize) {
		super(maxSize);
	}

	@Override
	public ImageDrawable get(String key) {
		final ImageDrawable img = super.get(key);
		return img != null ? img.addRef() : null;
	}

	@Override
	public ImageDrawable put(String key, ImageDrawable value) {
		if (value != null) {
			value.addRef();
			return super.put(key, value);
		}
		return null;
	}

	@Override
	protected void onRemoved(String key, ImageDrawable value) {
		value.release();
	}

	@Override
	protected int sizeOf(String key, ImageDrawable value) {
		return value.getByteCount();
	}

	public static String getCacheKey(String url, int width, int height) {
		return url + ":" + height;
	}

	public static final int CACHE_SIZE = 6 * 1024 * 1024;
	private static ImageCache mInstance;

	public static synchronized ImageCache getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new ImageCache(CACHE_SIZE);
		}
		return mInstance;
	}
}
