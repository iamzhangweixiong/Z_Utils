package com.zhangwx.z_utils.Z_Thread.Timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import com.zhangwx.z_utils.MyApplication;
import com.zhangwx.z_utils.Z_DB.file.FileCache;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimerTest {

    public static final String ALARM_START = "alarm_start";
    private Timer mTimer;
    private CountDownTimer mCountDownTimer;
    private TimerTask mTimerTask;
    private static final long PERIOD = 10 * 1000;// 2s
    private static final String testApi = "http://api-cmshow-ios.cmcm.com/v1/get_source?cate=hot";

    public void startTimerTest() {
//        mTimer = new Timer();
//        mTimerTask = new TimerTask() {
//            @Override
//            public void run() {
//                final TestModel testModel = new TestModel();
//                testModel.testUrl(testApi, aBoolean -> {
//                    Log.e("zhang", "TestModel callback" + "\naBoolean = " + aBoolean);
////                    if (aBoolean) {
////                        stopTimer();
////                        startTimerTest();
////                    }
//                    return Unit.INSTANCE;
//                });
//            }
//        };
//        mTimer.scheduleAtFixedRate(mTimerTask, PERIOD / 5, PERIOD);


//        Thread thread = new TestThread();
//        thread.run();
//        try {
//            thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        thread.interrupt();

//        mCountDownTimer = new CountDownTimer(10000, 2000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Log.e("zhang", String.valueOf(millisUntilFinished));
//            }
//
//            @Override
//            public void onFinish() {
//                Log.e("zhang", "finish.....");
//            }
//        };
//        mCountDownTimer.start();

        startAlarm(MyApplication.getContext());
    }

    public static void startAlarm(Context context) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(ALARM_START);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) PERIOD, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10), pendingIntent);
    }

    public static void cancelAlarm(Context context, String action) {
        Intent intent = new Intent(action);
        PendingIntent sender = PendingIntent.getBroadcast(context, (int) PERIOD, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private class TestThread extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Log.e("zhang", "TestThread readFile");
                    FileCache.readFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopTimer() {
//        if (mTimerTask != null) {
//            mTimerTask.cancel();
//            mTimerTask = null;
//        }
//        if (mTimer != null) {
//            mTimer.cancel();
//            mTimer.purge();
//            mTimer = null;
//        }
    }

}
