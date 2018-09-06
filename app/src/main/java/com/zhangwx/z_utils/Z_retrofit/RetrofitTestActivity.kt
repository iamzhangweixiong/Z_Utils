package com.zhangwx.z_utils.Z_retrofit

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.Bundle
import com.zhangwx.z_utils.R
import com.zhangwx.z_utils.Z_Permission.EasyPermissions
import com.zhangwx.z_utils.Z_Permission.PermissionUtils
import com.zhangwx.z_utils.Z_retrofit.loadfile.ApkLoadService
import kotlinx.android.synthetic.main.activity_retrofit.*

class RetrofitTestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)

        loadApk.setOnClickListener {
//            startService(Intent(this, ApkLoadService::class.java))
        }
    }
}