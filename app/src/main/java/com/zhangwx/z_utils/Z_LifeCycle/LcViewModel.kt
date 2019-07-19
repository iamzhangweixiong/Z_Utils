package com.zhangwx.z_utils.Z_LifeCycle

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class LcViewModel: ViewModel() {
    val testString by lazy { MutableLiveData<String>() }

    fun setString() {
        testString.value = "test"
    }

    override fun onCleared() {
        super.onCleared()

    }
}

