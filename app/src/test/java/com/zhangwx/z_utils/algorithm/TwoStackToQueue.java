package com.zhangwx.z_utils.algorithm;

/**
 * 用两个栈实现队列
 *
 * 思路：
 * 队列是 FIFO 的，栈是 FILO 的
 * 栈 S1 用来 push 数据
 * 当 pop 数据时，判断 S2 是不是空的，如果是空的，先把 S1 中的数据倒到 S2 中，再从 S2 中依次取出就可以了
 */
public class TwoStackToQueue {
}
