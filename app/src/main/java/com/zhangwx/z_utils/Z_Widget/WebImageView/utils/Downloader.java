package com.zhangwx.z_utils.Z_Widget.WebImageView.utils;

import com.zhangwx.z_utils.Z_Widget.WebImageView.cache.DiskCache;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Downloader implements Runnable {
	private static final ExecutorService mExecutor = Executors.newCachedThreadPool();
	private static final ConcurrentHashMap<Long, Lock> mLoadings = new ConcurrentHashMap<>();

	public interface Listener extends HttpClient.Callback {
		void onResult(String url, File file);
		void onError(String url, Throwable err);
	}

	private static class Lock extends ReentrantLock {
		File file;
		long length;
	}

	protected final String mUrl;
	protected final boolean mSupportRange;
	protected final DiskCache mDiskCache;
	protected Listener mListener;
	protected Lock mLock;

	public Downloader(String url, boolean supportRange, DiskCache cache, Listener listener) {
		mUrl = url;
		mSupportRange = supportRange;
		mDiskCache = cache;
		mListener = listener;
	}

	public Future download() {
		return mExecutor.submit(this);
	}

	@Override
	public void run() {
		if (!mDiskCache.isOpened())
			mDiskCache.open();

		// lock downloading by the a uri related ReentrantLock to avoid conflict
		final long key = DiskCache.hash64(mUrl);
		mLock = lock(key);

		File partFile = null;
		try {
			File file = mDiskCache.getCache(key);
			if (file.exists() && !onCheckCache(file)) {
				mDiskCache.removeCache(key);
			}
			if (!file.exists()) {
				HttpClient.throwIfCancelled(mListener);
				partFile = mLock.file = mDiskCache.beginWrite(key);
				if (!mSupportRange)
					partFile.delete();
				onDownload(partFile);
				file = mDiskCache.endWrite(key);
			}
			mLoadings.remove(key);
			HttpClient.throwIfCancelled(mListener);
			mListener.onResult(mUrl, file);
		} catch (Throwable e) {
			if (!mSupportRange && partFile != null)
				partFile.delete();
			mListener.onError(mUrl, e);
		} finally {
			unlock();
			mListener = null;
		}
	}

	private Lock lock(long key) {
		final Lock newLock = new Lock();
		final Lock curLock = mLoadings.putIfAbsent(key, newLock);
		final Lock lock = curLock != null ? curLock : newLock;
		try {
			File file;
			while (!lock.tryLock(100, TimeUnit.MILLISECONDS)) {
				HttpClient.throwIfCancelled(mListener);
				if (lock.length > 0 && (file = lock.file) != null) {
					mListener.onProgress(file.length(), lock.length);
				}
			}
		} catch (Exception ignored) {
		}
		return lock;
	}

	private void unlock() {
		final Lock lock = mLock;
		mLock = null;
		if (lock != null)
			lock.unlock();
	}

	protected boolean onCheckCache(File file) {
		return file.length() > 0;
	}

	protected void onDownload(File file) throws Exception {
		HttpClient.downloadFile(file, mUrl, new HttpClient.Callback() {
			@Override
			public boolean isCancelled() {
				return mListener.isCancelled();
			}

			@Override
			public void onProgress(long progress, long total) {
				mListener.onProgress(progress, total);
				mLock.length = total;
			}
		});
	}

	public static Future download(String url, boolean supportRange, DiskCache cache, Listener listener) {
		return new Downloader(url, supportRange, cache, listener).download();
	}
}
