package com.zhangwx.z_utils.java_base.线程;


public class HandlerTest {

    public static void main(String[] args) {
        forE:
        for (String s : args) {
            if (s.isEmpty()) {
                break forE;
            }
        }
    }
}
