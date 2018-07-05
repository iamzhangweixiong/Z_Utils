package com.zhangwx.z_utils.Z_retrofit.loadfile

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class ApkloadService {

    private var retrofit: Retrofit = Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    fun loadApk() {
        val downloadService = retrofit.create(ApkloadInterface::class.java)
    }
}
