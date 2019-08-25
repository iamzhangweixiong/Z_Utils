package com.zhangwx.z_utils.java_base.父类和子类关系;

public class ChildClass extends ParentClass {
    public int i = 40;

    public static void main(String[] args) {
        ParentClass parentClass = new ParentClass();
        ChildClass childClass = new ChildClass();
        System.out.println(parentClass.i + childClass.i);


        /**
         * 理解
         * instanceof: 子类是不是继承了父类
         * isAssignableFrom: 父类是否可以从子类中抽出来
         * isInstance: 和 isAssignableFrom 一样
         */
        boolean parentInstanceOfChild = parentClass instanceof ChildClass;
        boolean childInstanceOfParent = childClass instanceof ParentClass;

        boolean childInstanceParent = ChildClass.class.isInstance(parentClass);
        boolean parentInstanceChild = ParentClass.class.isInstance(childClass);

        boolean childIsAssignableFromParent = ChildClass.class.isAssignableFrom(ParentClass.class);
        boolean parentIsAssignableFromChild = ParentClass.class.isAssignableFrom(ChildClass.class);

        System.out.println("Parent instanceOf Child = " + parentInstanceOfChild);
        System.out.println("Child instanceOf Parent = " + childInstanceOfParent);
        System.out.println("Child instance Parent = " + childInstanceParent);
        System.out.println("Parent instance Child = " + parentInstanceChild);
        System.out.println("ChildClass is Assignable From ParentClass = " + childIsAssignableFromParent);
        System.out.println("ParentClass is Assignable From ChildClass = " + parentIsAssignableFromChild);
    }
}
