package com.zhangwx.z_utils;

import android.util.Log;

import com.zhangwx.z_utils.algorithm.ArrayPairSum;
import com.zhangwx.z_utils.algorithm.MaxDepth;
import com.zhangwx.z_utils.algorithm.YangHuiTriangle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

//        MaxDepth max = new MaxDepth();
//        System.out.println(max.maxDepth(max.genNode()));
//
//        ArrayPairSum sum = new ArrayPairSum();
//        System.out.println(sum.getMaxPairValue());

//        int a = 0;
//        System.out.println(a << 2 | 1);

        YangHuiTriangle yangHuiTriangle = new YangHuiTriangle();
        System.out.println(yangHuiTriangle.generate(5));


    }
}