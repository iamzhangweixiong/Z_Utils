package com.zhangwx.z_utils.Z_UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zhangwx on 2016/9/5.
 */
public class ViewUtils {
    public static void recycleView(View view) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(null);
        } else {
            view.setBackground(null);
        }
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            imageView.setImageBitmap(null);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                recycleView(child);
            }
            viewGroup.removeAllViews();
        }
    }

    public static void setTouchDelegate(final View view, final int inset) {
        if (view == null || inset == 0) {
            return;
        }
        final ViewGroup parent = (ViewGroup) view.getParent();
        if (parent == null) {
            return;
        }
        parent.post(new Runnable() {
            @Override
            public void run() {
                Rect bound = new Rect();
                view.getHitRect(bound);
                bound.inset(inset, inset);
                parent.setTouchDelegate(new TouchDelegate(bound, view));
            }
        });
    }

    /**
     * @param inSetX 负值表示向左右两边扩大的点击区域
     * @param inSetY 负值表示向上下两边扩大的点击区域
     */
    public static void setTouchDelegate(final View view, final int inSetX, final int inSetY) {
        if (view == null || inSetX == 0 || inSetY == 0) {
            return;
        }
        final ViewGroup parent = (ViewGroup) view.getParent();
        if (parent == null) {
            return;
        }
        parent.post(new Runnable() {
            @Override
            public void run() {
                Rect bound = new Rect();
                view.getHitRect(bound);
                bound.inset(inSetX, inSetY);
                parent.setTouchDelegate(new TouchDelegate(bound, view));
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T $(View parent, int id) {
        return (T) parent.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T $(Activity act, int id) {
        return (T) act.findViewById(id);
    }

    public static final void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static final void hideInputMethod(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static final void setEditable(TextView textView, boolean editable) {
        textView.setFocusable(editable);
        textView.setCursorVisible(editable);
        textView.setFocusableInTouchMode(editable);
        textView.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    public static final void runOnPreDraw(final View view, final Runnable r) {
        if (r == null || view==null) return;
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                r.run();
                return false;
            }
        });
    }

    /**
     * 创建 View 的截图
     * @param view
     * @return
     */
    private Bitmap createSnapshot(@NonNull View view) {
        try {
            final Method method = View.class.getDeclaredMethod("createSnapshot", Bitmap.Config.class, int.class, boolean.class);
            method.setAccessible(true);
            return (Bitmap) method.invoke(view, Bitmap.Config.ARGB_8888, Color.TRANSPARENT, false);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No such method: getItemView");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Impossible 1");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Impossible 2");
        }
    }

}
