package com.zhangwx.z_utils.Z_UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by Administrator on 2016/12/14.
 */
public class AnimationUtils {

    public void startRotateAnimation(View view, long durationMillis, int repeatCount) {
        if (view == null) return;
        Animation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        view.setAnimation(rotateAnimation);
        rotateAnimation.setInterpolator(linearInterpolator);
        rotateAnimation.setDuration(durationMillis);
        rotateAnimation.setRepeatCount(repeatCount);
        rotateAnimation.start();
    }

    private void startRevealAnimation(final View view, final View parentView) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                float height = parentView.getHeight() * value;
                float width = parentView.getWidth() * value;
                parentView.setTranslationX(width);
                parentView.setTranslationY(height);
                view.setTranslationX(-width);
                view.setTranslationY(-height);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                parentView.setVisibility(View.VISIBLE);
                parentView.setTranslationX(0);
                parentView.setTranslationY(0);
                view.setTranslationX(0);
                view.setTranslationY(0);
            }
        });
        valueAnimator.start();
    }
}
