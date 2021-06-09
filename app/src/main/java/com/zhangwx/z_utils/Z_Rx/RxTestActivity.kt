package com.zhangwx.z_utils.Z_Rx

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zhangwx.z_utils.R
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_rx.*

class RxTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx)

        single.setOnClickListener {
            testSingle()
        }

        observable.setOnClickListener {
            testObservable()
        }

        observableJust.setOnClickListener {
            testObservableJust()
        }

        observableDoOnError.setOnClickListener {
            testObservableDoOnError()
        }
    }

    // Single 是特殊的
    // 1. 数据源 getTestData 发生崩溃 catch 不住，onError 也不走，得包一个 try-catch
    // 2. onNext 中发生崩溃，如果有 setErrorHandler 则会被 ErrorHandler 捕获，onError 也不走， try-catch 也不行
    @SuppressLint("CheckResult")
    private fun testSingle() {
        RxJavaPlugins.setErrorHandler {
            RuntimeException("RxJava ErrorHandler", it).printStackTrace()
        }
        try {
            Single.just(getTestData())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        rx_result.text = it.name
                        throw NullPointerException("subscribe...")
                    }, {
                        RuntimeException("testSingle onError", it).printStackTrace()
                    })
        } catch (ex: Exception) {
            RuntimeException("testSingle catch", ex).printStackTrace()
        }
    }

    private fun getTestData(): TestData {
        val testString = TestData("RxTestActivity", 12)
//        throw RuntimeException("getTestData...")
        return testString
    }


    // 1. 数据源 getTestData 发生崩溃，ErrorHandler 不管有没有设置，都走 onError
    // 2. onNext 中发生崩溃，ErrorHandler 不管有没有设置，都走 onError
    // 3. onError 如果没有处理实现，则会走 ErrorHandler
    // 4. ErrorHandler 如果没有，onError 也没有实现则必崩，只能包一个 try-catch 解决
    @SuppressLint("CheckResult")
    private fun testObservable() {
        val testString = TestData("RxTestActivity", 12)

//        RxJavaPlugins.setErrorHandler {
//            RuntimeException("RxJava ErrorHandler", it).printStackTrace()
//        }
        try {
            Observable.fromCallable {
//                throw RuntimeException("just...")
                testString
            }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        rx_result.text = it.name
                        throw NullPointerException("subscribe...")
                    }, {
                        RuntimeException("testObservable onError", it).printStackTrace()
                    })
        } catch (ex: Exception) {
            RuntimeException("testObservable try-catch", ex).printStackTrace()
        }
    }

    @SuppressLint("CheckResult")
    private fun testObservableDoOnError() {
        val testString = TestData("RxTestActivity", 12)

//        RxJavaPlugins.setErrorHandler {
//            RuntimeException("RxJava ErrorHandler", it).printStackTrace()
//        }
//        try {
            Observable.fromCallable {
                throw RuntimeException("just...")
                testString
            }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Log.e(TAG,"testObservableDoOnError doOnError")
                    }
                    .doOnComplete {

                    }
                    .subscribe({
                        rx_result.text = it.name
//                        throw RuntimeException("subscribe...")
                    }, {
                        RuntimeException("testObservableDoOnError onError", it).printStackTrace()
                    })
//        } catch (ex: Exception) {
//            RuntimeException("testObservableDoOnError try-catch", ex).printStackTrace()
//        }
    }

    @SuppressLint("CheckResult")
    private fun testObservableJust() {
        val testString = TestData("RxTestActivity", 12)

        RxJavaPlugins.setErrorHandler {
            RuntimeException("RxJava ErrorHandler", it).printStackTrace()
        }

        Observable.just {
            throw RuntimeException("just...")
            testString
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    rx_result.text = it.invoke().name
                    throw NullPointerException("subscribe...")
                }, {
                    RuntimeException("testObservableJust onError", it).printStackTrace()
                })
    }

    data class TestData(val name: String, val age: Int)

    companion object {
        private const val TAG = "RxTestActivity"
    }
}