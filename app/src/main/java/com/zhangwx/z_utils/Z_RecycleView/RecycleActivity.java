package com.zhangwx.z_utils.Z_RecycleView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_RecycleView.adapter.ListDecoration;
import com.zhangwx.z_utils.Z_RecycleView.adapter.RecycleAdapter;
import com.zhangwx.z_utils.Z_RecycleView.adapter.SimpleHeaderWrapper;
import com.zhangwx.z_utils.Z_RecycleView.adapter.SimpleLoadMoreWrapper;
import com.zhangwx.z_utils.Z_RecycleView.data.DataService;
import com.zhangwx.z_utils.Z_UI.ViewUtils;

/**
 * Created by zhangwx on 2016/12/21.
 */
public class RecycleActivity extends Activity {

    private Handler mhandler;
    private DataService mDataService;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshItem;

    private RecycleAdapter mRecycleAdapter;
    private SimpleHeaderWrapper mSimpleHeaderWrapper;
    private SimpleLoadMoreWrapper mSimpleLoadMoreWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        mDataService = new DataService();
        mhandler = new Handler(Looper.getMainLooper());
        mRecyclerView = ViewUtils.$(this, R.id.recycleView);
        mRecycleAdapter = new RecycleAdapter();
        mSimpleHeaderWrapper = new SimpleHeaderWrapper(mRecycleAdapter);
        mSimpleLoadMoreWrapper = new SimpleLoadMoreWrapper(mSimpleHeaderWrapper);
        mRecyclerView.setAdapter(mSimpleLoadMoreWrapper);
        mSimpleLoadMoreWrapper.setOnLoadMoreListener(() -> {
            mRecycleAdapter.addFeedData(mDataService.getData());
            notifyDataChange();
        });
        mRecyclerView.addItemDecoration(new ListDecoration(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));
        mSwipeRefreshItem = ViewUtils.$(this, R.id.swipeRefreshItem);
        mSwipeRefreshItem.setOnRefreshListener(() -> {
            mSwipeRefreshItem.setRefreshing(true);
            mRecycleAdapter.addFeedDataToTop(mDataService.getData());
            notifyDataChange();
            mSwipeRefreshItem.setRefreshing(false);
        });
    }

    private void notifyDataChange() {
        mhandler.postDelayed(() -> mSimpleLoadMoreWrapper.notifyDataSetChanged(), 100);
    }

}
