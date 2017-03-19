package com.zhangwx.z_utils.Z_Widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.zhangwx.z_utils.Z_UI.DimenUtils;

/**
 * Created by zhangwx on 2017/1/5.
 */
public class PullOrFlingFrameLayout extends FrameLayout {

    public static final String TAG = "Zhang";
    private int mScreenHeight;
    private float mStartY;
    private GestureDetector mGestureDetector;

    public PullOrFlingFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public PullOrFlingFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullOrFlingFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScreenHeight = DimenUtils.getWindowHeight();
        mGestureDetector = new GestureDetector(context, new GestureListener());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mStartY = event.getRawY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float transY = event.getRawY() - mStartY;
            if (transY > 0) {
                setTranslationY(transY);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawY() - mStartY > mScreenHeight / 4) {
                leave(getTranslationY(), mScreenHeight);
            } else {
                Log.e(TAG, "dispatchTouchEvent: smoothTransToTop");
                if (!mGestureDetector.onTouchEvent(event)) {
                    smoothTransToTop();
                }
            }
        }
        return mGestureDetector.onTouchEvent(event) || super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            Log.e(TAG, "onDown");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float velocity = (float) Math.hypot(velocityX, velocityY);
            Log.e(TAG, "onFling: velocityX = " + velocityX + "    velocityY = " + velocityY + "    velocity = " + velocity);
            if (velocityY > 0 && velocity > 3500) {
                leave(getTranslationY(), mScreenHeight);
                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }


    private void smoothTransToTop() {
        ObjectAnimator transAnima = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, getTranslationY(), 0);
        transAnima.setInterpolator(new LinearInterpolator());
        transAnima.setDuration(100);
        transAnima.start();
    }

    private void leave(float translationY, int screenHeight) {
        ObjectAnimator transAnima = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, (int) translationY, screenHeight);
        transAnima.setInterpolator(new LinearInterpolator());
        transAnima.setDuration(125);
        transAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        transAnima.start();
    }
}
