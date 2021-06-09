package com.zhangwx.z_utils.Z_Regex

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.zhangwx.z_utils.R
import kotlinx.android.synthetic.main.activity_regex.*
import java.util.regex.Matcher
import java.util.regex.Pattern

@RequiresApi(Build.VERSION_CODES.O)
class RegexTestActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "RegexTestActivity"
        // 分组别名
        private const val PATTERN_KEEP_LIST = "(?<package>([\\w]+\\.)*)R.(?<inner>[^.]+)(.(?<field>.+))?"

        fun getMatchByGroup(m: Matcher, name: String): String? {
            if (m.find()) {
                return m.group(name)
            }
            return ""
        }
    }

    private val elapsedStartTime = SystemClock.elapsedRealtime()
    private val startTime = System.currentTimeMillis()

    private val keepListPattern = Pattern.compile(PATTERN_KEEP_LIST)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regex)

        val item = "android.support.constraint.R.id"
        val m = keepListPattern.matcher(item)
        var packageName = getMatchByGroup(m, "package")
        val innerClass = getMatchByGroup(m, "inner")
        val fieldName = getMatchByGroup(m, "field")
        val className = if (innerClass != null && innerClass.isNotEmpty()) {
            "R$$innerClass"
        } else {
            "R"
        }
        if (packageName != null && !packageName.isEmpty()) {
            packageName = packageName.replace("\\.".toRegex(), "/")
        } else {
            packageName = "([\\w]+/)*"
        }

        regex_result.text = packageName
        Log.e(TAG, "packageName = $packageName")
        Log.e(TAG, "innerClass = $innerClass")
        Log.e(TAG, "fieldName = $fieldName")
        Log.e(TAG, "className = $className")

        start.setOnClickListener {
            Log.e(TAG, "elapsed duration = ${SystemClock.elapsedRealtime() - elapsedStartTime}")
            Log.e(TAG, "system duration = ${System.currentTimeMillis() - startTime}")
        }

        regex_result.text = getVersionCode(this)
    }

    private fun getVersionCode(context: Context?): String {
        return try {
            context?.packageManager?.getPackageInfo(context.packageName, 0)?.versionName
                    ?: "UNKNOWN"
        } catch (e: Throwable) {
            e.printStackTrace()
            "UNKNOWN"
        }
    }
}