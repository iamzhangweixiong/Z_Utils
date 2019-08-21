package com.zhangwx.z_utils.java_base.静态.内部类继承;

public class ExtendTest extends ExtendStaticInnerClass.InnerClass {

    public static void main(String[] args) {

        // 继承之后，静态的 class 的属性已经变成了子类的属性
        ExtendTest extendTest = new ExtendTest();
        extendTest.i = 2;
        extendTest.printName();

        new ExtendTest().printName();
        new ExtendTest().printName();

        // new 出来已经是新类了
        ExtendStaticInnerClass.InnerClass innerClass = new ExtendStaticInnerClass.InnerClass();
        innerClass.i = 1;
        innerClass.printName();
        new ExtendStaticInnerClass.InnerClass().printName();
    }
}
