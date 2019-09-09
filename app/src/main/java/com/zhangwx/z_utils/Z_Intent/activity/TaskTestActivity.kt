package com.zhangwx.z_utils.Z_Intent.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zhangwx.z_utils.R
import kotlinx.android.synthetic.main.activity_task.*

class TaskTestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        startSelf.setOnClickListener {
            var intent = Intent(this, TaskTestActivity::class.java)
            this.startActivity(intent)
        }
    }
}