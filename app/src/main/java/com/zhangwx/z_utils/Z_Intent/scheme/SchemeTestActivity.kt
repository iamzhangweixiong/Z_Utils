package com.zhangwx.z_utils.Z_Intent.scheme

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle

class SchemeTestActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent: Intent = Intent.parseUri("zhang://home", Intent.URI_INTENT_SCHEME or Intent.URI_ANDROID_APP_SCHEME)
        val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

        if (list != null && list.isNotEmpty()) {
            for (info in list) {
                if (packageName == info.activityInfo.packageName) {
                    // when the uri can be handled by this application
                    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name)
                }
            }
        }


    }
}