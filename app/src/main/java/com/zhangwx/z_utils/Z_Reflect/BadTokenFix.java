package com.zhangwx.z_utils.Z_Reflect;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.zhangwx.z_utils.Z_LifeCycle.LifecycleCallbacksHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 另一种解决方案参考头条 https://mp.weixin.qq.com/s/DmN9sN_MdugpVm73vnVqww，侵入性更低
 */
public class BadTokenFix {
    public static final String TAG = "BadTokenFix";
    public static final int TYPE_BASE_APPLICATION = 1;
    public static final int FIRST_SUB_WINDOW = 1000;
    public static final int FIRST_SYSTEM_WINDOW = 2000;

    public static final int ADD_BAD_APP_TOKEN = -1;
    public static final int ADD_BAD_SUB_WINDOW_TOKEN = -2;
    public static final int ADD_STARTING_NOT_NEEDED = -6;

    public static void fix(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }
        try {
            Class<?> clazz = Class.forName("android.view.WindowManagerGlobal");
            Object rawWindowSession = ReflectHelper.invokeStaticMethod(
                    clazz, "getWindowSession", new Class<?>[]{}, new Object[]{});

            Field sWindowSession = clazz.getDeclaredField("sWindowSession");
            sWindowSession.setAccessible(true);

            Class<?> iWindowSession = Class.forName("android.view.IWindowSession");
            Object object = Proxy.newProxyInstance(
                    context.getClassLoader(),
                    new Class<?>[]{iWindowSession},
                    new WindowSessionInvocationHandler(rawWindowSession));

            sWindowSession.set(null, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class WindowSessionInvocationHandler implements InvocationHandler {
        private Object mRawWindowSession;

        WindowSessionInvocationHandler(Object rawWindowSession) {
            mRawWindowSession = rawWindowSession;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method == null) {
                return null;
            }
            String methodName = method.getName();
            if (TextUtils.isEmpty(methodName)) {
                return null;
            }

            Log.d(TAG, method.getName());
            if (args != null && args.length > 0) {
                for (Object object : args) {
                    Log.d(TAG, "arg = " + object);
                }
            }

            try {
                if (methodName.contains("addToDisplay") && args != null && args.length > 3) {
                    //      {
                    //      (0,0)(fillxfill)
                    //      sim={forwardNavigation}
                    //      ty=BASE_APPLICATION
                    //      wanim=0x10302fe
                    //      fl=LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS
                    //      pfl=FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED
                    //      fitSides=
                    //      }
                    Object object = args[2];
                    Object type = ReflectHelper.getFieldValue(object, "type");
                    int t = (int) type;
                    int ret = (int) method.invoke(mRawWindowSession, args);
                    if (t >= FIRST_SUB_WINDOW && t < FIRST_SYSTEM_WINDOW) {
                        if (ret == ADD_BAD_APP_TOKEN || ret == ADD_BAD_SUB_WINDOW_TOKEN) {
                            return ADD_STARTING_NOT_NEEDED;
                        }
                    }

                    if (t == TYPE_BASE_APPLICATION) {
                        if (ret == ADD_BAD_APP_TOKEN || ret == ADD_BAD_SUB_WINDOW_TOKEN) {
                            Activity activity = LifecycleCallbacksHandler.getCurrentActivity();
                            if (activity != null) {
                                activity.finish();
                                return ADD_STARTING_NOT_NEEDED;
                            }
                        }
                    }
                    return ret;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                return method.invoke(mRawWindowSession, args);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

