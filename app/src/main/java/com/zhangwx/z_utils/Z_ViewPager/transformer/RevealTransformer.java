package com.zhangwx.z_utils.Z_ViewPager.transformer;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * Created by zhangwx on 2017/3/14.
 */

public class RevealTransformer implements ViewPager.PageTransformer {

    private static final float SCROLL_SCALE = 0.6f;

    public void transformPage(View view, float position) {
        if (position == 0 || position == 1 || position == -1) {
            view.scrollTo(0, 0);
        }

        if (position < -0.999f || position > 0.999f) {
            view.setVisibility(View.INVISIBLE);
            return;
        }

        int pageWidth = view.getWidth();
        view.setVisibility(View.VISIBLE);

        if (position < 0) {  // (-1, 0)
            view.scrollTo((int)(pageWidth * position), 0);
        } else if (position < 1) {  // (0, 1)
            view.scrollTo((int)(pageWidth * position * SCROLL_SCALE), 0);
        }
    }
}
