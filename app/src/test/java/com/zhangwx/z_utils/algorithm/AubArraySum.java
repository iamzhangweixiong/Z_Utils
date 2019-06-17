package com.zhangwx.z_utils.algorithm;

import java.util.HashMap;
import java.util.Map;

// 和为K的子数组
public class AubArraySum {

    int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 3, 1};

    int k = 7; // 这里用求和为 7 子数组

    int result = 0;

    // 为什么是 sum - k
    // [1,2] 的值是 3
    // [1,2,3,4] 的值是 10
    // 这时候 sum-k = 10-7 = 3
    // 也就是说，连续的数组，当 sum-k 的值在之前出现过，那么肯定存在子数组的值为 K --> [3,4]
    // 时间复杂度 O(n)，空间复杂度 O(1)
    public int subarraySum() {
        Map<Integer, Integer> sumMap = new HashMap<>();
        int sum = 0;
        for (int num : data) {
            sum += num;
            if (sumMap.containsValue(sum - k)) {
                result += sumMap.get(sum - k) + 1;
            }
            sumMap.put(sum, sumMap.get(sum) + 1);
        }
        return result;
    }

    // 解法2
    // 时间复杂度 O(n^2)，空间复杂度 O(1)
    public int subarraySum2() {
        for (int i : data) {
            int sum = 0;
            for (int j = i; j < data.length; j++) {
                sum += j;
                if (sum == k) {
                    result += 1;
                }
            }
        }
        return result;
    }

}
