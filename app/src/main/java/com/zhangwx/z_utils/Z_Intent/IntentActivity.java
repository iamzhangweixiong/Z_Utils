package com.zhangwx.z_utils.Z_Intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_UI.ViewUtils;

/**
 * Created by zhangwx on 2017/4/17.
 */

public class IntentActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);
        ViewUtils.$(this, R.id.Jump_setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Jump_setting:
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);//在其他应用上层显示
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);//跳转应用详情页
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
                Intent intent = getNotificationServiceSettingIntent();//跳转通知栏权限页
                startActivity(intent);
                break;
        }
    }

    public Intent getNotificationServiceSettingIntent() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= 18) {
            intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        } else {
            intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}
