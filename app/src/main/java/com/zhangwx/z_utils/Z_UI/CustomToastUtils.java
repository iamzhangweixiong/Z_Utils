package com.zhangwx.z_utils.Z_UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zhangwx on 2016/12/17.
 */
public class CustomToastUtils {

    public static void showCustomToast(Context context, int layoutId, int textViewId, int textId) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutId, null);
        TextView textView = (TextView) layout.findViewById(textViewId);
        textView.setText(context.getResources().getText(textId));
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
