package com.zhangwx.z_utils.algorithm;

import java.util.Stack;

/**
 * 用两个栈实现队列
 *
 * 思路：
 * 队列是 FIFO 的，栈是 FILO 的
 * 栈 S1 用来 push 数据
 * 当 pop 数据时，判断 S2 是不是空的，如果是空的，先把 S1 中的数据倒到 S2 中，再从 S2 中依次取出就可以了
 *
 *
 * Stack 继承自 Vector，是线程安全的
 */
public class TwoStackToQueue {

    private Stack<Integer> s1 = new Stack<>();
    private Stack<Integer> s2 = new Stack<>();

    public void push(int value) {
        s1.push(value);
    }

    public int pop() {
        if (s1.empty() && s2.empty()) {
            throw new RuntimeException("queue is empty");
        }
        if (s2.empty()) {
            while (!s1.empty()) {
                s2.push(s1.pop());
            }
        }
        return s2.pop();
    }

    public static void main(String[] args) {
        TwoStackToQueue twoStackToQueue = new TwoStackToQueue();
        twoStackToQueue.push(1);
        twoStackToQueue.push(2);
        twoStackToQueue.push(3);
        twoStackToQueue.push(4);
        twoStackToQueue.push(5);

        System.out.println(twoStackToQueue.pop());
        System.out.println(twoStackToQueue.pop());
        System.out.println(twoStackToQueue.pop());
        System.out.println(twoStackToQueue.pop());
        System.out.println(twoStackToQueue.pop());
    }
}
