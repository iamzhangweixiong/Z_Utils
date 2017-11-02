package com.zhangwx.z_utils.Z_ListView.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangwx.z_utils.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangweixiong on 2017/10/15.
 */

public class TypeOneHolder {

    @BindView(R.id.lv_text_1)
    TextView mTitle;
    @BindView(R.id.lv_image_1)
    ImageView mImage;

    public TypeOneHolder(View view) {
        ButterKnife.bind(this,view);
    }

    public ImageView getImage() {
        return mImage;
    }

    public TextView getTitle() {
        return mTitle;
    }

    static View getView(Context context, View convertView, ViewGroup parent) {
        TypeOneHolder typeOneHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_layout_1, parent, false);
            typeOneHolder = new TypeOneHolder(convertView);
            convertView.setTag(typeOneHolder);
        } else {
            typeOneHolder = (TypeOneHolder) convertView.getTag();
        }


        return convertView;
    }
}
