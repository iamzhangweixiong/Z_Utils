package com.zhangwx.z_utils.Z_Components

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zhangwx.z_utils.R
import com.zhangwx.z_utils.Z_RecycleView.RecycleActivity
import kotlinx.android.synthetic.main.activity_components.*;

class ComponentsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_components)

        JumpActivity.setOnClickListener {
            val intent = Intent(this, ComponentsActivity::class.java)
            startActivity(intent)
        }

        JumpService.setOnClickListener {
            val intent = Intent(this, RecycleActivity::class.java)
            startActivity(intent)
        }

    }
}
