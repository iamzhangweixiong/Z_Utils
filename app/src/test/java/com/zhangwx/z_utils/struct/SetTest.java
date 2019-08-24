package com.zhangwx.z_utils.struct;

import java.util.HashSet;
import java.util.Set;

public class SetTest {

    public static void main(String[] args) {
        Set<String> hashSet = new HashSet<>();

        hashSet.add("value1");
        hashSet.add("value2");
        hashSet.add("value3");

        // set 使用了 HashMap 中的 key 来存放对象，所以不允许重复的值，但是可以存在 null
        boolean result = hashSet.add("value1");
        System.out.println(result);

        hashSet.forEach(System.out::println);
    }
}
