package com.zhangwx.z_utils.Z_coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhangwx.z_utils.R
import kotlinx.android.synthetic.main.activity_coroutines.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CoroutinesActivity : AppCompatActivity() {
    private lateinit var flow: Flow<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines)
        flow = flow {
            (0..10).forEach {
                delay(500)
                emit(it)
            }
        }.map {
            it * it
        }.flowOn(Dispatchers.Default)

        one.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                flow.collect {
                    text.text = it.toString()
                }
            }
        }
    }
}