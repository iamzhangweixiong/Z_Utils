package com.zhangwx.z_utils.algorithm;

// 用一个数组实现两个栈
// 从两边向中间靠拢
//  - > - > - > - > - - - - - < - < - < - <
// | 2 | 1 | 3 | 4 | 6 |   | 9 | 3 | 1 | 5 |
//  - > - > - > - > - - - - - < - < - < - <
//
public class ArrayTwoStack {

    private int[] objects;
    private int stack1Index, stack2Index;

    public ArrayTwoStack(int length) {
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
        ArrayTwoStack arrayTwoStack = new ArrayTwoStack(15);
        arrayTwoStack.pushStack1(2);
        arrayTwoStack.pushStack1(5);
        arrayTwoStack.pushStack1(7);
        arrayTwoStack.pushStack1(8);
        arrayTwoStack.pushStack1(11);

        arrayTwoStack.pushStack2(20);
        arrayTwoStack.pushStack2(50);
        arrayTwoStack.pushStack2(70);
        arrayTwoStack.pushStack2(80);
        arrayTwoStack.pushStack2(110);

        arrayTwoStack.getStack1();
        arrayTwoStack.getStack2();
    }
}
