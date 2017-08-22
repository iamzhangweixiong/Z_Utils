package com.zhangwx.z_utils.Z_UI;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zhangwx.z_utils.R;

/**
 * Created by zhangweixiong on 2017/8/1.
 */

public class ActionBarUIFixTool {

    public static void fixMutilSysBar(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            // 表现：6.0 以上机型状态栏透明，会根据我们背景颜色自动调节状态栏图标颜色，如白背景的图标为黑色

        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.statusbar_bg));
            // 表现：5.0 以上 6.0 以下机型状态栏不透明，有一层黑色有透明度的遮盖，故设置一下状态栏颜色
            // SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 使用这个属性时不会根据我们背景颜色自动调节状态栏图标颜色

        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 4.0 以上 5.0 以下状态栏透明
        }
    }

    public static void fixDialogFullScreen(Dialog dialog) {
        final Window window = dialog.getWindow();
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
