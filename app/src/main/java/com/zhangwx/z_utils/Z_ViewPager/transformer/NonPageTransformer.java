package com.zhangwx.z_utils.Z_ViewPager.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by zhangwx on 2017/1/1.
 */
public class NonPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        page.setScaleX(0.999f);//hack
    }

    public static final ViewPager.PageTransformer INSTANCE = new NonPageTransformer();
}
