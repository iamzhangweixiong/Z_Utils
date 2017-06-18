package com.zhangwx.z_utils.Z_Intent.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.zhangwx.z_utils.Z_UI.DimenUtils;


/**
 * @author shidefeng
 * @since 2016/4/20
 */
public class TickView extends View {

    private Paint mPaint        = new Paint();
    private RectF mRect         = new RectF();
    private Path mPath          = new Path();
    private float mRectCx       = DimenUtils.dp2px(5f);
    private float mRectCy       = DimenUtils.dp2px(5f);
    private float mRectWidth    = DimenUtils.dp2px(1f);
    private float mTickWidth    = DimenUtils.dp2px(2.5f);
    private float mTickOffset   = DimenUtils.dp2px(4.5f);

    private float mAlpha;
    private int mTickColor;


    public TickView(Context context) {
        this(context, null);
    }

    public TickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTickColor = 0xff3488EB;

        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setPathEffect(new CornerPathEffect(DimenUtils.dp2px(1f)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int height = getHeight();
        final int width = getWidth();

        mPaint.setAlpha(255);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(mRectWidth);
        mRect.set(0 + mRectWidth, 0 + mRectWidth, width - mRectWidth, height - mRectWidth);
        canvas.drawRoundRect(mRect, mRectCx, mRectCy, mPaint);

        final int alpha = Math.round((mAlpha) * (mTickColor >>> 24));
        mPaint.setColor(mTickColor & 0x00ffffff | (alpha << 24));
        mPaint.setStrokeWidth(mTickWidth);
        mPath.moveTo(mTickOffset, height >> 1);
        mPath.lineTo(mTickOffset + (height >> 2), (height >> 2) + (height >> 1));
        mPath.lineTo(width - mTickOffset, height >> 2);
        canvas.drawPath(mPath, mPaint);
        mPath.reset();
    }

    @Override
    public void setAlpha(@FloatRange(from=0.0, to=1.0) float alpha) {
        mAlpha = alpha;
        ViewCompat.postInvalidateOnAnimation(this);
    }

}
