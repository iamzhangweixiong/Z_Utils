package com.zhangwx.z_utils.java_base.基础;

/**
 * 进制转换
 */
public class BaseShift {

  public static void main(String[] args) {

    int value = -1187553289;
    int shiftValue = value ^ value >>> 16;
    System.out.println(Integer.toHexString(value));
    System.out.println(Integer.toHexString(value >>> 16));
    System.out.println(Integer.toHexString(shiftValue));
  }
}
