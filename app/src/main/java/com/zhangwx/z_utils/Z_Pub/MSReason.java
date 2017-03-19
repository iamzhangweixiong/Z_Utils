package com.zhangwx.z_utils.Z_Pub;

/**
 * use:
 * reason.addFlags(MSReason.FLAG_ONE | MSReason.FLAG_TWO | MSReason.FLAG_THREE)
 * reason.getFlags() & MSReason.FLAG_ONE != 0
 */
public class MSReason {

    public static final int FLAG_ONE   = 0x00000001;
    public static final int FLAG_TWO   = 0x00000002;
    public static final int FLAG_THREE = 0x00000004;
    public static final int FLAG_FOUR  = 0x00000008;
    public static final int FLAG_FIVE  = 0x00000010;
    public static final int FLAG_SIX   = 0x00000020;
    public static final int FLAG_SEVEN = 0x00000040;
    public static final int FLAG_EIGHT = 0x00000080;
    public static final int FLAG_NINE  = 0x00000100;
    public static final int FLAG_TEN   = 0x00000200;

    private int mFlags;

    /**
     * Add additional flags to the reason (or with existing flags
     * value).
     *
     * @param flags The new flags to set.
     * @return Returns the same MSReason object, for chaining multiple calls
     * into a single statement.
     * @see #setFlags
     */
    protected MSReason addFlags(int flags) {
        mFlags |= flags;
        return this;
    }

    protected MSReason setFlags(int flags) {
        mFlags = flags;
        return this;
    }

    /**
     * Retrieve any special flags associated with this reason.  You will
     * normally just set them with {@link #setFlags} and let the system
     * take the appropriate action with them.
     *
     * @return int The currently set flags.
     * @see #setFlags
     */
    public int getFlags() {
        return mFlags;
    }
}
