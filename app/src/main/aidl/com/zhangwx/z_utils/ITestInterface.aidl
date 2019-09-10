package com.zhangwx.z_utils;

import com.zhangwx.z_utils.Z_AIDL.ContentData;

interface ITestInterface {

    String getTestContent(int id);

    // oneway 表示非同步方法，是异步调用，但是不能有返回值
    oneway void testIn(in ContentData data);

    void testOut(out ContentData data);

    void testInOut(inout ContentData data);
}
