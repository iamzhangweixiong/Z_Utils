package com.zhangwx.z_utils.Z_TouchEvent;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by zhangwx on 2017/1/4.
 */
public class TouchButton extends AppCompatButton implements View.OnTouchListener {
    public static final String TAG = "ZHANG";

    public TouchButton(Context context) {
        super(context);
    }

    public TouchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_BUTTON_PRESS:
                Log.e(TAG, "dispatchTouchEvent: ACTION_BUTTON_PRESS");
                break;
            case MotionEvent.ACTION_BUTTON_RELEASE:
                Log.e(TAG, "dispatchTouchEvent: ACTION_BUTTON_RELEASE");
                break;
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "dispatchTouchEvent: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "dispatchTouchEvent: ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "dispatchTouchEvent: ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_BUTTON_PRESS:
                Log.e(TAG, "onTouchEvent: ACTION_BUTTON_PRESS");
                break;
            case MotionEvent.ACTION_BUTTON_RELEASE:
                Log.e(TAG, "onTouchEvent: ACTION_BUTTON_RELEASE");
                break;
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "onTouchEvent: ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouchEvent: ACTION_UP");
                break;
        }

        return super.onTouchEvent(event);
    }
}
