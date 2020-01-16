package com.zhangwx.z_utils.Z_Bitmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.zhangwx.z_utils.R
import kotlinx.android.synthetic.main.activity_bitmap.*

class BitmapTestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap)
        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView)
    }
}