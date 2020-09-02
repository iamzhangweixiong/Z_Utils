package com.zhangwx.z_utils.java_base.锁;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            try {
                CountDownLatch lock = new CountDownLatch(1);
                int finalI = i;
                new Thread(() -> {
                    try {
                        Thread.sleep(3000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("run thread = " + finalI);
                    lock.countDown();
                }).start();
                lock.await(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
