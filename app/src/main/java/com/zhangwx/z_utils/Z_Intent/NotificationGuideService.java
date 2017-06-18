package com.zhangwx.z_utils.Z_Intent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_Intent.UI.AccessGuideAnimHelper;
import com.zhangwx.z_utils.Z_Intent.UI.TickView;
import com.zhangwx.z_utils.Z_Intent.UI.ToggleView;

/**
 * Created by zhangwx
 * on 2017/6/12.
 * TODO bugfix
 */

public class NotificationGuideService extends Service {

    public static final String TAG = "NotificationGuideService";

    private boolean mShowTick = false;
    private View mGuideView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    AccessGuideAnimHelper mAnimHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = getLayoutParams();
        mGuideView = View.inflate(this, R.layout.notify_access_guide, null);
        mGuideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addView(mGuideView, mLayoutParams);
        initView();
    }

    private void initView() {
        final View fingerView = mGuideView.findViewById(R.id.finger);
        final ToggleView toggleView = (ToggleView) mGuideView.findViewById(R.id.toggleView);
        final TickView tickView = (TickView) mGuideView.findViewById(R.id.tick);
        if (mShowTick) {
            tickView.setVisibility(View.VISIBLE);
        } else {
            toggleView.setVisibility(View.VISIBLE);
        }
        mAnimHelper = new AccessGuideAnimHelper(mShowTick);
        mAnimHelper.setTargetViews(fingerView, tickView, toggleView);
        mAnimHelper.startGuideAnim();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAnimHelper.clear();
        removeView(mGuideView);
    }

    private WindowManager.LayoutParams getLayoutParams() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        params.gravity = Gravity.CENTER;

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.RGBA_8888;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        return params;
    }

    private void addView(View view, WindowManager.LayoutParams params) {
        try {
            mWindowManager.addView(view, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeView(View view) {
        try {
            mWindowManager.removeView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
