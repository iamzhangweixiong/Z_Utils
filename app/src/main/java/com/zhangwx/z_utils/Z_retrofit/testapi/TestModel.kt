package com.zhangwx.z_utils.Z_retrofit.testapi

import android.text.TextUtils
import android.util.Log
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

class TestModel {

    val manager = createX509TrustManager()!!
    val factory = createSSLSocketFactory(manager)
    private var okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(factory, manager)
            .build()
    private var retrofit: Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://haveibeenpwned.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

    fun testUrl(url: String, callBack: (Boolean) -> Unit) {
        val _disposable = retrofit.create(TestInterface::class.java)
                .testapi(url)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.e("response", it.string())
                    callBack(true)
                }, {
                    callBack(false)
                })
    }

    fun createX509TrustManager(): X509TrustManager? {
        try {
            return object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun createSSLSocketFactory(trustManager: TrustManager): SSLSocketFactory {
        try {
            val context = SSLContext.getInstance("TLS")
            context.init(null, arrayOf(trustManager), null)
            return context.socketFactory
        } catch (e: Exception) {
            throw AssertionError(e)
        }

    }

    fun createInsecureHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { hostName, sslSession ->
            if (TextUtils.isEmpty(hostName)) {
                return@HostnameVerifier false
            }
            !Arrays.asList(*VERIFY_HOST_NAME_ARRAY).contains(hostName)
        }
    }

    private val VERIFY_HOST_NAME_ARRAY = arrayOf<String>()
}