package com.zhangwx.z_utils.Z_Intent.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhangwx
 * on 16-3-30.
 */
public class ToggleView extends View {

    private static final String TAG = "ToggleView";
    private Paint mPaint;
    private Paint mRipplePaint;
    private Paint mCirclePaint;

    private float mCoef;
    private float mDensity;
    private boolean mToggling;
    private boolean mFinish;
    private int mRippleColor;

    public ToggleView(Context context) {
        this(context, null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(0xffb0b0b0); //0xff18ffff
        mCirclePaint.setShadowLayer(5, 3, 3, 0x80000000);
        setLayerType(LAYER_TYPE_SOFTWARE, mCirclePaint);
        mPaint = new Paint();
        mPaint.setColor(0xff868686); //#00bcd4
        mRipplePaint = new Paint();
        mRipplePaint.setColor(0x7787d065);
        mRippleColor = 0xffb0b0b0;
        mDensity = getResources().getDisplayMetrics().density;
    }

    public void setCoef(float coef) {
        mCoef = coef;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public float getCoef() {
        return mCoef;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float height = getHeight();
        float width = getWidth();
        if (mToggling) {
            mPaint.setColor(0xff5ba547);
            mCirclePaint.setColor(0xff87d065);
        }
        // draw background
        final float rectW = 20*mDensity;
        final float rectH = 10*mDensity;
        final float rectR = 5*mDensity;
        float paddingTop = (height - rectH) / 2;
        float paddingLeft = (width - rectW) / 2;
        canvas.drawCircle(paddingLeft, height/2f, rectR, mPaint);
        canvas.drawCircle(width - paddingLeft, height/2f, rectR, mPaint);
        canvas.drawRect(paddingLeft, paddingTop, width - paddingLeft, height - paddingTop, mPaint);

        // draw access_helper_circle button
        final float radius = rectH / 1.2f;
        canvas.drawCircle(paddingLeft + mCoef * (width - 2*paddingLeft), height/2f, radius, mCirclePaint);

        // draw ripple
        if (mToggling && !mFinish) {
            final int alpha = Math.round((1 - mCoef) * (mRippleColor >>> 24));
            mRipplePaint.setColor(mRippleColor & 0x00ffffff | (alpha << 24));
            final float rippleRadius = rectH * 2f;
            canvas.drawCircle(width/2f, height/2f, mCoef * rippleRadius, mRipplePaint);
        }
    }

    public void startToggle() {
        mToggling = true;
        mCoef = 0.f;
    }

    public void init() {
        mToggling = false;
        mFinish = false;
        mCoef = 0.f;
        mCirclePaint.setColor(0xffb0b0b0); //0xff18ffff
        mPaint.setColor(0xff868686); //#00bcd4
        invalidate();
    }

    public void stopToggle() {
        mFinish = true;
    }
}
