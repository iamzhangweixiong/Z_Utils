package com.zhangwx.z_utils.Z_Thread.ThreadPools;

import com.zhangwx.z_utils.Z_Thread.HandlerThread.HandlerService;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by zhangwx on 2017/5/14.
 * <p>
 * Executors.newCachedThreadPool();
 * 一个没有限制最大线程数的线程池，线程池中如果有已创建的线程，则重复利用，没有则重建。
 * Cache 的线程默认 timeout 是 60s，超过将内移除线程池。
 * <p>
 * Executors.newFixedThreadPool(count);
 * 限制线程池大小为 count 的线程池，这 count 条线程永远在池中，可以重复利用，
 * 一旦需求超过 count 条，队列将阻塞
 * <p>
 * Executors.newScheduledThreadPool(count);
 * 一个在制定时间内可周期性的执行的线程池
 * <p>
 * Executors.newSingleThreadExecutor();
 * 每次只执行一个线程任务的线程池，最大的好处是保证 FIFO
 */

public class ZThreadPool {

    private static ExecutorService mThreadPool;
    private static ScheduledExecutorService mScheduledExecutorService;
    private ZThreadPool sInstance;

    private static final int CPU_CORE = Runtime.getRuntime().availableProcessors();

    public ZThreadPool getInstance() {
        if (sInstance == null) {
            synchronized (HandlerService.class) {
                if (sInstance == null) {
                    sInstance = new ZThreadPool();
                }
            }
        }
        return sInstance;
    }

    private ZThreadPool() {
        mThreadPool = Executors.newFixedThreadPool(CPU_CORE * 2 + 1);
        mScheduledExecutorService = Executors.newScheduledThreadPool(CPU_CORE);
    }

    public void command(Runnable runnable) {
        mThreadPool.execute(runnable);
    }

    public void command(Runnable command, long initialDelay, long period, TimeUnit unit) {
        mScheduledExecutorService.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    private static int getNumCores() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                return Pattern.matches("cpu[0-9]", pathname.getName());
            }
        }
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }
}
