package com.zhangwx.z_utils.Z_RecycleView.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhangwx.z_utils.R;


/**
 * item 背景的设置和分割线的显示有很大关系，建议 item 和 RecycleView 背景一致
 */
public class ListDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private Context mContext;

    public ListDecoration(Context context) {
        mContext = context;
        mDivider = ContextCompat.getDrawable(context, R.drawable.listdecoration);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        画一条线
//        if (parent.getItemAnimator() != null && parent.getItemAnimator().isRunning()) {
//            return;
//        }
//        int left = DimenUtils.dp2px(10);
//        int right = parent.getWidth() - left;
//
//        int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = parent.getChildAt(i);
//            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//            int top = child.getBottom() + params.bottomMargin;
//            int bottom = top + mDivider.getIntrinsicHeight();
//            mDivider.setBounds(left, top, right, bottom);
//            mDivider.draw(c);
//        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        int position = parent.getChildAdapterPosition(view);
//        int itemCount = parent.getAdapter().getItemCount();
//        if (position == 0 || position == itemCount - 1) { // 第一个和最后一个 item 不显示分割线
//            outRect.set(0, 0, 0, 60);
//        } else {
            outRect.set(20, 20, 20, 20);
//            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
//        }
    }
}

