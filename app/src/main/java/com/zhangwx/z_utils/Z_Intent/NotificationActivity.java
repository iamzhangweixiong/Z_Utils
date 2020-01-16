package com.zhangwx.z_utils.Z_Intent;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_Intent.UI.AccessGuideAnimHelper;
import com.zhangwx.z_utils.Z_Intent.UI.TickView;
import com.zhangwx.z_utils.Z_Intent.UI.ToggleView;

/**
 * Created by Administrator on 2017/6/14.
 */

public class NotificationActivity extends Activity {

    private boolean mShowTick = true;
    AccessGuideAnimHelper mAnimHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.setting_show_anim, R.anim.setting_hide_anim);
        setContentView(R.layout.notify_access_guide);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setDimAmount(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mShowTick = false;
        }
        initView();
    }

    private void initView() {
        final View fingerView = findViewById(R.id.finger);
        final ToggleView toggleView = (ToggleView) findViewById(R.id.toggleView);
        final TickView tickView = (TickView) findViewById(R.id.tick);
        if (mShowTick) {
            tickView.setVisibility(View.VISIBLE);
        } else {
            toggleView.setVisibility(View.VISIBLE);
        }
        mAnimHelper = new AccessGuideAnimHelper(mShowTick);
        mAnimHelper.setTargetViews(fingerView, tickView, toggleView);
        mAnimHelper.startGuideAnim();

        findViewById(R.id.guide_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimHelper.clear();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAnimHelper.clear();
        finish();
    }
}
