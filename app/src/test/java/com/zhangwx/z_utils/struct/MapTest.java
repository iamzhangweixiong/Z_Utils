package com.zhangwx.z_utils.struct;

import com.zhangwx.z_utils.Z_Reflect.ReflectHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 1. LinkedHashMap 有序，HashMap 无序
 */
public class MapTest {

    public static void main(String[] args) {

        // LinkHashMap
        final HashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>(16,0.75f, true) {

            /**
             * 最先加入 map 中的元素就是最老的元素，修改过的元素要放到最后，擦亮新鲜度
             * 当超过规定的大小时删除最老的元素，这是 LruCache 最简单的实现
             */
            @Override
            protected boolean removeEldestEntry(Entry<String, String> eldest) {
                return size() > 16;
            }
        };
        linkedHashMap.put("Key1", "Value1");
        linkedHashMap.put("Key2", "Value2");
        linkedHashMap.put("Key3", "Value3");
        linkedHashMap.put("Key4", "Value4");
        linkedHashMap.put("Key5", null);
        linkedHashMap.put(null, "Value5");
        linkedHashMap.put(null, null);

        linkedHashMap.put("Key3", "replace");

        linkedHashMap.put("Key10", "Value1");
        linkedHashMap.put("Key20", "Value2");
        linkedHashMap.put("Key30", "Value3");
        linkedHashMap.put("Key40", "Value4");
        linkedHashMap.put("Key50", null);

        linkedHashMap.put("Key91", "Value1");
        linkedHashMap.put("Key92", "Value2");
        linkedHashMap.put("Key93", "Value3");
        linkedHashMap.put("Key94", "Value4");
        linkedHashMap.put("Key95", null);

        linkedHashMap.put("Key191", "Value1");
        linkedHashMap.put("Key192", "Value2");
        linkedHashMap.put("Key193", "Value3");
        linkedHashMap.put("Key194", "Value4");
        linkedHashMap.put("Key195", null);



        System.out.println("== LinkedHashMap == " + linkedHashMap.size());
//        linkedHashMap.keySet().forEach(System.out::println);
//        linkedHashMap.values().forEach(System.out::println);

        linkedHashMap.forEach((s, s2) -> System.out.println("key: " + s + "  value: " + s2));


        // HashMap
//        final HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("Key1", "Value1");
//        hashMap.put("Key2", "Value2");
//        hashMap.put("Key3", "Value3");
//        hashMap.put("Key4", "Value4");
//        hashMap.put("Key5", null);
//        hashMap.put(null, "Value5");
//        hashMap.put(null, null);
//
//        hashMap.put("Key11", "Value1");
//        hashMap.put("Key12", "Value2");
//        hashMap.put("Key13", "Value3");
//        hashMap.put("Key14", "Value4");
//        hashMap.put("Key15", null);
//
//        hashMap.put("Key111", "Value1");
//        hashMap.put("Key112", "Value2");
//        hashMap.put("Key113", "Value3");
//        hashMap.put("Key114", "Value4");
//        hashMap.put("Key115", null);
//
//        hashMap.put("Key110", "Value1");
//        hashMap.put("Key120", "Value2");
//        hashMap.put("Key130", "Value3");
//        hashMap.put("Key140", "Value4");
//        hashMap.put("Key150", null);
//
//        hashMap.put("Key1110", "Value1");
//        hashMap.put("Key1120", "Value2");
//        hashMap.put("Key1130", "Value3");
//        hashMap.put("Key1140", "Value4");
//        hashMap.put("Key1150", null);
//
//        System.out.println("== HashMap ==");
//        hashMap.forEach((s, s2) -> System.out.println("key: " + s + "  value: " + s2));
//
//        System.out.println("== entrySet: ==");
//        hashMap.entrySet().forEach(stringStringEntry -> System.out.println("key: " + stringStringEntry.getKey() + "  value: " + stringStringEntry.getValue()));
//
//        System.out.println("== 链表入口 table: ==");
//        final Map.Entry[] nodes = (Map.Entry[]) ReflectHelper.getFieldValue(hashMap, "table");
//        System.out.println(nodes.length);
//        for (Map.Entry e: nodes) {
//            if (e != null) {
//                System.out.println("key: " + e.getKey() + "  value: " + e.getValue());
//            }
//        }

    }
}
