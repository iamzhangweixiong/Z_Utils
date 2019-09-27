package com.zhangwx.z_utils.algorithm.tt;

/**
 * 顺时针螺旋矩阵矩阵，生成二维数组
 * <p>
 * [ 1, 2, 3 ]
 * [ 8, 9, 4 ]
 * [ 7, 6, 5 ]
 */
public class RotateMatrix {

    public int[][] generateMatrix(int n) {

        int[][] result = new int[n][n];
        int l = 0;
        int t = 0;
        int r = n - 1;
        int b = n - 1;

        int target = n * n;
        int num = 1;

        while (num <= target) {

            // 从左到右
            for (int i = l; i <= r; i++) {
                result[t][i] = num++;
            }
            t++;

            // 从右上到右下
            for (int i = t; i <= b; i++) {
                result[i][r] =num++;
            }
            r--;

            // 从右下到左下
            for (int i = r; i >= l; i--) {
                result[b][i] =num++;
            }
            b--;

            // 从左下到左上
            for (int i = b; i >= t; i--) {
                result[i][l] =num++;
            }
            l++;
        }
        return result;
    }

    public static void main(String[] args) {
        RotateMatrix rotateMatrix = new RotateMatrix();
        int[][] result = rotateMatrix.generateMatrix(5);
        for (int i = 0; i < result.length; i++) {
            int[] line = new int[result.length];
            for (int j = 0; j < result.length; j++) {
                line[j] = result[i][j];
            }

            for (int value : line) {
                System.out.print("  "+value);
            }
            System.out.println(" ");
        }
    }
}
