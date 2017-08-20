package com.zhangwx.z_utils.Z_TextView;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

import java.lang.reflect.Method;

/**
 * Created by zhangweixiong on 2017/8/15.
 * 修改 TextView 下划线颜色的 span
 * SpannableString content = new SpannableString("sfsfvsdf");
 * content.setSpan(new ColoredUnderlineSpan(Color.BLUE), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
 */

public class ColoredUnderlineSpan extends CharacterStyle implements UpdateAppearance {

    private final int mColor;

    public ColoredUnderlineSpan(final int color) {
        mColor = color;
    }

    @Override
    public void updateDrawState(final TextPaint tp) {
        try {
            final Method method = TextPaint.class.getMethod("setUnderlineText",
                    Integer.TYPE,
                    Float.TYPE);
            method.invoke(tp, mColor, 3.0f);
        } catch (final Exception e) {
            tp.setUnderlineText(true);
        }
    }
}
