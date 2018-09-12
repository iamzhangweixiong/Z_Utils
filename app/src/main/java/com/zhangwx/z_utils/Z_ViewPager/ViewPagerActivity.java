package com.zhangwx.z_utils.Z_ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.widget.LinearLayout;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_UI.ViewUtils;
import com.zhangwx.z_utils.Z_ViewPager.transformer.NonPageTransformer;
import com.zhangwx.z_utils.Z_ViewPager.transformer.RevealTransformer;
import com.zhangwx.z_utils.Z_ViewPager.transformer.ScaleTransFormer;

/**
 * Created by zhangwx on 2016/12/21.
 */
public class ViewPagerActivity extends Activity {

    private static final int OFFSCREEN_PAGE_LIMIT = 2;
    private ViewPager mViewPager;
    private GestureDetector mGestureDetector;
    private LinearLayout mPagerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        mViewPager = ViewUtils.$(this, R.id.ViewPager);
        mPagerLayout = ViewUtils.$(this, R.id.pagerLayout);
        mViewPager.setAdapter(new ViewPagerAdapter(this));
        mViewPager.setPageTransformer(false, new ScaleTransFormer());
        mViewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);

    }


}
