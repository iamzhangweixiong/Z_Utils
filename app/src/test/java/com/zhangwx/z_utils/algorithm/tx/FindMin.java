package com.zhangwx.z_utils.algorithm.tx;

/**
 * 递增循环数组
 * 要求尽可能的快，二分查找
 */
public class FindMin {

    public int findmin(int array[], int count) {

        int length = array.length;


        return 0;
    }

    public int search(int startIndex, int endIndex, int array[], int count) {
        int mid = (endIndex - startIndex) / 2;
        if (array[mid] > count) {
//            return search()
        }

        return mid;
    }


    public static void main(String[] args) {
        int[] test = new int[]{50, 52, 63, 90, 3, 8, 15, 44};

    }
}
