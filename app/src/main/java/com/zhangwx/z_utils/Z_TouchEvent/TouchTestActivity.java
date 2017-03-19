package com.zhangwx.z_utils.Z_TouchEvent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_UI.ViewUtils;
import com.zhangwx.z_utils.Z_Widget.PullOrFlingFrameLayout;

/**
 * Created by zhangwx on 2017/1/4.
 */
public class TouchTestActivity extends Activity {

    private PullOrFlingFrameLayout pullFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        pullFrameLayout = ViewUtils.$(this, R.id.pullFrameLayout);
        ViewUtils.$(this, R.id.touchTestBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CustomWidget.showCustomToast(getApplicationContext(), R.layout.toast_layout, R.id.message, "touchTestBtn", true);
                pullFrameLayout.setTranslationY(0);
            }
        });
    }
}
