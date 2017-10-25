package com.zhangwx.z_utils.Z_Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zhangwx.z_utils.Z_UI.DimenUtils;

/**
 * Created by zhangweixiong on 2017/10/24.
 */

public class StepView extends View {

    private Paint mPaint;
    private Paint mTextPaint;
    private Paint.FontMetrics mFontMetrics;

    public static final float RADIUS = DimenUtils.dp2px(10);
    public static final float OFFSET = DimenUtils.dp2px(15);

    public StepView(Context context) {
        super(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setColor(0xff8A6FE1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(DimenUtils.dp2px(3));
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(DimenUtils.dp2px(15));
        mTextPaint.setAntiAlias(true);
        mFontMetrics = mTextPaint.getFontMetrics();
        Log.e("zhang", "onDraw: mFontMetrics.descent = " + mFontMetrics.descent +
                "   ascent = " + mFontMetrics.ascent +
                "   bottom = " + mFontMetrics.bottom +
                "   top = " + mFontMetrics.top +
                "   leading = " + mFontMetrics.leading);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        canvas.drawLine(
                getWidth() / 2 - RADIUS - OFFSET, (getBottom() - getTop()) / 2,
                getWidth() / 2 + RADIUS + OFFSET, (getBottom() - getTop()) / 2, mPaint);

        // 画圆
        canvas.drawCircle(getWidth() / 2 - RADIUS - OFFSET, (getBottom() - getTop()) / 2, RADIUS, mPaint);
        canvas.drawCircle(getWidth() / 2 + RADIUS + OFFSET, (getBottom() - getTop()) / 2, RADIUS, mPaint);

        canvas.drawText("1", getWidth() / 2 - RADIUS - OFFSET - 12, (getBottom() - getTop()) / 2 + 15, mTextPaint);
        canvas.drawText("2", getWidth() / 2 + RADIUS + OFFSET - 12, (getBottom() - getTop()) / 2 + 15, mTextPaint);
        canvas.restore();
    }
}
