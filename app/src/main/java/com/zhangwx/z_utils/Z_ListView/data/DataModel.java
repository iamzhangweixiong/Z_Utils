package com.zhangwx.z_utils.Z_ListView.data;

/**
 * Created by zhangweixiong on 2017/10/15.
 */

public class DataModel {

    public static final byte TYPE_ONE = 0x01;
    public static final byte TYPE_TWO = 0x02;
    public static final byte TYPE_THREE = 0x03;

    private byte mType;
    private int mImageId;
    private String mText;

    public byte getType() {
        return mType;
    }

    public void setType(byte mType) {
        this.mType = mType;
    }

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int mImageId) {
        this.mImageId = mImageId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }
}
