package com.zhangwx.z_utils.Z_retrofit.loadfile

import android.app.IntentService
import android.content.Intent
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import com.zhangwx.z_utils.Z_retrofit.DownLoadInfo
import io.reactivex.schedulers.Schedulers
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import com.zhangwx.z_utils.R
import com.zhangwx.z_utils.Z_retrofit.DownLoadProgressListener

class ApkLoadService : IntentService("DownLoadApk") {

    private var totalFileSize = 0
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    override fun onHandleIntent(intent: Intent?) {

        Log.e("zhang", "start load")

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.abc_ic_clear_material)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true)
        notificationManager.notify(0, notificationBuilder.build())
        loadApk()
    }

    private var okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(DownloadProgressInterceptor(
                    DownLoadProgressListener { bytesRead, contentLength, done ->
                        Log.e("zhang", "bytesRead = $bytesRead   contentLength = $contentLength   done = $done")
//                        val download = DownLoadInfo()
//                        download.totalFileSize = 15000000
//                        download.currentFileSize = bytesRead.toInt()
//                        download.progress = 1000
//                        sendNotification(download)
                    }))
            .build()

    private var retrofit: Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

    fun loadApk() {
        retrofit
                .create(ApkloadInterface::class.java)
                .downloadApk("")
                .map { t: ResponseBody -> mapProgress(t) }
                .subscribeOn(Schedulers.io())
                .subscribe({

                }, {
                    Log.e("zhang", "Load Fail e = $it")
                })
    }

    private fun mapProgress(body: ResponseBody) {
        var count: Int
        val data = ByteArray(1024 * 4)

        val fileSize = body.contentLength()
        val bis = BufferedInputStream(body.byteStream(), 1024 * 8)
        val outputFile = File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "update.apk")
        val output = FileOutputStream(outputFile)
        val startTime = System.currentTimeMillis()
        var total: Long = 0
        var timeCount = 1
        count = bis.read(data)
        while (count != -1) {

            total += count.toLong()
            totalFileSize = (fileSize / Math.pow(1024.0, 2.0)).toInt()
            val current = Math.round(total / Math.pow(1024.0, 2.0)).toDouble()

            val progress = (total * 100 / fileSize).toInt()

            val currentTime = System.currentTimeMillis() - startTime

            val download = DownLoadInfo()
            download.totalFileSize = totalFileSize

            if (currentTime > 1000 * timeCount) {

                download.currentFileSize = current.toInt()
                download.progress = progress
                sendNotification(download)
                timeCount++
            }

            output.write(data, 0, count)
            count = bis.read(data)
        }
        onDownloadComplete()
        output.flush()
        output.close()
        bis.close()
    }

    private fun sendNotification(download: DownLoadInfo) {

        notificationBuilder.setProgress(100, download.progress, false)
        notificationBuilder.setContentText("Downloading file " + download.currentFileSize + "/" + totalFileSize + " MB")
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun sendIntent(download: DownLoadInfo) {
//        val intent = Intent(MainActivity.MESSAGE_PROGRESS)
//        intent.putExtra("download", download)
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun onDownloadComplete() {

        val download = DownLoadInfo()
        download.progress = 100
//        sendIntent(download)

        notificationManager.cancel(0)
        notificationBuilder.setProgress(0, 0, false)
        notificationBuilder.setContentText("File Downloaded")
        notificationManager.notify(0, notificationBuilder.build())

    }

    override fun onTaskRemoved(rootIntent: Intent) {
        notificationManager.cancel(0)
    }
}
