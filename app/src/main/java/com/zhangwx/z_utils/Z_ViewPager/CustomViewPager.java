package com.zhangwx.z_utils.Z_ViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * Created by zhangwx on 2016/12/21.
 */
public class CustomViewPager extends FrameLayout {

    float mStartX;
    float mStartY;
    float mMeasuredTotalWidth;
    float mPageItemX;
    float mPageMargin;
    ViewPager mViewPager;
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        event.setLocation(event.getX() - mViewPager.getLeft(), event.getY() - mViewPager.getTop());
        return mViewPager.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mStartY = ev.getRawY();
            mStartX = ev.getX();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float dis = Math.abs(ev.getRawY() - mStartY);
            if (dis > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                return super.dispatchTouchEvent(ev);
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            final float leftSpaceBoundary = mPageItemX - mPageMargin;
            final float rightSpaceBoundary = mMeasuredTotalWidth - leftSpaceBoundary;

            final float eventX = ev.getX();
            if (mStartX < leftSpaceBoundary && eventX < leftSpaceBoundary) {
                onClickLeftSpace();
                return true;
            } else if (mStartX > rightSpaceBoundary && eventX > rightSpaceBoundary) {
                onClickRightSpace();
                return true;
            }
            return super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void onClickRightSpace() {
        final int position = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(position + 1, true);
    }

    private void onClickLeftSpace() {
        final int position = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(position - 1, true);
    }
}
