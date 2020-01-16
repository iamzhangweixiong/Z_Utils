package com.zhangwx.z_utils.Z_LifeCycle

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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