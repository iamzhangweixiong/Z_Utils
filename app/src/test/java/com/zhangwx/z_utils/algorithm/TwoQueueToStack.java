package com.zhangwx.z_utils.algorithm;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 用队列实现栈
 *
 * 思路1：
 * 队列是 FIFO 的，需要用两个栈队列，一个用来存放临时的数据
 * 每次 pop 的时候先把主队列里的元素放到临时队列里，同时返回最后一个元素，再把临时队列里的元素放回来
 * pop 的时候是 O(n), push 的时候是 O(1)
 *
 * 思路2：
 * 每次 push 的时候先放到临时队列中，再把主队列中的元素放到临时队列中，最后把主队列和临时队列对调
 * 这样 pop 的时候是 O(1), push 的时候是 O(n)
 */
public class TwoQueueToStack {

    private int top;
    private Queue<Integer> queue1 = new LinkedList<>();// 主队列
    private Queue<Integer> queue2 = new LinkedList<>();// 临时队列

    public void push(int x) {
        queue1.add(x);
        top = x;
    }

    public int pop() {
        while (queue1.size() > 1) {
            queue2.add(queue1.remove());
        }
        final int last = queue1.remove();

        // 做一个转化
        final Queue<Integer> tempQueue = queue1;
        queue1 = queue2;
        queue2 = tempQueue;
        return last;
    }

    public int top() {
        return top;
    }

    public boolean empty() {
        return queue1.size() == 0;
    }

    public static void main(String[] args) {

    }
}
