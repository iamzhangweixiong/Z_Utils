package com.zhangwx.z_utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public enum Type {
        START, NODE, END;

        public String getName() {
            switch (Type.this) {
                case START:
                    return "s";
                default:
                    return "f";
            }
        }
    }

    @Test
    public void addition_isCorrect() throws Exception {

        int a = 1;
        System.out.println(a << 2 | 1);

        System.out.println(Type.START.getName());

    }
}