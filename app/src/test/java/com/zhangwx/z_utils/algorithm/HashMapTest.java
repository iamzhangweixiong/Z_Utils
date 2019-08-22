package com.zhangwx.z_utils.algorithm;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 1. LinkedHashMap 有序，HashMap 无序
 */
public class HashMapTest {

    public static void main(String[] args) {

        // LinkHashMap
        final HashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("Key1", "Value1");
        linkedHashMap.put("Key2", "Value2");
        linkedHashMap.put("Key3", "Value3");
        linkedHashMap.put("Key4", "Value4");
        linkedHashMap.put("Key5", null);
        linkedHashMap.put(null, "Value5");
        linkedHashMap.put(null, null);

        System.out.println("== LinkedHashMap ==");
        linkedHashMap.keySet().forEach(System.out::println);
        linkedHashMap.values().forEach(System.out::println);

        linkedHashMap.forEach((s, s2) -> System.out.println("key: " + s + "  value: " + s2));


        // HashMap
        final HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Key1", "Value1");
        hashMap.put("Key2", "Value2");
        hashMap.put("Key3", "Value3");
        hashMap.put("Key4", "Value4");
        hashMap.put("Key5", null);
        hashMap.put(null, "Value5");
        hashMap.put(null, null);

        System.out.println("== HashMap ==");
        hashMap.forEach((s, s2) -> System.out.println("key: " + s + "  value: " + s2));
    }
}
