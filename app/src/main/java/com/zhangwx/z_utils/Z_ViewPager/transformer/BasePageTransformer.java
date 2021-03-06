package com.zhangwx.z_utils.Z_ViewPager.transformer;

import android.annotation.TargetApi;
import android.os.Build;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * Created by zhangwx on 2017/1/1.
 */
public abstract class BasePageTransformer implements ViewPager.PageTransformer {
    protected ViewPager.PageTransformer mPageTransformer = NonPageTransformer.INSTANCE;
    public static final float DEFAULT_CENTER = 0.5f;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void transformPage(View view, float position) {
        if (mPageTransformer != null) {
            mPageTransformer.transformPage(view, position);
        }

        pageTransform(view, position);
    }

    protected abstract void pageTransform(View view, float position);


}
