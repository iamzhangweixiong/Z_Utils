package com.zhangwx.z_utils.Z_Widget.WebImageView.utils;

import java.io.FileNotFoundException;

public class CancelledException extends FileNotFoundException {
    public CancelledException() {
        super("cancelled");
    }
}
