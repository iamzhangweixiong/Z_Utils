package com.zhangwx.z_utils.Z_retrofit.loadfile

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

class ApkloadService {

    private var retrofit: Retrofit = Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

    fun loadApk() {
        val downloadService = retrofit.create(ApkloadInterface::class.java)
    }
}
