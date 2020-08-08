package com.zhangwx.z_utils.Z_Cache

import android.app.Activity
import android.os.Bundle
import com.google.gson.Gson
import com.zhangwx.z_utils.R
import com.zhangwx.z_utils.Z_Cache.preference.PreferenceTestModel
import com.zhangwx.z_utils.Z_Cache.preference.PreferenceUtils
import kotlinx.android.synthetic.main.activity_cache.*
import kotlin.random.Random

class CacheActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cache)

        val myDiskLruCache = MyDiskLruCache()

        ActionPut.setOnClickListener {
            myDiskLruCache.putValue()
        }

        ActionGet.setOnClickListener {
            content.text = myDiskLruCache.jFile
        }

        PreferenceSet.setOnClickListener {
            val testModel = PreferenceTestModel().apply {
                age = Random(10).nextInt()
                name = "wilson $age"
            }
            PreferenceUtils.setStringValue("wilson", Gson().toJson(testModel))
        }

        PreferenceGet.setOnClickListener {
            val testModel = Gson().fromJson(PreferenceUtils.getStringValue("wilson", ""), PreferenceTestModel::class.java)
            content.text = testModel.name + "    " + testModel.age
        }
    }
}