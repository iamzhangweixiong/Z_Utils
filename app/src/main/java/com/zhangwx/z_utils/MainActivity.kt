package com.zhangwx.z_utils

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.zhangwx.z_utils.Z_DB.DataBaseActivity
import com.zhangwx.z_utils.Z_Intent.IntentActivity
import com.zhangwx.z_utils.Z_ListView.ListViewActivity
import com.zhangwx.z_utils.Z_RecycleView.RecycleActivity
import com.zhangwx.z_utils.Z_Reflect.ReflectActivity
import com.zhangwx.z_utils.Z_Thread.HandlerThreadActivity
import com.zhangwx.z_utils.Z_TouchEvent.TouchTestActivity
import com.zhangwx.z_utils.Z_ViewPager.ViewPagerActivity
import com.zhangwx.z_utils.Z_Widget.PatternActivity
import com.zhangwx.z_utils.Z_Window.WindowActivity
import com.zhangwx.z_utils.Z_retrofit.RetrofitTestActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RecycleBtn.setOnClickListener(this)
        ViewPagerBtn.setOnClickListener(this)
        HandlerThreadBtn.setOnClickListener(this)
        TouchTestBtn.setOnClickListener(this)
        WindowTestBtn.setOnClickListener(this)
        ReflectTestBtn.setOnClickListener(this)
        IntentTestBtn.setOnClickListener(this)
        DataBaseTestBtn.setOnClickListener(this)
        ListTestBtn.setOnClickListener(this)
        PatternBtn.setOnClickListener(this)
        RetrofitBtn.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val intent = Intent()
        when (view.id) {
            R.id.RecycleBtn -> intent.setClass(this, RecycleActivity::class.java)
            R.id.ViewPagerBtn -> intent.setClass(this, ViewPagerActivity::class.java)
            R.id.HandlerThreadBtn -> intent.setClass(this, HandlerThreadActivity::class.java)
            R.id.TouchTestBtn -> intent.setClass(this, TouchTestActivity::class.java)
            R.id.WindowTestBtn -> intent.setClass(this, WindowActivity::class.java)
            R.id.ReflectTestBtn -> intent.setClass(this, ReflectActivity::class.java)
            R.id.IntentTestBtn -> intent.setClass(this, IntentActivity::class.java)
            R.id.DataBaseTestBtn -> intent.setClass(this, DataBaseActivity::class.java)
            R.id.ListTestBtn -> intent.setClass(this, ListViewActivity::class.java)
            R.id.PatternBtn -> intent.setClass(this, PatternActivity::class.java)
            R.id.RetrofitBtn -> intent.setClass(this, RetrofitTestActivity::class.java)
        }
        startActivity(intent)
    }
}
