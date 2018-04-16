package com.zhangwx.z_utils.Z_Widget.WebImageView;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.zhangwx.z_utils.Z_Widget.WebImageView.cache.DiskCache;
import com.zhangwx.z_utils.Z_Widget.WebImageView.cache.ImageCache;
import com.zhangwx.z_utils.Z_Widget.WebImageView.utils.CancelledException;
import com.zhangwx.z_utils.Z_Widget.WebImageView.utils.Downloader;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WebImageView extends ImageView {
	private static final String TAG = "WebImageView";

	private static final ExecutorService mExecutor = Executors.newFixedThreadPool(2);

	private final AspectRatioMeasure.Spec mMeasureSpec = new AspectRatioMeasure.Spec();
	private volatile boolean mAttachedToWindow;
	private float mAspectRatio = 0;
	private Drawable mLoadingDrawable;
	private Future mFuture;
	private Progress mProgress;
	private Poster mPoster;
	private Uri mUri;

	// round clipping after download
	private static final int CLIP_DURATION = 500;
	private RoundClipper mRoundClipper;
	private RectF mClipBounds;
	private Interpolator mClipInterpolator;
	private float mStartClipRadius;
	private float mDeltaClipRadius;
	private long mStartClipTime;
	/**
	 * Resource ID of the image to be used as a placeholder until the network image is loaded.
	 */
	private int mDefaultImageId;

	public WebImageView(Context context) {
		super(context);
	}

	public WebImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WebImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public File getCacheFile() {
		return getCacheFile(getContext(), mUri != null ? mUri.toString() : "");
	}

	public Uri getImageUri() {
		return mUri;
	}

	public String getMimeType() {
		final Drawable drawable = getDrawable();
		return (drawable instanceof ImageDrawable) ? ((ImageDrawable) drawable).mMimeType : null;
	}

	public void setImageUrl(String url) {
		final Uri uri = url != null ? Uri.parse(url) : null;
		setImageURI(uri);
	}

	@Override
	public void setImageURI(Uri uri) {
		if (uri == null) {
			cancelRequest();
			removePoster();
			setDefaultImageOrNull();
			mUri = null;
		} else if (isLocalUri(uri) || isWebUri(uri)) {
			loadImageIfNecessary(uri, false);
		} else {
			super.setImageURI(uri);
		}
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		final Drawable previous = getDrawable();
		if (previous != drawable && previous instanceof Animatable)
			((Animatable) previous).stop();

		super.setImageDrawable(drawable);

		if (drawable != null) {
			if (drawable instanceof ImageDrawable)
				((ImageDrawable) drawable).addRef();
			if (drawable instanceof Animatable && getVisibility() == VISIBLE)
				((Animatable) drawable).start();
		} else {
			if (mLoadingDrawable != null) {
				mLoadingDrawable.setLevel(0);
				mLoadingDrawable.setVisible(false, false);
			}
			destroyRoundClipper();
		}

		if (previous instanceof ImageDrawable)
			((ImageDrawable) previous).release();
	}

	public void setDefaultImageResId(int defaultImage) {
		mDefaultImageId = defaultImage;
	}

	public void setImageNull() {
		setImageDrawable(null);
	}

	public void setDefaultImageOrNull() {
		if(mDefaultImageId != 0) {
			setImageResource(mDefaultImageId);
		} else {
			setImageDrawable(null);
		}
	}

	public void setLoadingDrawable(Drawable drawable) {
		mLoadingDrawable = drawable;
		invalidate();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mAttachedToWindow = true;

		if (mUri != null)
			loadImageIfNecessary(mUri, true);
	}

	@Override
	protected void onDetachedFromWindow() {
		cancelRequest();
		removePoster();
		setDefaultImageOrNull();

		mAttachedToWindow = false;
		super.onDetachedFromWindow();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final Drawable drawable = getDrawable();
		if (drawable != null) {
			final long now;
			final Paint paint;
			if (mRoundClipper != null
					&& (now = SystemClock.uptimeMillis()) < mStartClipTime + CLIP_DURATION
					&& (paint = ImageDrawable.getPaint(drawable)) != null) {
				final float interpolate = mClipInterpolator.getInterpolation((now - mStartClipTime) / (float) CLIP_DURATION);
				final float radius = mStartClipRadius + mDeltaClipRadius * interpolate;
				final int paddingLeft = getPaddingLeft();
				final int paddingTop = getPaddingTop();
				final Matrix matrix = getImageMatrix();
				mClipBounds.set(drawable.getBounds());
				mClipBounds.offset(paddingLeft, paddingTop);
				if (matrix != null)
					matrix.mapRect(mClipBounds);
				mRoundClipper.beginClip(canvas, mClipBounds, paint, radius);
				super.onDraw(canvas);
				mRoundClipper.endClip(canvas, paint);
				// redraw next clipping frame
				drawable.invalidateSelf();
			} else {
				// don't need clipper anymore
				if (mRoundClipper != null) {
					destroyRoundClipper();
				}
				super.onDraw(canvas);
			}
		} else if (mLoadingDrawable != null
				&& mLoadingDrawable.isVisible()) {
			mLoadingDrawable.draw(canvas);
		}
	}

	@Override
	public void onDrawForeground(Canvas canvas) {
		final Drawable background = getBackground();
		if (getDrawable() != null && background instanceof RippleDrawable) {
			background.draw(canvas);
		} else {
			super.onDrawForeground(canvas);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		if (mUri != null)
			loadImageIfNecessary(mUri, true);

		if (mLoadingDrawable != null) {
			final int width = right - left;
			final int height = bottom - top;
			final float radius = Math.min(mLoadingDrawable.getIntrinsicWidth(), mLoadingDrawable.getIntrinsicHeight()) / 2f;
			final float centerX = width / 2f;
			final float centerY = height / 2f;
			mLoadingDrawable.setBounds((int) (centerX - radius), (int) (centerY - radius),
					Math.round(centerX + radius), Math.round(centerY + radius));

			if (mRoundClipper != null) {
				mStartClipRadius = radius;
				mDeltaClipRadius = Math.max(width, height) / 2f - radius;
				mStartClipTime = SystemClock.uptimeMillis();
			}
		}
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		final boolean visible = visibility == VISIBLE;
		final Drawable drawable = getDrawable();
		if (drawable != null)
			drawable.setVisible(visible, false);
	}

	/**
	 * Gets the desired aspect ratio (w/h).
	 */
	public float getAspectRatio() {
		return mAspectRatio;
	}

	/**
	 * Sets the desired aspect ratio (w/h).
	 */
	public void setAspectRatio(float aspectRatio) {
		if (mAspectRatio != aspectRatio) {
			mAspectRatio = aspectRatio;
			requestLayout();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mMeasureSpec.width = widthMeasureSpec;
		mMeasureSpec.height = heightMeasureSpec;
		AspectRatioMeasure.updateMeasureSpec(
				mMeasureSpec, mAspectRatio,
				getLayoutParams(),
				getPaddingLeft() + getPaddingRight(),
				getPaddingTop() + getPaddingBottom());
		super.onMeasure(mMeasureSpec.width, mMeasureSpec.height);
	}

	protected void onLoadImage(Drawable drawable) {
		setImageDrawable(drawable);
	}

	protected void onLoadFailed(Throwable e) {
		Log.e(TAG, "load failed: ", e);
	}

	protected void createRoundClipper() {
		mRoundClipper = new RoundClipper(true);
		mClipBounds = new RectF();
		mClipInterpolator = new AccelerateInterpolator();
	}

	protected void destroyRoundClipper() {
		mRoundClipper = null;
		mClipBounds = null;
		mClipInterpolator = null;
	}

	protected void loadImageIfNecessary(final Uri uri, boolean isInLayoutPass) {
		if (uri == null)
			return;
		if (uri.equals(mUri)) {
			if (mFuture != null && !mFuture.isDone()) {
				Log.i(TAG, "still loading: " + uri);
				return;
			}

			final Drawable drawable = getDrawable();
			if (drawable instanceof ImageDrawable) {
				final ImageDrawable image = (ImageDrawable) drawable;
				if (image.isValid()) {
					if (mAttachedToWindow && getVisibility() == VISIBLE
							&& image instanceof Animatable) {
						((Animatable) image).start();
					}
					Log.i(TAG, "already loaded: " + uri);
					return; // already loaded
				}
			}
		}

		cancelRequest();
		removePoster();
		mUri = uri;

		final int width = getWidth();
		final int height = getHeight();
		final ViewGroup.LayoutParams params = getLayoutParams();
		final boolean wrapWidth;
		final boolean wrapHeight;
		if (params != null) {
			wrapWidth = params.width == ViewGroup.LayoutParams.WRAP_CONTENT;
			wrapHeight = params.height == ViewGroup.LayoutParams.WRAP_CONTENT;
		} else {
			wrapWidth = wrapHeight = false;
		}
		// if the view's bounds aren't known yet, and this is not a wrap-content/wrap-content
		// view, hold off on loading the image.
		if (width == 0 && height == 0 && !(wrapWidth && wrapHeight))
			return;

		final String url = uri.toString();
		final String key = ImageCache.getCacheKey(url, width, height);
		final Context context = getContext().getApplicationContext();
		final ImageDrawable image = ImageCache.getInstance(context).get(key);
		if (image != null) {
			if (image.getCallback() == null) {
				if (isInLayoutPass)
					postImage(image, false);
				else
					onLoadImage(image);
				image.release();
				return;
			}
			image.release();
		}

		final boolean local = isLocalUri(uri);
		final File cacheFile = getCacheFile();
		if (local || cacheFile.length() > 0) {
			if (mLoadingDrawable != null)
				mLoadingDrawable.setVisible(false, false);
			mFuture = loadImageAsync(local ? uri : Uri.fromFile(cacheFile), false);
			return;
		}

		setDefaultImageOrNull();
		if (mLoadingDrawable != null)
			mLoadingDrawable.setVisible(true, false);

		final Progress progress = mProgress = new Progress(this);
		final DiskCache diskCache = DiskCache.getInstance(context);
		mFuture = Downloader.download(uri.toString(), true, diskCache, progress);
	}

	protected void cancelRequest() {
		final Future future = mFuture;
		mFuture = null;
		if (future != null)
			future.cancel(false);

		final Progress progress = mProgress;
		mProgress = null;
		if (progress != null)
			progress.cancel();
	}

	protected Future loadImageAsync(final Uri uri, final boolean fromNetwork) {
		final int width = getWidth();
		final int height = getHeight();
		final int maxPixels = (width > 0 && height > 0) ? width * height : 256 * 256;
		final DiskCache diskCache = DiskCache.getInstance(getContext());
		return mExecutor.submit(new Runnable() {
			private final Uri mLoadingUri = mUri;
			@Override
			public void run() {
				if (!diskCache.isOpened())
					diskCache.open();

				final Context context = getContext().getApplicationContext();
				final ImageDrawable image = ImageDrawable.decodeImage(context, uri, maxPixels);
				if (image != null) {
					final String key = ImageCache.getCacheKey(mLoadingUri.toString(), width, height);
					ImageCache.getInstance(context).put(key, image);
					if (equalsObject(mLoadingUri, mUri))
						postImage(image, fromNetwork);
					image.release();
				} else {
					if (equalsObject(mLoadingUri, mUri))
						postImage(new RuntimeException("load failed: " + uri), fromNetwork);
				}
			}
		});
	}

	protected void postImage(final Object object, final boolean fromNetwork) {
		removePoster();
		final Poster poster = mPoster = new Poster(this, object, fromNetwork);
		if (!post(poster)) {
			mPoster = null;
			poster.release();
		}
	}

	protected void removePoster() {
		final Poster poster = mPoster;
		mPoster = null;
		if (poster != null) {
			removeCallbacks(poster);
			poster.release();
		}
	}

	private static class Progress implements Downloader.Listener {
		private final Uri mLoadingUri;
		private volatile boolean mCancelled = false;
		private WebImageView mImageView;
		private int mLevel = 0;

		public Progress(WebImageView imageView) {
			mImageView = imageView;
			mLoadingUri = imageView.mUri;
		}

		public void cancel() {
			mCancelled = true;
		}

		@Override
		public boolean isCancelled() {
			return mCancelled;
		}

		@Override
		public void onProgress(long progress, long total) {
			final WebImageView imageView = mImageView;
			if (imageView != null && equalsObject(mLoadingUri, imageView.mUri)) {
				final Drawable drawable = imageView.mLoadingDrawable;
				if (drawable != null && total > 0) {
					final int level = (int) (progress * 10000 / total);
					if (mLevel != level) {
						mLevel = level;
						drawable.setLevel(mLevel);
						postInvalidateOnAnimation(imageView);
						Log.d(TAG, "load progress: " + progress + "/" + total);
					}
				}
			}
		}

		@Override
		public void onResult(String url, File file) {
			final WebImageView imageView = mImageView;
			if (imageView != null && equalsObject(mLoadingUri, imageView.mUri)) {
				imageView.mProgress = null;
				imageView.mFuture = imageView.loadImageAsync(Uri.fromFile(file), true);
			}
			mImageView = null;
		}

		@Override
		public void onError(String url, Throwable err) {
			final WebImageView imageView = mImageView;
			if (imageView != null && equalsObject(mLoadingUri, imageView.mUri)) {
				imageView.mProgress = null;
				if (err instanceof CancelledException)
					Log.i(TAG, "download cancelled: " + url);
				else
					imageView.postImage(err, true);
			}
			mImageView = null;
		}
	}

	private static class Poster implements Runnable {
		private final boolean mFromNetwork;
		private WebImageView mImageView;
		private Object mObject;

		public Poster(WebImageView imageView, Object object, boolean fromNetwork) {
			mImageView = imageView;
			mObject = object;
			mFromNetwork = fromNetwork;
			if (object instanceof ImageDrawable)
				((ImageDrawable) object).addRef();
		}

		public void release() {
			final Object object = mObject;
			mObject = null;
			mImageView = null;
			if (object instanceof ImageDrawable)
				((ImageDrawable) object).release();
		}

		@Override
		public void run() {
			final WebImageView imageView = mImageView;
			final Object object = mObject;
			if (imageView != null && imageView.mAttachedToWindow) {
				if (object instanceof Drawable) {
					imageView.onLoadImage((Drawable) object);
					if (mFromNetwork)
						imageView.createRoundClipper();
				} else if (object instanceof Throwable) {
					imageView.onLoadFailed((Throwable) object);
				}
			}
			release();
		}
	}

	public static boolean equalsObject(Object a, Object b) {
		return (a == b) || (a != null && a.equals(b));
	}

	public static File getCacheFile(Context context, String url) {
		return DiskCache.getInstance(context).getCache(DiskCache.hash64(url));
	}

	public static boolean isLocalUri(Uri uri) {
		final String scheme = uri != null ? uri.getScheme() : "";
		return ContentResolver.SCHEME_FILE.equals(scheme)
				|| ContentResolver.SCHEME_CONTENT.equals(scheme);
	}

	public static boolean isWebUri(Uri uri) {
		final String scheme = uri != null ? uri.getScheme() : "";
		return "http".equals(scheme)
				|| "https".equals(scheme);
	}

	public static void postInvalidateOnAnimation(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			view.postInvalidateOnAnimation();
		else
			view.postInvalidate();
	}
}
