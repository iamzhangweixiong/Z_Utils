package com.zhangwx.z_utils.Z_Pub;


/**
 * <p>
 *     使用 bitmask 来设置不同的状态，可以降低内存使用
 * </p>
 * Created by zhangwx on 2016/8/15.
 */
public class BitMaskSample {

    private static final int STATUS_START = 0;

    private static final int STATUS_PRESSED = 2;// 0000 0010
    private static final int STATUS_NOMAL = 4;// 0000 0100
//    private static final int STATUS_THIRD = 8;// 0000 1000

    private static final int STATUS_MASK = STATUS_NOMAL | STATUS_PRESSED;// 0000 0110
//    private static final int STATUS_MASK = STATUS_NOMAL | STATUS_PRESSED | STATUS_THIRD;// 0000 1110

    private int mStatus = STATUS_START;

    public boolean isPressedOrNomal() {
        return (mStatus & STATUS_MASK) != 0;
    }

    public boolean isPressed() {
        return (mStatus & STATUS_PRESSED) != 0;
    }

    public boolean isNomal() {
        return (mStatus & STATUS_NOMAL) != 0;
    }

    public void setStatusPressed(boolean isPressed) {
        if (isPressed) {
            mStatus |= STATUS_PRESSED;
            return;
        }
        mStatus &= ~STATUS_PRESSED;
    }

    public void setStatusNomal(boolean isNomal) {
        if (isNomal) {
            mStatus |= STATUS_NOMAL;
            return;
        }
        mStatus &= ~STATUS_NOMAL;
    }
}
