package com.zhangwx.z_utils.Z_Utils;

import android.util.Log;

import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zhangwx on 2017/6/3.
 * 1. 写代码的时候，尽量不要使用finalize方法。
 * 2. 如果没办法必须要用，那么考虑使用 FinalizerHelper 删除无用对象。
 */

public class FinalizerHelper {
    private static final String TAG = "FinalizerHelper";
    private Class<?> mFinalizerClazz = null;
    private Field mUnFinalizedField = null;
    private Field mNextField = null;
    private Field mReferentField = null;
    private Method mRemoveMethod = null;
    private static FinalizerHelper sInstance = new FinalizerHelper();
    private static final Object sLock = new Object();

    public static synchronized FinalizerHelper getInstance() {
        return sInstance;
    }

    public FinalizerHelper() {

        try {
            mFinalizerClazz = Class.forName("java.lang.ref.FinalizerReference");

            // the start into the linked list of finalizers
            mUnFinalizedField = mFinalizerClazz.getDeclaredField(
                    "head");
            mUnFinalizedField.setAccessible(true);

            // the next element in the linked list
            mNextField = mFinalizerClazz.getDeclaredField("next");
            mNextField.setAccessible(true);

            // the object that the finalizer is defined on
            mReferentField = Reference.class.getDeclaredField("referent");
            mReferentField.setAccessible(true);

            // the remove method
            mRemoveMethod = mFinalizerClazz.getMethod("remove", mFinalizerClazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runFinalizers() {
        System.runFinalization();
    }

    public void collectGarbage() {
        System.gc();
    }

    public void removeReference(Class<?> cls) {
        if (mFinalizerClazz == null ||
                mUnFinalizedField == null ||
                mReferentField == null ||
                mRemoveMethod == null ||
                mNextField == null)
            return;

        try {
            synchronized (sLock) {
                Object finalizer = mUnFinalizedField.get(null);
                Object tmpNext = null;
                while (finalizer != null) {
                    Object value = mReferentField.get(finalizer);
                    tmpNext = mNextField.get(finalizer);
                    if (value != null && value.getClass() == cls) {
                        Log.d(TAG, "Removing " + value.getClass().toString());
                        mRemoveMethod.invoke(null, finalizer);
                    }

                    finalizer = tmpNext;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
