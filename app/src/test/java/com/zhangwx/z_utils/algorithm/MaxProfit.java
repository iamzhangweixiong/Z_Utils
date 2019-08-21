package com.zhangwx.z_utils.algorithm;

// 求最大利润（最大差值）
public class MaxProfit {

    private int[] prices = new int[]{7, 1, 5, 3, 6, 4};

    public int maxProfit() {
        int lowPoint = 0;
        int highPoint = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < prices[lowPoint]) {
                lowPoint = i;
            }
            if (highPoint < lowPoint) {
                prices[highPoint] = 0;
            }
            if (prices[i] > prices[highPoint]) {
                if (i > lowPoint) {
                    highPoint = i;
                }
            }
        }

        int max = prices[highPoint] - prices[lowPoint];

        return max < 0 ? 0 : max;
    }

    public static void main(String[] args) {
        MaxProfit maxProfit = new MaxProfit();
        System.out.println(maxProfit.maxProfit());
    }
}
