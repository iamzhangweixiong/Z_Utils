package com.zhangwx.z_utils.Z_LifeCycle

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zhangwx.z_utils.R

class LifeCycleActivity: AppCompatActivity() {

    private lateinit var viewModel: LcViewModel
    private val observerTest by lazy { LifeCycleObserverTest() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lifecycle)

        viewModel = ViewModelProviders.of(this)[LcViewModel::class.java]
        viewModel.testString.observe(this, Observer { string ->
            print(string)
        })

        lifecycle.addObserver(observerTest)
    }
}