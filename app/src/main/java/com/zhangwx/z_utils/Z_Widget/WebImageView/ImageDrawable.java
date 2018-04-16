package com.zhangwx.z_utils.Z_Widget.WebImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public class ImageDrawable extends Drawable {
	protected Bitmap mBitmap;
	protected Paint mPaint;
	protected String mMimeType;
	private final AtomicInteger mCounter = new AtomicInteger(1);

	public ImageDrawable() {
	}

	public ImageDrawable(Bitmap bitmap, String mimeType) {
		mBitmap = bitmap;
		mMimeType = mimeType;
	}

	public ImageDrawable addRef() {
		mCounter.incrementAndGet();
		return this;
	}

	public int release() {
		final int ref = mCounter.decrementAndGet();
		if (ref == 0)
			recycle();
		return ref;
	}

	public Bitmap detach() {
		final Bitmap bitmap = mBitmap;
		mBitmap = null;
		mPaint = null;
		mMimeType = null;
		return bitmap;
	}

	public boolean isValid() {
		return mBitmap != null;
	}

	protected void recycle() {
		final Bitmap bitmap = detach();
		if (bitmap != null)
			bitmap.recycle();
	}

//	Check if any object leaking in finalize()
//	@Override
//	protected void finalize() throws Throwable {
//		final int ref = mCounter.get();
//		if (ref > 0)
//			Log.w("ImageDrawable", "leak object: " + this + ", ref=" + ref);
//		super.finalize();
//	}

	@Override
	public void draw(Canvas canvas) {
		final Bitmap bitmap = mBitmap;
		if (bitmap != null && !bitmap.isRecycled()) {
			final Rect bounds = getBounds();
			canvas.drawBitmap(bitmap, null, bounds, getPaint());
		}
	}

	public int getByteCount() {
		final Bitmap bitmap = mBitmap;
		return bitmap != null ? bitmap.getRowBytes() * bitmap.getHeight() : 0;
	}

	public String getMimeType() {
		return mMimeType;
	}

	public Paint getPaint() {
		if (mPaint == null)
			mPaint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
		return mPaint;
	}

	@Override
	public int getIntrinsicWidth() {
		final Bitmap bitmap = mBitmap;
		return bitmap != null ? bitmap.getWidth() : 0;
	}

	@Override
	public int getIntrinsicHeight() {
		final Bitmap bitmap = mBitmap;
		return bitmap != null ? bitmap.getHeight() : 0;
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
		getPaint().setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter filter) {
		getPaint().setColorFilter(filter);
	}

	public static Paint getPaint(Drawable drawable) {
		if (drawable instanceof ImageDrawable)
			return ((ImageDrawable) drawable).getPaint();
		else if (drawable instanceof BitmapDrawable)
			return ((BitmapDrawable) drawable).getPaint();
		else if (drawable instanceof ShapeDrawable)
			return ((ShapeDrawable) drawable).getPaint();
		else
			return null;
	}

	public static int computeSampleSize(int bmpPixels, int maxPixels) {
		int pixels = bmpPixels;
		int sample = 1;
		while (maxPixels > 0 && pixels > maxPixels) {
			if (sample < 8)
				sample *= 2;
			else
				sample += 8;
			pixels = bmpPixels / (sample * sample);
		}
		return sample;
	}

	public static Bitmap decodeBitmap(ParcelFileDescriptor pfd, BitmapFactory.Options opts, int maxPixels) {
		if (opts == null)
			opts = new BitmapFactory.Options();
		final Bitmap.Config prefConfig = opts.inPreferredConfig;

		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor(), null, opts);
		opts.inJustDecodeBounds = false;

		final int width = opts.outWidth;
		final int height = opts.outHeight;
		opts.inSampleSize = computeSampleSize(opts.outWidth * opts.outHeight, maxPixels);

		for (;;) {
			try {
				return BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor(), null, opts);
			} catch (OutOfMemoryError e) {
				if (opts.inPreferredConfig == Bitmap.Config.ARGB_8888) {
					opts.inPreferredConfig = Bitmap.Config.RGB_565;
				} else {
					opts.inSampleSize *= 2;
					opts.inPreferredConfig = prefConfig;
				}
			} catch (Throwable e) {
				Log.e("ImageDrawable", "decode failed: " + pfd + ", " + width + "x" + height + "/" + opts.inSampleSize, e);
				break;
			}
		}
		return null;
	}

	public static ImageDrawable decodeImage(Context context, Uri uri, int maxPixels) {
		ImageDrawable image = null;
		ParcelFileDescriptor pfd = null;
		try {
			pfd = context.getContentResolver().openFileDescriptor(uri, "r");
			GifDrawable gif = new GifDrawable(pfd, maxPixels);
			if (gif.isValid()) {
				image = gif;
			} else {
				final BitmapFactory.Options opts = new BitmapFactory.Options();
				final Bitmap bitmap = ImageDrawable.decodeBitmap(pfd, opts, maxPixels);
				if (bitmap != null) {
					image = new ImageDrawable(bitmap, opts.outMimeType);
				}
			}
		} catch (Throwable e) {
			Log.e("ImageDrawable", "decode failed: " + uri, e);
		} finally {
			try {
				if (pfd != null)
					pfd.close();
			} catch (Exception ignored) {
			}
		}
		return image;
	}
}
