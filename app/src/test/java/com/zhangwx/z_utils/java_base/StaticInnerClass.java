package com.zhangwx.z_utils.java_base;

/**
 * AsyncTask java 部分原理
 * 1. 静态代码块只会执行一次
 * 2. 静态内部类只会 new 一次
 */
public class StaticInnerClass {

    private static InnerClass innerClass = null;

    static {
        System.out.println("static code block ...");
    }

    static class InnerClass {
         void printName() {
             System.out.println("InnerClass");
         }
    }

    public StaticInnerClass() {
        if (innerClass == null) {
            System.out.println("new InnerClass ...");
            innerClass = new InnerClass();
        }
        innerClass.printName();
    }
}
