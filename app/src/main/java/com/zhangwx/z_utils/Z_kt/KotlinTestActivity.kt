package com.zhangwx.z_utils.Z_kt

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zhangwx.z_utils.R
import kotlinx.android.synthetic.main.activity_kotlin.*
import java.io.File

class KotlinTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        kotlinxTestView.setOnClickListener {
            File("/proc/net/xt_qtaguid/stats").useLines {
                it.forEach {
                    Log.e("zhang", it)
                }
            }
        }
    }
}