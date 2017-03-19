package com.zhangwx.z_utils.Z_ViewPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangwx.z_utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwx on 2016/12/21.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private View mLayout;
    private List<View> dataList = new ArrayList<>();

    public ViewPagerAdapter(Context context) {
        mContext = context;
        for (int i = 0; i < 10; i++) {
            dataList.add(LayoutInflater.from(mContext).inflate(R.layout.pagercard, null));
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        mLayout = dataList.get(position);
        container.addView(mLayout);

        return mLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(dataList.get(position));
    }

}
