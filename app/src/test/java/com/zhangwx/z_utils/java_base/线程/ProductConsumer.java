package com.zhangwx.z_utils.java_base.线程;

public class ProductConsumer {

    private int n;

    public ProductConsumer(int n) {
        this.n = n;

        // volatile 方式
        this.lock = 2 * n;
    }

    /**
     * 信号量方式
     */
//    private Semaphore fooSemaphore = new Semaphore(1);
//    private Semaphore barSemaphore = new Semaphore(0);
//
//    public void foo(Runnable printFoo) throws InterruptedException {
//
//        for (int i = 0; i < n; i++) {
//            // printFoo.run() outputs "foo". Do not change or remove this line.
//            fooSemaphore.acquire();
//            printFoo.run();
//            barSemaphore.release();
//        }
//    }
//
//    public void bar(Runnable printBar) throws InterruptedException {
//
//        for (int i = 0; i < n; i++) {
//            // printBar.run() outputs "bar". Do not change or remove this line.
//            fooSemaphore.release();
//            printBar.run();
//            barSemaphore.acquire();
//        }
//    }

    /**
     * objectLock + flag 方式
     */
//    private Object objectLock = new Object();
//    private boolean flag = false;
//    public void foo(Runnable printFoo) throws InterruptedException {
//        for (int i = 0; i < n; i++) {
//            // printFoo.run() outputs "foo". Do not change or remove this line.
//            synchronized (objectLock) {// 注意一定要同步
//                while (flag) {
//                    objectLock.wait();
//                }
//                printFoo.run();
//                flag = true;
//                objectLock.notify();
//            }
//        }
//    }
//
//    public void bar(Runnable printBar) throws InterruptedException {
//        for (int i = 0; i < n; i++) {
//            // printBar.run() outputs "bar". Do not change or remove this line.
//            synchronized (objectLock) {
//                while (!flag) {
//                    objectLock.wait();
//                }
//                printBar.run();
//                flag = false;
//                objectLock.notify();
//            }
//        }
//    }


    /**
     * volatile 锁方式
     */
    private volatile int lock = 0;
    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            // printFoo.run() outputs "foo". Do not change or remove this line.
            while (lock % 2 == 1) {

            }
            printFoo.run();
            lock--;
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            // printBar.run() outputs "bar". Do not change or remove this line.
            while (lock % 2 == 0) {

            }
            printBar.run();
            lock--;
        }
    }

    public static void main(String[] args) {
        final ProductConsumer productConsumer = new ProductConsumer(5);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    productConsumer.foo(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("foo");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    productConsumer.bar(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("bar");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}


