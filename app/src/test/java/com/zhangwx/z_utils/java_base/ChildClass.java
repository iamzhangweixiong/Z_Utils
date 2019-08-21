package com.zhangwx.z_utils.java_base;

/**
 *
 */
public class ChildClass extends ParentClass {
    public int i = 40;

    public static void main(String[] args){
        ParentClass parentClass = new ParentClass();
        ChildClass childClass = new ChildClass();
        System.out.println(parentClass.i + childClass.i);
    }
}
