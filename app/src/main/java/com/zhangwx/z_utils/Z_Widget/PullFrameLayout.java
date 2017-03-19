package com.zhangwx.z_utils.Z_Widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.zhangwx.z_utils.Z_UI.DimenUtils;

/**
 * Created by zhangwx on 2017/1/5.
 */
public class PullFrameLayout extends FrameLayout {

    public static final String TAG = "Zhang";
    private float mStartY;
    private int mScreenHeight = 0;
    public PullDownListener mPullDownListener;
    public IDetailPageListener mDetailPageListener;

    public PullFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public PullFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScreenHeight = DimenUtils.getWindowHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartY = event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
                if (event.getRawY() - mStartY > mScreenHeight / 3) {
                    leave(getTranslationY(), mScreenHeight);
                } else {
                    smoothTransToTop();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                float transY = event.getRawY() - mStartY;
                if (transY > 0) {
                    setTranslationY(transY);
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                setTranslationY(0);
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            mStartY = ev.getRawY();
        } else if (action == MotionEvent.ACTION_MOVE) {
            float dis = ev.getRawY() - mStartY;
            if (dis > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void smoothTransToTop() {
        ObjectAnimator transAnima = ObjectAnimator
                .ofFloat(this, View.TRANSLATION_Y, getTranslationY(), 0)
                .setDuration(100);
        transAnima.setInterpolator(new LinearInterpolator());
        transAnima.start();
    }

    private void leave(float translationY, int screenHeight) {
        ObjectAnimator transAnima = ObjectAnimator
                .ofFloat(this, View.TRANSLATION_Y, (int) translationY, screenHeight)
                .setDuration(125);
        transAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                setTranslationY(0);
            }
        });
        transAnima.setInterpolator(new LinearInterpolator());
        transAnima.start();
    }

    private boolean needIntercept() {
        return mDetailPageListener != null && mDetailPageListener.isGalleryPage();
    }

    public void setPullDownListener(PullDownListener listener) {
        this.mPullDownListener = listener;
    }

    public void setDetailPageListener(IDetailPageListener listener) {
        this.mDetailPageListener = listener;
    }

    public interface PullDownListener {
        void onPullDownOverOneThird(int currY, int screenHeight);
    }

    public interface IDetailPageListener {
        boolean isGalleryPage();
    }

}
