package com.zhangwx.z_utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.zhangwx.z_utils.Z_AIDL.AidlClientActivity
import com.zhangwx.z_utils.Z_Anima.AnimationTestActivity
import com.zhangwx.z_utils.Z_Anima.MatrixTestActivity
import com.zhangwx.z_utils.Z_Cache.CacheActivity
import com.zhangwx.z_utils.Z_Components.ComponentsActivity
import com.zhangwx.z_utils.Z_DB.DataBaseActivity
import com.zhangwx.z_utils.Z_EventBus.EventBusActivity
import com.zhangwx.z_utils.Z_Intent.IntentActivity
import com.zhangwx.z_utils.Z_LifeCycle.LifeCycleActivity
import com.zhangwx.z_utils.Z_ListView.ListViewActivity
import com.zhangwx.z_utils.Z_Processor.ProcessorActivity
import com.zhangwx.z_utils.Z_RecycleView.RecycleActivity
import com.zhangwx.z_utils.Z_Reflect.ReflectActivity
import com.zhangwx.z_utils.Z_Regex.RegexTestActivity
import com.zhangwx.z_utils.Z_Rx.RxTestActivity
import com.zhangwx.z_utils.Z_ScreenFix.ScreenFixActivity
import com.zhangwx.z_utils.Z_Spi.SpiTestActivity
import com.zhangwx.z_utils.Z_Thread.HandlerThreadActivity
import com.zhangwx.z_utils.Z_TouchEvent.TouchTestActivity
import com.zhangwx.z_utils.Z_ViewPager.ViewPagerActivity
import com.zhangwx.z_utils.Z_Widget.WidgetTestActivity
import com.zhangwx.z_utils.Z_Window.WindowActivity
import com.zhangwx.z_utils.Z_coroutines.CoroutinesActivity
import com.zhangwx.z_utils.Z_kt.KotlinTestActivity
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
        ProcessorBtn.setOnClickListener(this)
        ComponentsBtn.setOnClickListener(this)
        LifeCycleBtn.setOnClickListener(this)
        CacheBtn.setOnClickListener(this)
        EventBusBtn.setOnClickListener(this)
        ScreenFixBtn.setOnClickListener(this)
        AidlBtn.setOnClickListener(this)
        AnimationBtn.setOnClickListener(this)
        SpiBtn.setOnClickListener(this)
        MatrixBtn.setOnClickListener(this)
        CoroutinesBtn.setOnClickListener(this)
        KotlinBtn.setOnClickListener(this)
        RegexTestBtn.setOnClickListener(this)
        RxTestBtn.setOnClickListener(this)
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
            R.id.PatternBtn -> intent.setClass(this, WidgetTestActivity::class.java)
            R.id.RetrofitBtn -> intent.setClass(this, RetrofitTestActivity::class.java)
            R.id.ProcessorBtn -> intent.setClass(this, ProcessorActivity::class.java)
            R.id.ComponentsBtn -> intent.setClass(this, ComponentsActivity::class.java)
            R.id.LifeCycleBtn -> intent.setClass(this, LifeCycleActivity::class.java)
            R.id.CacheBtn -> intent.setClass(this, CacheActivity::class.java)
            R.id.EventBusBtn -> intent.setClass(this, EventBusActivity::class.java)
            R.id.ScreenFixBtn -> intent.setClass(this, ScreenFixActivity::class.java)
            R.id.AidlBtn -> intent.setClass(this, AidlClientActivity::class.java)
            R.id.AnimationBtn -> intent.setClass(this, AnimationTestActivity::class.java)
            R.id.MatrixBtn -> intent.setClass(this, MatrixTestActivity::class.java)
            R.id.SpiBtn -> intent.setClass(this, SpiTestActivity::class.java)
            R.id.CoroutinesBtn -> intent.setClass(this, CoroutinesActivity::class.java)
            R.id.KotlinBtn -> intent.setClass(this, KotlinTestActivity::class.java)
            R.id.RegexTestBtn -> intent.setClass(this, RegexTestActivity::class.java)
            R.id.RxTestBtn -> intent.setClass(this, RxTestActivity::class.java)
        }
        startActivity(intent)
    }
}
