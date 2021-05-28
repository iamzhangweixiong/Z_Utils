package com.zhangwx.z_utils.Z_kt

import android.util.Log

object ForeachTest {

    private val testList = mutableListOf("aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg")

    fun foreachOne(block: (String) -> Unit): Unit {
        var string = ""
        testList.forEach {
            block.invoke(it)
            string += it
        }
        Log.e("zhang", string)
    }
}