package com.zhangwx.z_utils.Z_Components;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {


    @Override
    public boolean onStartJob(JobParameters params) {
        EventBus.getDefault().post(params.getJobId());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
