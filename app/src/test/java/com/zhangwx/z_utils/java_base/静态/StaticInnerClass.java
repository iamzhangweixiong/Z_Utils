package com.zhangwx.z_utils.java_base.静态;

/**
 * AsyncTask java 部分原理
 * 2. 静态变量一直都在
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
