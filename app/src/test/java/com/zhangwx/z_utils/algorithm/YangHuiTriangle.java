package com.zhangwx.z_utils.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成杨辉三角：
 *
 * 　　　　　　　　１
 * 　　　　　　　１　１
 * 　　　　　　１　２　１
 * 　　　　　１　３　３　１
 * 　　　　１　４　６　４　１
 * 　　　１　５　10　10　５　１
 * 　　１　６　15　20　15　６　１
 * 　１　７　21　35　35　21　７　１
 * １　８　28　56　70　56　28　８　１
 *
 */
public class YangHuiTriangle {

    private int numRows = 0;
    private List<List<Integer>> genList = new ArrayList<>();

    public List<List<Integer>> generate(int numRows) {
        this.numRows = numRows;
        generate(new ArrayList<>());
        return genList;
    }

    private void generate(List<Integer> oldList) {
        int newSize = oldList.size() + 1;

        if (newSize > numRows) return;

        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < newSize; i++) {
            if (i == 0 || i == newSize - 1) {
                newList.add(i, 1);
            } else {
                newList.add(oldList.get(i - 1) + oldList.get(i));
            }
        }
        genList.add(newList);

        generate(newList);
    }

    public static void main(String[] args) {
        YangHuiTriangle yangHuiTriangle = new YangHuiTriangle();
        System.out.println(yangHuiTriangle.generate(5));
    }
}
