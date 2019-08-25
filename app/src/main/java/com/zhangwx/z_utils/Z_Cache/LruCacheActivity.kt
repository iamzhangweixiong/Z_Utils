package com.zhangwx.z_utils.Z_Cache

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.zhangwx.z_utils.R
import kotlinx.android.synthetic.main.activity_cache.*

class LruCacheActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cache)

        val myDiskLruCache = MyDiskLruCache()

        ActionPut.setOnClickListener {
            myDiskLruCache.putValue()
        }

        ActionGet.setOnClickListener {
            Toast.makeText(this, myDiskLruCache.value, Toast.LENGTH_SHORT).show()
            content.text = myDiskLruCache.jFile
        }
    }
}