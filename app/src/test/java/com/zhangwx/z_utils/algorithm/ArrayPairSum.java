package com.zhangwx.z_utils.algorithm;

import java.util.Arrays;

// 拆分数组，求从 1 到 n, min(a,b) 总和最大值
public class ArrayPairSum {

    private int[] intArray = new int[]{1, 4, 3, 2};
    private int sum = 0;
    private int maxMinValue = 0;

    /**
     * 做法1
     */
    public int getMaxPairValue() {
        for (int i = 0; i < intArray.length; i++) {
            for (int j = i + 1; j < intArray.length; j++) {
                int minValue = min(intArray[i], intArray[j]);
                int tempValue = maxMinValue + minValue;
                if (tempValue > sum) {
                    sum = tempValue;
                    maxMinValue = minValue;
                }
            }
        }
        return sum;
    }

    private int min(int a, int b) {
        return a > b ? b : a;
    }


    /**
     * 做法2
     * 规定拆成 n 对
     * 本质是-- 先排序，找到0,2,4,6...就是对应的每2组的最小值。 nums = [1,4,3,2] 排序后为 nums = [1,2,3,4] -> nums[0]+nums[2]
     */
    public int getMaxPairValueN() {
        Arrays.sort(intArray);
        //循环相加
        int sum = 0;
        for (int i = 0; i < intArray.length; i++) {
            if (i % 2 != 0) {// 取第奇数个
                sum += Math.min(intArray[i], intArray[i - 1]);
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        ArrayPairSum sum = new ArrayPairSum();
        System.out.println(sum.getMaxPairValue());

//        Arrays.sort(intArray);
//        for (int i : intArray) {
//            System.out.println(i);
//        }
    }
}




