package com.zhangwx.z_utils.Z_RecycleView.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangwx.z_utils.R;

import com.zhangwx.z_utils.Z_RecycleView.holder.CardHolder;

/**
 * Created by Administrator on 2016/12/15.
 */
public class SimpleHeaderWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int STYLE_HEADER = Integer.MAX_VALUE - 1;

    private RecyclerView.Adapter mInnerAdapter;

    public SimpleHeaderWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == STYLE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            CardHolder holder = new CardHolder(parent.getContext(), view);
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position))
            return STYLE_HEADER;
        return mInnerAdapter.getItemViewType(position - getHeaderCount());
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeaderCount());
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + getHeaderCount();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
    }

    private boolean isHeaderPosition(int position) {
        return position < 1;
    }

    public int getHeaderCount() {
        return 1;
    }
}
