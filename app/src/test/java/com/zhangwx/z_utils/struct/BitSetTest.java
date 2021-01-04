package com.zhangwx.z_utils.struct;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
// 有1千万个随机数，随机数的范围在1到1亿之间。现在要求写出一种算法，将1到1亿之间没有在随机数中的数求出来？
// BitSet 通过判断对应 index 的值是否被设置来判断对否存在
public class BitSetTest {
    public static void main(String[] args) {
        Random random = new Random();

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int randomResult = random.nextInt(100);
            list.add(randomResult);
        }
        System.out.println("产生的随机数有");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        BitSet bitSet = new BitSet(100);
        for (int i = 0; i < list.size(); i++) {
            bitSet.set(list.get(i));
        }

        System.out.println("0~100 不在上述随机数中有 " + bitSet.cardinality());
        for (int i = 0; i < 100; i++) {
            if (!bitSet.get(i)) {
                System.out.println(i);
            }
        }
    }
}
