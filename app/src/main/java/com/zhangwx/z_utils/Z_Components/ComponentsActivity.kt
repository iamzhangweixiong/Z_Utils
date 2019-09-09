package com.zhangwx.z_utils.Z_Components

import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TimeUtils
import com.zhangwx.z_utils.R
import com.zhangwx.z_utils.Z_RecycleView.RecycleActivity
import com.zhangwx.z_utils.Z_Utils.DateUtils
import kotlinx.android.synthetic.main.activity_components.*;
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

class ComponentsActivity : Activity() {

    companion object {
        const val SchedulerId = 123456
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_components)
        EventBus.getDefault().register(this)

        JumpActivity.setOnClickListener {
            val intent = Intent(this, ComponentsActivity::class.java)
            startActivity(intent)
        }

        JumpService.setOnClickListener {
            val intent = Intent(this, RecycleActivity::class.java)
            startActivity(intent)
        }

        JobService.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val job = this.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                val jobInfo = JobInfo
                        .Builder(SchedulerId, ComponentName(this, JobSchedulerService::class.java))
                        .setPeriodic(1000L * 60)//间隔
                        .build()
                job.schedule(jobInfo)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onJobscheduler(job: String) {
        infoText.text = DateUtils.getCurrentTime(System.currentTimeMillis())
    }
}
