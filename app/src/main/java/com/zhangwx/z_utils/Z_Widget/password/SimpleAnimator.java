package com.zhangwx.z_utils.Z_Widget.password;

import android.os.Build;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class SimpleAnimator implements Runnable {
	private boolean mAutoMode;
	private long mStartTime;
	private long mDuration;
	private final View mView;
	private final Interpolator mInterpolator;

	public SimpleAnimator(View view, Interpolator interpolator) {
		mView = view;
		mInterpolator = interpolator;
	}

	public void start(long duration, boolean autoMode) {
		// in JellyBean, animate with postOnAnimation() is better in Nexus 7(2012)
		// when rotate screen by image size
		mAutoMode = autoMode || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
		mStartTime = AnimationUtils.currentAnimationTimeMillis();
		mDuration = duration;

		onStart();

		if (mAutoMode)
			postOnAnimation(mView, this);
		else
			mView.invalidate();
	}

	public void stop() {
		if (mAutoMode)
			mView.removeCallbacks(this);
	}

	public void run() {
		if (mAutoMode)
			interpolate();
	}

	// should be called in view's onDraw() if not auto mode
	public void step() {
		if (!mAutoMode)
			interpolate();
	}

	private boolean interpolate() {
		float interpolation;
		boolean done;

		final long currentTime = AnimationUtils.currentAnimationTimeMillis();
		if (mDuration > 0) {
			final float currentMs = Math.min(mDuration, currentTime - mStartTime);
			interpolation = currentMs / mDuration;    // Default is linear
			if (mInterpolator != null)
				interpolation = mInterpolator.getInterpolation(interpolation);
			done = currentMs >= mDuration;
		} else {
			interpolation = -1;
			done = false;
		}

		if (done) {
			onDoing(1);
			stop();
			onEnd();
		} else {
			if (mAutoMode)
				postOnAnimation(mView, this);
			//	else
			//		mView.invalidate();

			// We can cancel in onDoing()
			onDoing(interpolation);
		}
		return !done;
	}

	public static void postOnAnimation(View view, Runnable runnable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			view.postOnAnimation(runnable);
		else
			view.postDelayed(runnable, 1000 / 60); // 60 FPS
	}

	//	Overridable
	public void onStart() {
	}

	public void onDoing(float interpolation) {
	}

	public void onEnd() {
	}
}
