package com.zhangwx.z_utils.Z_Widget

import android.os.Bundle
import android.os.StatFs
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.zhangwx.z_utils.R
import com.zhangwx.z_utils.Z_Widget.password.LockPatternView
import kotlinx.android.synthetic.main.activity_password.*

/**
 * Created by zhangweixiong on 2017/11/3.
 */

class WidgetTestActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        initView()

    }

    private fun initView() {
//        patternView.setOnPatternListener(object : LockPatternView.OnPatternListener {
//            override fun onPatternStart() {}
//
//            override fun onPatternCleared() {}
//
//            override fun onPatternCellAdded(pattern: MutableList<LockPatternView.Cell>?) {}
//
//            override fun onPatternDetected(pattern: MutableList<LockPatternView.Cell>?) {
//                val length = pattern?.size ?: return
//                if (length < 4) {
//                    patternView.drawError()
//                    Toast.makeText(this@WidgetTestActivity, "must be > 4", Toast.LENGTH_SHORT).show()
//                    return
//                }
//                val password = LockPatternView.patternToString(pattern)
//                Toast.makeText(this@WidgetTestActivity, password, Toast.LENGTH_SHORT).show()
//            }
//        })



    }

}

