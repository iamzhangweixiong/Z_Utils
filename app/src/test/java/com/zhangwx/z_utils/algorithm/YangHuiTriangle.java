package com.zhangwx.z_utils.algorithm;

import java.util.ArrayList;
import java.util.List;

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

}
