package com.zhangwx.z_utils.java_base;

/**
 * AsyncTask java 部分原理
 * 1. 静态代码块只会执行一次
 * 2. 静态内部类只会 new 一次
 */
public class StaticInnerClass {

    private static InnerClass innerClass = null;

    static {
        System.out.println("静态代码块执行 ...");
    }

    public StaticInnerClass() {
        if (innerClass == null) {
            System.out.println("new 静态内部类 ...");
            innerClass = new InnerClass();
        }
        innerClass.printName();
    }

    static class InnerClass {

        static {
            System.out.println("静态内部类 静态代码块执行 ...");
        }

        void printName() {
            System.out.println("静态内部类方法执行 ...");
        }
    }

    public static void main(String[] args) {
        new StaticInnerClass();
        new StaticInnerClass();
        new StaticInnerClass();
    }
}
