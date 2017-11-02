package com.zhangwx.z_utils.Z_ListView;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.zhangwx.z_utils.MyApplication;
import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_ListView.data.DataModel;
import com.zhangwx.z_utils.Z_ListView.data.DataSource;
import com.zhangwx.z_utils.Z_ListView.holder.TypeOneHolder;
import com.zhangwx.z_utils.Z_RecycleView.data.DataService;
import com.zhangwx.z_utils.Z_RecycleView.data.ListData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangweixiong
 * on 2017/10/15.
 */

class ListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<DataModel> mData = new ArrayList<>();

    ListViewAdapter(Context context) {
        mContext = context;
        mData.addAll(DataSource.getData());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        int type = getItemViewType(position);
//        if (type == DataModel.TYPE_ONE) {
//
//        } else if (type == DataModel.TYPE_TWO) {
//
//        } else {
//
//        }
//        DataModel dataModel = mData.get(position);

        TypeOneHolder typeOneHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_layout_1, parent, false);
            typeOneHolder = new TypeOneHolder(convertView);
            convertView.setTag(typeOneHolder);
        } else {
            typeOneHolder = (TypeOneHolder) convertView.getTag();
        }
//        String allText = MyApplication.getContext().getString(R.string.sign_in_guide_text);
//        String hightlightText = MyApplication.getContext().getString(R.string.sign_in_guide_text_click);
//        SpannableString spannableString = new SpannableString(allText);
//        int index = allText.indexOf(hightlightText);
//        spannableString.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                Toast.makeText(mContext, "toast", Toast.LENGTH_SHORT).show();
//            }
//        }, index, index + hightlightText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new ForegroundColorSpan(MyApplication.getContext().getResources().getColor(R.color.colorPrimary)), index, index + hightlightText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new BackgroundColorSpan(MyApplication.getContext().getResources().getColor(R.color.statusbar_bg)), index, index + hightlightText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        typeOneHolder.getTitle().setText(spannableString);
//        MovementMethod m = typeOneHolder.getTitle().getMovementMethod();
//        if ((m == null) || !(m instanceof LinkMovementMethod)) {
//            typeOneHolder.getTitle().setMovementMethod(LinkMovementMethod.getInstance());
//        }
//        typeOneHolder.getTitle().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "onClick", Toast.LENGTH_SHORT).show();
//            }
//        });

//        String PATTERN = "<[^>]*h>";
        final String PATTERN = "<h>(.+?)</h>";
        String text = "You can also earn more credit on the \"Wheel of Fortune\" in the \"Credits\" page!\n" +
                "    <h>Tap here to get 1,000 credits</h>";

        Pattern p = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(text);
        matcher.find();
        String result  = matcher.group(1);
        String result2 = matcher.replaceAll("");
        typeOneHolder.getTitle().setText(result2);
        return convertView;
    }
}
