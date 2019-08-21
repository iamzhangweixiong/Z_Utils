package com.zhangwx.z_utils.java_base.静态.内部类继承;

public class ExtendStaticInnerClass {

    static class InnerClass {

        int i = 0;

        static {
            System.out.println("静态内部类 静态代码块执行 ...");
        }

        void printName() {
            System.out.println("静态内部类方法执行 ... i = " + i);
        }
    }
}
