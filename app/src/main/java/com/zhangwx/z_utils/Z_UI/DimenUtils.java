package com.zhangwx.z_utils.Z_UI;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.zhangwx.z_utils.MyApplication;

import java.lang.reflect.Method;

/**
 * <P>
 *     屏幕参数及适配相关工具
 * </P>
 * Created by zhangwx on 2016/7/13.
 */
public class DimenUtils {
    public final static float BASE_SCREEN_HEIGHT = 1280f;
    public final static float BASE_SCREEN_DENSITY = 2f;
    public static DisplayMetrics mMetrics;
    public static final int LDPI = 1;
    public static final int MDPI = 2;
    public static final int HDPI = 3;
    public static final int XHDPI = 4;
    public static final int XXHDPI = 5;
    public static final int XXXHDPI = 6;
    public static final int OTHER_DPI = 7;
    public static final int NULL_DPI = 0;
    private static final int DP_TO_PX = TypedValue.COMPLEX_UNIT_DIP;
    private static final int SP_TO_PX = TypedValue.COMPLEX_UNIT_SP;
    private static final int PX_TO_DP = TypedValue.COMPLEX_UNIT_MM + 1;
    private static final int PX_TO_SP = TypedValue.COMPLEX_UNIT_MM + 2;
    private static final int DP_TO_PX_SCALE_H = TypedValue.COMPLEX_UNIT_MM + 3;
    public static Float sScaleH;
    public static float sDensity = 1.0f;
    public static int sDensityDpi;
    public static int sWidthPixels;
    public static int sHeightPixels;
    public static float sFontDensity;
    public static int sRealWidthPixels;
    public static int sRealHeightPixels;
    public static int sNavBarWidth; // 虚拟键宽度
    public static int sNavBarHeight; // 虚拟键高度
    private static Class sClass = null;
    // -- dimens convert

    private static Context context = MyApplication.getContext();

    private static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch (unit) {
            case DP_TO_PX:
            case SP_TO_PX:
                return TypedValue.applyDimension(unit, value, metrics);
            case PX_TO_DP:
                return value / metrics.density;
            case PX_TO_SP:
                return value / metrics.scaledDensity;
            case DP_TO_PX_SCALE_H:
                return TypedValue.applyDimension(DP_TO_PX, value * getScaleFactorH(), metrics);
        }
        return 0;
    }

    public static int dp2px(float value) {
        if (mMetrics == null) {
            if (context != null) {
                mMetrics = context.getResources().getDisplayMetrics();
            }
        }

        if (mMetrics != null) {
            return (int) applyDimension(DP_TO_PX, value, mMetrics);
        }
        return 0;
    }

    public static float getScaleFactorH() {
        if (sScaleH == null) {
            if (mMetrics == null) {
                if (context != null) {
                    mMetrics = context.getResources().getDisplayMetrics();
                }
            }

            if (mMetrics != null) {
                sScaleH = (mMetrics.heightPixels * BASE_SCREEN_DENSITY)
                        / (mMetrics.density * BASE_SCREEN_HEIGHT);
            }
        }
        return sScaleH != null ? sScaleH : 0.0f;
    }

    // -- window dimens

    public static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    private static int sStatusBarHeight = -1;

    public static int getWindowWidth() {
        if (mMetrics == null) {
            if (context != null) {
                mMetrics = context.getResources().getDisplayMetrics();
            }
        }
        if (mMetrics != null) {
            return mMetrics.widthPixels;
        }
        return 0;
    }

    public static int getWindowHeight() {
        if (mMetrics == null) {
            if (context != null) {
                mMetrics = context.getResources().getDisplayMetrics();
            }
        }
        if (mMetrics != null) {
            return mMetrics.heightPixels;
        }
        return 0;
    }

    public static int getScreenType() {
        if (mMetrics == null) {
            if (context != null) {
                mMetrics = context.getResources().getDisplayMetrics();
            }
        }

        if (mMetrics != null) {
            if (mMetrics.densityDpi > 240 && mMetrics.densityDpi <= 320) {
                return XHDPI;

            } else if (mMetrics.densityDpi > 320 && mMetrics.densityDpi <= 480) {
                return XXHDPI;

            } else if (mMetrics.densityDpi > 480 && mMetrics.densityDpi <= 640) {
                return XXXHDPI;

            } else if (mMetrics.densityDpi > 160 && mMetrics.densityDpi <= 240) {
                return HDPI;

            } else if (mMetrics.densityDpi > 120 && mMetrics.densityDpi <= 160) {
                return MDPI;

            } else if (mMetrics.densityDpi <= 120 && mMetrics.densityDpi > 0) {
                return LDPI;

            } else if (mMetrics.densityDpi > 640) {
                return XXXHDPI;

            } else if (mMetrics.densityDpi <= 0) {
                return OTHER_DPI;
            }
        }
        return NULL_DPI;
    }

    public static void resetDensity(Context context) {
        if (context != null && null != context.getResources()) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            sDensity = metrics.density;
            sFontDensity = metrics.scaledDensity;
            sWidthPixels = metrics.widthPixels;
            sHeightPixels = metrics.heightPixels;
            sDensityDpi = metrics.densityDpi;
            try {
                getStatusBarHeight(context);
            } catch (Error e) {
                Log.i("DrawUtils", "resetDensity has error" + e.getMessage());
            }
        }
        resetNavBarHeight(context);
    }

    public static int getStatusBarHeight(Context context) {
        if (sStatusBarHeight != -1) {
            return sStatusBarHeight;
        }

        //这个方法更快
        try {
            sStatusBarHeight = Resources.getSystem().getDimensionPixelSize(
                    Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

//        Class<?> c;
//        Object obj;
//        Field field;
//        int x;
//        int top;
//        try {
//            c = Class.forName("com.android.internal.R$dimen");
//            obj = c.newInstance();
//            field = c.getField("status_bar_height");
//            x = Integer.parseInt(field.get(obj).toString());
//            top = context.getResources().getDimensionPixelSize(x);
//            sStatusBarHeight = top;
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
        return sStatusBarHeight;
    }

    private static void resetNavBarHeight(Context context) {
        if (context != null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            try {
                if (sClass == null) {
                    sClass = Class.forName("android.view.Display");
                }
                Point realSize = new Point();
                Method method = sClass.getMethod("getRealSize", Point.class);
                method.invoke(display, realSize);
                sRealWidthPixels = realSize.x;
                sRealHeightPixels = realSize.y;
                sNavBarWidth = realSize.x - sWidthPixels;
                sNavBarHeight = realSize.y - sHeightPixels;
            } catch (Exception e) {
                sRealWidthPixels = sWidthPixels;
                sRealHeightPixels = sHeightPixels;
                sNavBarHeight = 0;
            }
        }
    }

    public static int getRealWidth() {
        if (sWidthPixels <= 0) {
            resetDensity(context);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return sRealWidthPixels;
        }
        return sWidthPixels;
    }

    public static int getRealHeight() {
        if (sHeightPixels <= 0) {
            resetDensity(context);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return sRealHeightPixels;
        }
        return sHeightPixels;
    }
}
