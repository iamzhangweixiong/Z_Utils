package com.zhangwx.z_utils.Z_Processor

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Debug
import androidx.appcompat.app.AppCompatActivity
import com.zhangwx.z_utils.R
import kotlinx.android.synthetic.main.activity_processer.*
import androidx.core.content.ContextCompat.getSystemService

class ProcessorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processer)
        processorBtn.setOnClickListener {
            textBox.text = getFreeMem(this)
        }
    }

    private fun getFreeMem(context: Context): String {

        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val memoryInfo = Debug.MemoryInfo()
        Debug.getMemoryInfo(memoryInfo)

        val runtime = Runtime.getRuntime()

        return "max: " + runtime.maxMemory() / 1024 / 1024 +
                "\ntotal: " + runtime.totalMemory() / 1024 / 1024 +
                "\nfree: " + runtime.freeMemory() / 1024 / 1024 +
                "\ndalvikPss: " + memoryInfo.dalvikPss / 1024 +
                "\nnativePss: " + memoryInfo.nativePss / 1024 +
                "\notherPss: " + memoryInfo.otherPss / 1024 +
                "\nmemoryClass: " + am.memoryClass +
                "\nlargeMemoryClass: " + am.largeMemoryClass
    }
}