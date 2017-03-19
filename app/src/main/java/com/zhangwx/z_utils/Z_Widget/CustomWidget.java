package com.zhangwx.z_utils.Z_Widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zhangwx on 2016/12/22.
 */
public class CustomWidget {

    public static void showCustomToast(Context context, int layoutID, int textViewID, String text, boolean shortToast) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutID, null);
        TextView textView = (TextView) layout.findViewById(textViewID);
        textView.setText(text);
        Toast toast = new Toast(context);
        toast.setDuration(shortToast ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


}
