package com.zhangwx.z_utils.algorithm;

/**
 * 用一个数组实现两个栈
 * 从两边向中间靠拢
 *  - > - > - > - > - - - - - < - < - < - <
 * | 2 | 1 | 3 | 4 | 6 |   | 9 | 3 | 1 | 5 |
 *  - > - > - > - > - - - - - < - < - < - <
 */
public class TwoStackByOneArray {

    private int[] objects;
    private int stack1Index, stack2Index;

    public TwoStackByOneArray(int length) {
        objects = new int[length];
        stack1Index = -1;
        stack2Index = length;
    }

    public boolean pushStack1(int value) {
        if (stack1Index + 1 == stack2Index) {
            return false;
        }
        objects[++ stack1Index] = value;
        return true;
    }

    public boolean pushStack2(int value) {
        if (stack2Index - 1 == stack1Index) {
            return false;
        }
        objects[-- stack2Index] = value;
        return true;
    }

    public int peekFromStack1() {
        return objects[stack1Index];
    }

    public int peekFromStack2() {
        return objects[stack2Index];
    }

    public int popFromStack1() {
        return objects[stack1Index --];
    }

    public int popFromStack2() {
        return objects[stack2Index ++];
    }

    public void getStack1() {
        System.out.println("Stack1 num :");
        for (int i = 0; i <= stack1Index; i++) {
            System.out.println(objects[i]);
        }
    }

    public void getStack2() {
        System.out.println("Stack2 num :");
        for (int i = objects.length - 1; i >= stack2Index; i--) {
            System.out.println(objects[i]);
        }
    }

    public static void main(String[] args) {
        TwoStackByOneArray twoStack = new TwoStackByOneArray(15);
        twoStack.pushStack1(2);
        twoStack.pushStack1(5);
        twoStack.pushStack1(7);
        twoStack.pushStack1(8);
        twoStack.pushStack1(11);

        twoStack.pushStack2(20);
        twoStack.pushStack2(50);
        twoStack.pushStack2(70);
        twoStack.pushStack2(80);
        twoStack.pushStack2(110);

        twoStack.getStack1();
        twoStack.getStack2();
    }
}
