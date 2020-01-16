package com.zhangwx.z_utils.Z_ScreenFix

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhangwx.z_utils.R
import kotlinx.android.synthetic.main.activity_screen_fix.*

class ScreenFixActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_fix)

        density.text = "density = ${resources.displayMetrics.density}"
        densityDpi.text = "densityDpi = ${resources.displayMetrics.densityDpi}"
        scaledDensity.text = "scaledDensity = ${resources.displayMetrics.scaledDensity}"

        resources.displayMetrics.density = 3.0f
    }
}