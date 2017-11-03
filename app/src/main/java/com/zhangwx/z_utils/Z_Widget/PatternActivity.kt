package com.zhangwx.z_utils.Z_Widget

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.zhangwx.z_utils.R
import com.zhangwx.z_utils.Z_Widget.password.LockPatternView

/**
 * Created by zhangweixiong on 2017/11/3.
 */

class PatternActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val patternView = findViewById(R.id.patternView) as LockPatternView
        patternView.setOnPatternListener(object : LockPatternView.OnPatternListener {
            override fun onPatternStart() {}

            override fun onPatternCleared() {}

            override fun onPatternCellAdded(pattern: MutableList<LockPatternView.Cell>?) {}

            override fun onPatternDetected(pattern: MutableList<LockPatternView.Cell>?) {
                if (pattern!!.size < 4) {
                    patternView.clearPattern()

                    Toast.makeText(this@PatternActivity, "must be > 4", Toast.LENGTH_SHORT).show()
                    return
                }
                val password = LockPatternView.patternToString(pattern)
                Toast.makeText(this@PatternActivity, password, Toast.LENGTH_SHORT).show()

            }
        })


    }
}

