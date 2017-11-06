package com.zhangwx.z_utils.Z_Widget

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.zhangwx.z_utils.R
import com.zhangwx.z_utils.Z_Widget.password.LockPatternView

/**
 * Created by zhangweixiong on 2017/11/3.
 */

class PatternActivity : FragmentActivity() {

    private var mPatternView: LockPatternView? = null
    private var mLockImage: ImageView? = null
    private var mLockTips: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        initView()
    }

    private fun initView() {
        mLockImage = findViewById(R.id.lock_img) as ImageView
        mLockTips = findViewById(R.id.lock_tips) as TextView
        mPatternView = findViewById(R.id.patternView) as LockPatternView

        mPatternView?.setOnPatternListener(object : LockPatternView.OnPatternListener {
            override fun onPatternStart() {}

            override fun onPatternCleared() {}

            override fun onPatternCellAdded(pattern: MutableList<LockPatternView.Cell>?) {}

            override fun onPatternDetected(pattern: MutableList<LockPatternView.Cell>?) {
                val length = pattern?.size ?: return
                if (length < 4) {
                    mPatternView?.drawError()
                    Toast.makeText(this@PatternActivity, "must be > 4", Toast.LENGTH_SHORT).show()
                    return
                }
                val password = LockPatternView.patternToString(pattern)
                Toast.makeText(this@PatternActivity, password, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun changeLockView() {

    }
}

