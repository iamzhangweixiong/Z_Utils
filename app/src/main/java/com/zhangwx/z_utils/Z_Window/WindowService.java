package com.zhangwx.z_utils.Z_Window;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zhangwx.z_utils.R;

/**
 * <p>
 * Created by zhangwx on 2017/3/19.
 * <p>
 * LayoutParams.type 的常用属性：
 * <p>
 * 1、TYPE_PHONE：显示在所有应用之上，在状态栏之下；
 * <p>
 * 2、TYPE_PRIORITY_PHONE：电话优先，当锁屏时显示。此窗口不能获得输入焦点，否则影响锁屏；
 * <p>
 * 3、TYPE_SYSTEM_ALERT：系统提示，它总是出现在应用程序窗口之上；
 * <p>
 * 4、TYPE_SYSTEM_ERROR：系统内部错误提示，显示于所有内容之上；
 * <p>
 * 5、TYPE_SYSTEM_OVERLAY：系统顶层窗口，显示在其他一切内容之上，此窗口不能获得输入焦点，否则影响锁屏；
 * <p>
 * 以上5种类型都需要 android.Manifest.permission.SYSTEM_ALERT_WINDOW 权限、
 * (Android 6.0+：设置——应用——右上角齿轮——在其他应用的上层显示)；
 * <p>
 * 6、TYPE_TOAST：特殊类型，不需要权限都能显示
 * <p>
 * <p>
 * LayoutParams.flags 的常用属性：
 * <p>
 * 1、FLAG_NOT_FOCUSABLE：设置这个属性使后面的View可以获得焦点；
 * <p>
 * 2、FLAG_ALT_FOCUSABLE_IM ：如果同时设置了FLAG_NOT_FOCUSABLE选项和本选项，窗口将能够与输入法交互，允许输入法窗口覆盖；如果FLAG_NOT_FOCUSABLE没有设置而设置了本选项，窗口不能与输入法交互，可以覆盖输入法窗口。
 * <p>
 * 3、FLAG_NOT_TOUCHABLE：设置这个属性使后面的View可以获得触碰事件；
 * <p>
 * 4、FLAG_KEEP_SCREEN_ON：当此窗口为用户可见时，保持设备常开，并保持亮度不变；
 * <p>
 * 5、FLAG_LAYOUT_NO_LIMITS：不限制窗口的显示，允许窗口扩展到屏幕之外；
 * <p>
 * 6、FLAG_FULLSCREEN：窗口显示时，隐藏所有的屏幕装饰（例如状态条）、使窗口占用整个显示区域；
 * <p>
 * 7、FLAG_LAYOUT_IN_SCREEN：窗口占满整个屏幕，除了周围的装饰边框（例如状态栏）；
 * <p>
 * 8、FLAG_SHOW_WHEN_LOCKED：只有是全屏的window才起作用!!!当屏幕锁定时，窗口可以被看到。这使得应用程序窗口优先于锁屏界面。可配合FLAG_KEEP_SCREEN_ON选项点亮屏幕并直接显示在锁屏界面之前。
 */
public class WindowService extends Service {

    public static final String TAG = "WindowService";
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    public WindowService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceivers();
        initView();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void registerReceivers() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mReceiver, filter);
    }

    private void initView() {
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mLayoutParams.gravity = Gravity.CENTER;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                Log.e(TAG, "onReceive: " + "ACTION_SCREEN_OFF");
            } else if (action.equals(Intent.ACTION_USER_PRESENT)) {
                Log.e(TAG, "onReceive: " + "ACTION_USER_PRESENT");
                final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.window, null);
                view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWindowManager.removeView(view);
                    }
                });
                mWindowManager.addView(view, mLayoutParams);
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                Log.e(TAG, "onReceive: " + "ACTION_SCREEN_ON");
            }
        }
    };


}
