package com.zhangwx.z_utils.Z_Intent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);//跳转应用详情页
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                break;
        }
    }
}
