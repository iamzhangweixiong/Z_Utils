package com.zhangwx.z_utils.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 一个 lamdba 表达式引起的死锁
 */
public class DeadLock {
    public static void main(String[] args) {
        System.out.println("startTest");
        // 起一个线程T1，初始化A
        new Thread(new Runnable() {
            @Override
            public void run() {
                new A();
            }
        }).start();

        // 停顿下，确保T1初始化A类时的“将lamdba传递出去”步骤可以执行完成
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 和A类中的key相同，确保可以掉到同样的lamdba表达式
        System.out.println(new Container().get("key"));

        System.out.println("finishTest");
    }
}

// javap -p
//class com.zhangwx.z_utils.thread.A {
//private static final com.zhangwx.z_utils.thread.Container CT;
//com.zhangwx.z_utils.thread.A();
//private static java.lang.String lambda$static$0();
//static {};
//}
class A {
    private static final Container CT = Container.addSupplier("key", () -> "value"); // 传入一个lamdba表达式

    //private static java.lang.String lambda$static$0();
    // lamdba 生成的这静态方法依赖 A 初始化完成
}

//class com.zhangwx.z_utils.thread.Container {
//  private static java.util.Map<java.lang.String, java.util.function.Supplier<java.lang.String>> supplierMap;
//  com.zhangwx.z_utils.thread.Container();
//  public java.lang.String get(java.lang.String);
//  public static com.zhangwx.z_utils.thread.Container addSupplier(java.lang.String, java.util.function.Supplier<java.lang.String>);
//  static {};
//}
class Container {
    private static Map<String, Supplier<String>> supplierMap = new ConcurrentHashMap<>();

    public String get(String key) {
        synchronized (supplierMap) { // 在获取时加锁
            return supplierMap.get(key).get();// get 需要等待 A 初始化。此时 A 没有初始化完成
        }
    }

    public static Container addSupplier(String key, Supplier<String> supplier) {
        // 将可能的lamdba放入static map，确保其它线程（类可以调用到）
        supplierMap.put(key, supplier);

        // 停顿下，确保有操作可能在：“将lamdba传递出去” 和 “完成类整个初始化” 间调用
        // 这样在调用 get 方法时，A 就是未初始化完的状态
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (supplierMap) { // 和 get 函数采用同一个锁，这个时候在等 get 释放锁，造成死锁
            return new Container();
        }
    }
}