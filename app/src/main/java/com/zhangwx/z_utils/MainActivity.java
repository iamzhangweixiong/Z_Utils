package com.zhangwx.z_utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.zhangwx.z_utils.Z_DB.DataBaseActivity;
import com.zhangwx.z_utils.Z_Intent.IntentActivity;
import com.zhangwx.z_utils.Z_ListView.ListViewActivity;
import com.zhangwx.z_utils.Z_RecycleView.RecycleActivity;
import com.zhangwx.z_utils.Z_Reflect.ReflectActivity;
import com.zhangwx.z_utils.Z_Thread.HandlerThreadActivity;
import com.zhangwx.z_utils.Z_TouchEvent.TouchTestActivity;
import com.zhangwx.z_utils.Z_UI.ViewUtils;
import com.zhangwx.z_utils.Z_ViewPager.ViewPagerActivity;
import com.zhangwx.z_utils.Z_Widget.PatternActivity;
import com.zhangwx.z_utils.Z_Window.WindowActivity;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ViewUtils.$(this, R.id.RecycleBtn).setOnClickListener(this);
        ViewUtils.$(this, R.id.ViewPagerBtn).setOnClickListener(this);
        ViewUtils.$(this, R.id.HandlerThreadBtn).setOnClickListener(this);
        ViewUtils.$(this, R.id.TouchTestBtn).setOnClickListener(this);
        ViewUtils.$(this, R.id.WindowTestBtn).setOnClickListener(this);
        ViewUtils.$(this, R.id.ReflectTestBtn).setOnClickListener(this);
        ViewUtils.$(this, R.id.IntentTestBtn).setOnClickListener(this);
        ViewUtils.$(this, R.id.DataBaseTestBtn).setOnClickListener(this);
        ViewUtils.$(this, R.id.ListTestBtn).setOnClickListener(this);
        ViewUtils.$(this, R.id.PasswordBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.RecycleBtn:
                intent.setClass(this, RecycleActivity.class);
                break;
            case R.id.ViewPagerBtn:
                intent.setClass(this, ViewPagerActivity.class);
                break;
            case R.id.HandlerThreadBtn:
                intent.setClass(this, HandlerThreadActivity.class);
                break;
            case R.id.TouchTestBtn:
                intent.setClass(this, TouchTestActivity.class);
                break;
            case R.id.WindowTestBtn:
                intent.setClass(this, WindowActivity.class);
                break;
            case R.id.ReflectTestBtn:
                intent.setClass(this, ReflectActivity.class);
                break;
            case R.id.IntentTestBtn:
                intent.setClass(this, IntentActivity.class);
                break;
            case R.id.DataBaseTestBtn:
                intent.setClass(this, DataBaseActivity.class);
                break;
            case R.id.ListTestBtn:
                intent.setClass(this, ListViewActivity.class);
                break;
            case R.id.PasswordBtn:
                intent.setClass(this, PatternActivity.class);
                break;
        }
        startActivity(intent);
    }
}
