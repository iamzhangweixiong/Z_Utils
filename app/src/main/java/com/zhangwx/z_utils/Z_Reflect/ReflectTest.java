package com.zhangwx.z_utils.Z_Reflect;

/**
 * Created by zhangwx on 2017/4/17.
 */

public class ReflectTest {

    private int mTypeOne;

    public String mTypeTwo = "TypeTwo";

    public String getTypeTwo() {
        return mTypeTwo;
    }

    public void setTypeTwo(String mTypeTwo) {
        this.mTypeTwo = mTypeTwo;
    }

    public int getTypeOne() {
        return mTypeOne;
    }

    public void setTypeOne(int mTypeOne) {
        this.mTypeOne = mTypeOne;
    }

    public void ReflectFunOne() {
        mTypeOne = 100;
    }

    private void ReflectFunTwo() {
        mTypeTwo = "Type4";
    }
}
