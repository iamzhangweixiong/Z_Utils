package com.zhangwx.z_utils.Z_Window;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_UI.ViewUtils;

/**
 * Created by zhangwx on 2017/1/20.
 */
public class WindowActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        ViewUtils.$(this, R.id.start).setOnClickListener(this);
        ViewUtils.$(this, R.id.stop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WindowService.class);
        switch (v.getId()) {
            case R.id.start:
                startService(intent);
                break;
            case R.id.stop:
                stopService(intent);
                break;
        }
    }
}
