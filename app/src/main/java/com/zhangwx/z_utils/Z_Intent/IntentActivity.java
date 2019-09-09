package com.zhangwx.z_utils.Z_Intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_Intent.activity.TaskTestActivity;
import com.zhangwx.z_utils.Z_UI.ViewUtils;

/**
 * Created by zhangwx on 2017/4/17.
 */

public class IntentActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "IntentActivity";
    public static final int REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);
        ViewUtils.$(this, R.id.Jump_setting).setOnClickListener(this);
        ViewUtils.$(this, R.id.notification).setOnClickListener(this);
        ViewUtils.$(this, R.id.task).setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: isEnabled = " + NotificationHelper.isEnabled(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: isEnabled = " + NotificationHelper.isEnabled(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: isEnabled = " + NotificationHelper.isEnabled(this));
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

                new Handler().postDelayed(() -> {
                    Intent intent1 = new Intent(getApplication(), NotificationActivity.class);
                    startActivity(intent1);
                }, 500);

                break;
            case R.id.notification:
                NotificationHelper.showNotification(this);
                break;
            case R.id.task:
                Intent intent1 = new Intent(this, TaskTestActivity.class);
                startActivityForResult(intent1, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public Intent getNotificationServiceSettingIntent() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        } else {
            intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}
