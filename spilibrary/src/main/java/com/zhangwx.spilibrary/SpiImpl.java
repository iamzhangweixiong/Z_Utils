package com.zhangwx.spilibrary;

import com.zhangwx.spiinterface.Display;

public class SpiImpl implements Display {
    @Override
    public String getDisplayName() {
        return "SpiImpl";
    }
}
