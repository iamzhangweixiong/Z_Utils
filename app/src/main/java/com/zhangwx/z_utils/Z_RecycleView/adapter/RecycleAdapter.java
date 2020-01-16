package com.zhangwx.z_utils.Z_RecycleView.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangwx.z_utils.MyApplication;
import com.zhangwx.z_utils.R;

import java.util.ArrayList;
import java.util.List;

import com.zhangwx.z_utils.Z_RecycleView.data.ListData;
import com.zhangwx.z_utils.Z_RecycleView.holder.CardHolder;

/**
 * Created by zhangwx on 2016/12/10.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ListData> feedData = new ArrayList<ListData>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclecard, parent, false);
        CardHolder cardHolder = new CardHolder(parent.getContext(), view);
        return cardHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CardHolder && feedData.size() > 0) {
            ((CardHolder) holder).setText(R.id.recycle_text, Html.fromHtml(MyApplication.getContext().getString(R.string.recy)).toString());
            ((CardHolder) holder).setImageDrawable(R.id.recycle_pic, feedData.get(position).getDrawable());
        }
    }

    @Override
    public int getItemCount() {
        return feedData.size();
    }

    public void setFeedData(List<ListData> data) {
        feedData.clear();
        feedData.addAll(data);
    }

    public void addFeedData(List<ListData> data) {
        feedData.addAll(data);
    }

    public void addFeedDataToTop(List<ListData> data) {
        feedData.addAll(0, data);
    }
}
