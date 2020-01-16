package com.zhangwx.z_utils.Z_LifeCycle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LcViewModel: ViewModel() {
    val testString by lazy { MutableLiveData<String>() }

    fun setString() {
        testString.value = "test"
    }

    override fun onCleared() {
        super.onCleared()

    }
}

