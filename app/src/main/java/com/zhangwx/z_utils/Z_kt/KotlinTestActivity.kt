package com.zhangwx.z_utils.Z_kt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhangwx.z_utils.R
import kotlinx.android.synthetic.main.activity_kotlin.*

class KotlinTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        kotlinxTestView.setOnClickListener { }
    }
}