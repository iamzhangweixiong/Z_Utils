package com.zhangwx.z_utils.Z_Widget.WebImageView.cache;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DiskCache {
	private static final String TAG = "DiskCache";
	private static final String INDEX_NAME = "index";
	private static final String PART_NAME = ".part";

	// 8 for key, 8 for file size, 8 for access time
	private static final int OFFSET_SIZE = 8;
	private static final int OFFSET_TIME = OFFSET_SIZE + 8;
	private static final int ITEM_BYTES = OFFSET_TIME + 8;

	private int mMaxPosition;
	private final File mDir;
	private final Object mLock = new Object();
	private final LruHashMap<Long, Integer> mIndex;
	private final LruHashMap<Integer, Long> mRecycledPositions = new LruHashMap<>(Long.MAX_VALUE);

	private final MappedFile mIndexFile = new MappedFile() {
		@Override
		protected int getExpandSize() {
			return ITEM_BYTES * 100;
		}
	};

	private static class Item implements Comparable<Item> {
		long key;
		long time;

		public Item(long key, long time) {
			this.key = key;
			this.time = time;
		}

		@Override
		public int compareTo(Item that) {
			return (int) (time - that.time);
		}
	}

	public DiskCache(File dir, long maxSize) {
		mDir = dir;
		mIndex = new LruHashMap<Long, Integer>(maxSize) {
//			@Override
//			public long trimToSize(long maxSize) {
//				final long trimmed = super.trimToSize(maxSize);
//				if (trimmed > 0)
//					Log.d(TAG, "trimmed=" + trimmed + ", size=" + super.size() + ", count=" + super.count());
//				return trimmed;
//			}

			@Override
			protected void onRemoved(Long key, Integer position) {
				try {
					// write 0 to mark it as deleted
					mIndexFile.writeLong(position, 0);
					mIndexFile.writeLong(position + OFFSET_SIZE, 0);
					mIndexFile.writeLong(position + OFFSET_TIME, 0);
					//mIndexFile.force();

					mRecycledPositions.put(position, key);
				} catch (Exception e) {
					Log.e(TAG, "write failed", e);
				}

				final File fileCache = onGenerateCacheFile(key);
				if (!fileCache.delete())
					Log.e(TAG, "delete failed: " + fileCache);
			}

			@Override
			protected int sizeOf(Long key, Integer value) {
				return (int) onGenerateCacheFile(key).length();
			}
		};
	}

	public int count() {
		synchronized (mLock) {
			return mIndex.count();
		}
	}

	public long size() {
		synchronized (mLock) {
			return mIndex.size();
		}
	}

	public boolean isOpened() {
		return mIndexFile.isOpened();
	}

	public void open() {
		synchronized (mLock) {
			if (mIndexFile.isOpened())
				return;

			try {
				final long ms = System.currentTimeMillis();
				if (!mDir.exists())
					mDir.mkdirs();

				final File file = new File(mDir, INDEX_NAME);
				if (!file.exists())
					deleteDirectory(mDir); // delete the old cache files

				mIndexFile.open(file, false);

				int position = 0;
				final int length = mIndexFile.size();
				final ArrayList<Item> items = new ArrayList<>(length / ITEM_BYTES);
				while (position + ITEM_BYTES <= length) {
					final long key = mIndexFile.readLong(position);
					final long size = mIndexFile.readLong(position + OFFSET_SIZE);
					final long time = mIndexFile.readLong(position + OFFSET_TIME);
					if (size != 0) {
						final Item item = new Item(key, time);
						items.add(item);
						mIndex.put(key, position);
					} else {
						// size is 0, it was marked as deleted
						mRecycledPositions.put(position, key);
					}
					position += ITEM_BYTES;
				}
				mMaxPosition = position;

				// put by access time order
				if (!mIndex.isEmpty()) {
					Collections.sort(items);
					for (Item item : items)
						mIndex.get(item.key); // for access order
				}
				Log.d(TAG, "open " + mIndex.size() + " bytes, used " + (System.currentTimeMillis() - ms));
			} catch (Exception e) {
				Log.e(TAG, "open failed: ", e);
			}
		}
	}

	public void close() {
		synchronized (mLock) {
			mIndex.clear();
			mIndexFile.close();
		}
	}

	public void clear() {
		synchronized (mLock) {
			final boolean opened = isOpened();
			close();
			deleteDirectory(mDir);
			if (opened)
				open();
		}
	}

	public File getCache(long key) {
		final File fileCache = onGenerateCacheFile(key);
		final long length = fileCache.length();

		synchronized (mLock) {
			final Integer position = mIndex.get(key);
			if (position != null) {
				try {
					final long size = mIndexFile.readLong(position + OFFSET_SIZE);
					if (size != length) {
						mIndexFile.writeLong(position + OFFSET_SIZE, length);
						Log.w(TAG, "wrong size: " + size + "/" + length);
					}
					mIndexFile.writeLong(position + OFFSET_TIME, System.currentTimeMillis());
					//mIndexFile.force();
				} catch (Exception e) {
					Log.e(TAG, "write failed", e);
				}
			}
		}
		return fileCache;
	}

	public File beginWrite(long key) {
		return onGeneratePartFile(key);
	}

	public File endWrite(long key) {
		final File fileCache = onGenerateCacheFile(key);
		final File filePart = onGeneratePartFile(key);
		final long length = filePart.length();
		if (filePart.renameTo(fileCache)) {
			synchronized (mLock) {
				try {
					Integer position = mIndex.get(key);
					if (position != null) {
						final long size = mIndexFile.readLong(position + OFFSET_SIZE);
						if (size != 0)
							Log.w(TAG, "size != 0: " + size);
					} else {
						position = getAvailablePosition();
						mIndex.put(key, position);
					}
					mIndexFile.writeLong(position, key);
					mIndexFile.writeLong(position + OFFSET_SIZE, length);
					mIndexFile.writeLong(position + OFFSET_TIME, System.currentTimeMillis());
					//mIndexFile.force();
				} catch (Exception e) {
					Log.e(TAG, "write failed", e);
				}
			}
		} else
			Log.e(TAG, "rename failed: " + filePart);
		return fileCache;
	}

	public void removeCache(long key) {
		synchronized (mLock) {
			mIndex.remove(key);
		}
	}

	protected int getAvailablePosition() {
		synchronized (mLock) {
			final Map.Entry<Integer, Long> entry = mRecycledPositions.eldest();
			if (entry != null) {
				final int position = entry.getKey();
				mRecycledPositions.remove(position);
				return position;
			}

			final int position = mMaxPosition;
			mMaxPosition += ITEM_BYTES;
			return position;
		}
	}

	protected File onGenerateCacheFile(long key) {
		final File file = new File(mDir, Long.toHexString(key));
		if (!file.exists()) {
			// for older version which all the cache files
			final File fileOld = new File(mDir, Long.toHexString(key) + ".gif");
			if (fileOld.exists())
				fileOld.renameTo(file);
		}
		return file;
	}

	protected File onGeneratePartFile(long key) {
		return new File(mDir, Long.toHexString(key) + PART_NAME);
	}

	public static void deleteDirectory(File dir) {
		final File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory())
					deleteDirectory(file);
				file.delete();
			}
		}
	}

	public static long hashCode(String str, int begin, int end) {
		final int len = str != null ? str.length() : 0;
		if (end > len)
			end = len;
		if (begin < 0)
			begin = 0;

		long h = 0;
		for (int i = begin; i < end; i++)
			h = h * 31 + str.charAt(i);
		return h;
	}

	private static final ConcurrentHashMap<Long, String> sHash64Checker = new ConcurrentHashMap<>();
	public static long hash64(String str) {
		final int len = str != null ? str.length() : 0;
		if (len == 0)
			return 0;

		final int half = len / 2;
		final long hash = (hashCode(str, 0, half) << 32) | (0xffffffffL & hashCode(str, half, len));
		final String prev = sHash64Checker.put(hash, str);
		if (prev != null && !prev.equals(str))
			Log.e(TAG, "hash64 conflict: " + prev + ", " + str + " -> " + hash);
		return hash;
	}

	public static final long CACHE_SIZE = 128 * 1024 * 1024;//FIXME
	public static final String CACHE_DIR = "gif_cache";
	private static DiskCache mInstance;

	public static synchronized DiskCache getInstance(Context context) {
		if (mInstance == null) {
			final File cacheDir = new File(context.getExternalCacheDir(), CACHE_DIR);
			mInstance = new DiskCache(cacheDir, CACHE_SIZE);
		}
		return mInstance;
	}
}
