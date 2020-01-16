package com.zhangwx.z_utils.Z_ViewPager.transformer;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by zhangwx on 2016/12/21.
 */
public class ScaleTransFormer implements ViewPager.PageTransformer {

    public static final String TAG = "zhang";
    private static final float OFFSCREEN_PAGE_SCALE_FACTOR = 0.75F;
    private static final float OFFSCREEN_PAGE_ALPHA_FACTOR = 0.50F;

    @Override
    public void transformPage(View page, float position) {
        Log.e(TAG, "transformPage: position = " + position);
        if (position < -2 || position > 2) {
            return;
        }
        if (position >= -1 && position <= 0) {
            changeViewScale(page, Math.abs(position));
            changeViewAlpha(page, Math.abs(position));
        } else if (position >= 0 && position <= 1) {
            changeViewScale(page, Math.abs(position));
        } else {
            changeViewScale(page, 1.0F);
        }
    }

    private void changeViewAlpha(@NonNull View view, float factor) {
        final float alphaFactor = 1.0F - ((1.0F - OFFSCREEN_PAGE_ALPHA_FACTOR) * factor);
        view.setAlpha(alphaFactor);
    }

    private void changeViewScale(@NonNull View view, float factor) {
        final float scaleFactor = 1.0F - ((1.0F - OFFSCREEN_PAGE_SCALE_FACTOR) * factor);
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
    }
}
