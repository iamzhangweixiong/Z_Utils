package com.zhangwx.z_utils.Z_Processor

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zhangwx.z_utils.R
import kotlinx.android.synthetic.main.activity_processer.*

class ProcessorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processer)
        processorBtn.setOnClickListener {
            textBox.text = getFreeMem(this).toString()
        }
    }

    private fun getFreeMem(context: Context): Long {
        val manager = context.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(info)
        // 单位Byte
        return info.availMem
    }

}