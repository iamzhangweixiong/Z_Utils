package com.zhangwx.z_utils.Z_Intent.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.zhangwx.z_utils.Z_UI.DimenUtils;

/**
 * Created by Administrator on 2017/6/14.
 */

public class AccessGuideAnimHelper {
    private View mFingerView;
    private TickView mTickView;
    private ToggleView mToggleView;
    private AnimatorSet mAnimatorSet;

    private boolean mIsEnded;
    private final boolean mShowTick;

    final float mOffsetX = DimenUtils.dp2px(-60f);
    final float mOffsetY = DimenUtils.dp2px(70f);

    public AccessGuideAnimHelper(boolean showTick) {
        mShowTick = showTick;
    }

    public void setTargetViews(View fingerView, TickView tickView, ToggleView toggleView) {
        mFingerView = fingerView;
        mTickView = tickView;
        mToggleView = toggleView;
    }

    public void startGuideAnim() {
        final ValueAnimator anim1 = fingerTransAnim();
        final ValueAnimator anim2 = mShowTick ? tickAnim() : toggleAnim();

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playSequentially(anim1, anim2);
        mAnimatorSet.setStartDelay(800);
        mAnimatorSet.start();
    }

    public void clear() {
        mIsEnded = true;
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
    }


    private ValueAnimator fingerTransAnim() {

        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, mOffsetX, 0);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, mOffsetY, 0);
        final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mFingerView, holderX, holderY);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationEnd(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFingerView.setTranslationX(0);
                mFingerView.setTranslationY(0);
            }

            @Override
            public void onAnimationStart(Animator animation) {

                if (mShowTick) {
                    mTickView.setAlpha(0f);
                } else {
                    mToggleView.init();
                }
                mFingerView.setTranslationX(mOffsetX);
                mFingerView.setTranslationY(mOffsetY);
                mFingerView.setAlpha(1f);
                mFingerView.setVisibility(View.VISIBLE);
            }
        });
        animator.setDuration(1000);
        return animator;
    }

    private ValueAnimator toggleAnim() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                mToggleView.setCoef(fraction);
                mFingerView.setAlpha(1 - fraction);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mToggleView.startToggle();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mToggleView.stopToggle();
                mFingerView.setAlpha(0f);
                if (!mIsEnded) {
                    startGuideAnim();
                }
            }
        });
        return animator;
    }

    private ValueAnimator tickAnim() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                mTickView.setAlpha(fraction);
                mFingerView.setAlpha(1 - fraction);
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationEnd(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mTickView.setAlpha(1f);
                mFingerView.setAlpha(0f);
                if (!mIsEnded) {
                    startGuideAnim();
                }
            }
        });
        return animator;
    }
}
