package com.zhangwx.z_utils.Z_Widget.WebImageView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.util.Log;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class GifDrawable extends ImageDrawable implements Animatable {
	private static final String TAG = "GifDrawable";

	private static final int STATE_NONE = 0;
	private static final int STATE_DECODE = 1;
	private static final int STATE_DECODING = 2;
	private static final int STATE_READY = 3;
	private static final int STATE_DRAW = 4;
	private static final int STATE_NEXT = 5;

	private static final Executor mExecutor = Executors.newFixedThreadPool(2);

	private int mGifImage;
	private int mBuffer;
	private int mBufSize;
	private int mDecodeIndex;
	private int mFrameCount;
	private long mNextFrameTime;
	private final AtomicInteger mState = new AtomicInteger(STATE_NONE);

	public GifDrawable(ParcelFileDescriptor pfd) {
		this(pfd, 0);
	}

	public GifDrawable(ParcelFileDescriptor pfd, int maxPixels) {
		int gif = 0;
		try {
			gif = Native.gifOpenFD(pfd.getFd());
			if (gif == 0)
				return; // throw new RuntimeException("load error");

			mFrameCount = Native.gifGetFrameCount(gif);

			final int width = Native.gifGetImageWidth(gif);
			final int height = Native.gifGetImageHeight(gif);
			mBufSize = width * height;
			mBuffer = Native.gifAllocBuffer(mBufSize);
			if (mBuffer == 0)
				throw new RuntimeException("out of buffer");

			final int sampleSize = maxPixels > 0 ? computeSampleSize(width * height, maxPixels) : 1;
			mBitmap = Bitmap.createBitmap(width / sampleSize, height / sampleSize, Bitmap.Config.ARGB_8888);
			mMimeType = "image/gif";
			mGifImage = gif;
			Log.d(TAG, "open: " + this);
			return;
		} catch (Throwable e) {
			Log.e(TAG, "load failed: ", e);
		}

		if (gif != 0)
			Native.gifClose(gif);
	}

	@Override
	public int getByteCount() {
		return super.getByteCount() + mBufSize;
	}

	public long getDuration() {
		final int gif = mGifImage;
		return gif != 0 ? Native.gifGetDuration(gif) : 0;
	}

	public int getFrameCount() {
		return mFrameCount;
	}

	@Override
	public boolean isRunning() {
		return mState.get() != STATE_NONE;
	}

	@Override
	public void start() {
		if (mState.compareAndSet(STATE_NONE, STATE_NEXT)
				|| mState.compareAndSet(STATE_READY, STATE_NEXT)
				/*|| mState.compareAndSet(STATE_DRAW, STATE_NEXT)*/) {
			unschedule(mRedrawTask);
			if (mRedrawTask != null) {
				mRedrawTask.run();
			}
		} else {
			//Log.w(TAG, "wrong state when start: " + this + ", " + mState.get());
		}
		//Log.d(TAG, "start: " + this);
	}

	@Override
	public void stop() {
		unschedule(mRedrawTask);
		mState.set(STATE_NONE);
		//Log.d(TAG, "stop: " + this);
	}

	@Override
	public void draw(Canvas canvas) {
		if (mNextFrameTime == 0)
			return;

		super.draw(canvas);

		if (mFrameCount > 1 && mState.compareAndSet(STATE_DRAW, STATE_NEXT)) {
			// delay decode the next frame to avoid flicking
			if (!schedule(mRedrawTask, mNextFrameTime))
				mState.set(STATE_NONE);
		}
	}

	@Override
	public boolean setVisible(boolean visible, boolean restart) {
		final boolean changed = super.setVisible(visible, restart);
		if (changed) {
			if (visible && !isRunning())
				start();
			else if (!visible && isRunning())
				stop();
		}
		return changed;
	}

	@Override
	protected void recycle() {
		stop();

		synchronized (this) {
			if (mBuffer != 0) {
				Native.gifFreeBuffer(mBuffer);
				mBuffer = 0;
			}
			if (mGifImage != 0) {
				Log.d(TAG, "close: " + this);
				Native.gifClose(mGifImage);
				mGifImage = 0;
			}
			mDecodeIndex = 0;
			mFrameCount = 0;
			mNextFrameTime = 0;
			mDecodeTask = null;
			mRedrawTask = null;
		}

		super.recycle();
	}

	public boolean invalidate() {
		Drawable.Callback callback = getCallback();
		if (callback != null) {
			callback.invalidateDrawable(this);
			return true;
		}
		return false;
	}

	public boolean schedule(Runnable task, long when) {
		Drawable.Callback callback;
		if (task != null && (callback = getCallback()) != null) {
			callback.scheduleDrawable(this, task, when);
			return true;
		}
		return false;
	}

	public void unschedule(Runnable task) {
		Drawable.Callback callback;
		if (task != null && (callback = getCallback()) != null)
			callback.unscheduleDrawable(this, task);
	}

	protected int decodeNextFrame() {
		final int duration;
		synchronized (this) {
			if (mGifImage != 0 && mBuffer != 0) {
				duration = Native.gifDecodeFrame(mGifImage, mDecodeIndex, mBuffer);
				if (duration > 0)
					Native.gifDrawFrame(mGifImage, mDecodeIndex, mBuffer, mBitmap, null);
				else
					Log.w(TAG, "decode: " + this + ", frame=" + mDecodeIndex + ", duration=" + duration);
				if (++mDecodeIndex >= mFrameCount)
					mDecodeIndex = 0;
			} else {
				duration = 0;
			}
		}
		return duration;
	}

	private Runnable mDecodeTask = new Runnable() {
		@Override
		public void run() {
			// run in background thread
			if (mState.compareAndSet(STATE_DECODE, STATE_DECODING)) {
				final long time = SystemClock.uptimeMillis();
				final int duration = decodeNextFrame();
				if (duration > 0) {
					mNextFrameTime = time + duration;
					if (mState.compareAndSet(STATE_DECODING, STATE_READY)) {
						if (!schedule(mRedrawTask, SystemClock.uptimeMillis()))
							mState.set(STATE_NONE);
					}
				} else {
					mState.set(STATE_NONE);
				}
			}
		}
	};

	private Runnable mRedrawTask = new Runnable() {
		@Override
		public void run() {
			// run in UI thread
			if (mState.compareAndSet(STATE_READY, STATE_DRAW)) {
				if (!invalidate())
					mState.set(STATE_NONE);
			} else if (mState.compareAndSet(STATE_NEXT, STATE_DECODE)) {
				mExecutor.execute(mDecodeTask);
			}
		}
	};
}
