package com.zhangwx.z_utils.Z_Reflect;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.zhangwx.z_utils.R;
import com.zhangwx.z_utils.Z_UI.ViewUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by zhangwx on 2017/4/12.
 */

public class ReflectActivity extends Activity {

    private ReflectTest reflectTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflect);
        TextView tv = ViewUtils.$(this, R.id.reflect_result);

//        reflectTest = new ReflectTest();

//        Class<?> clazz = reflectTest.getClass();

//        try {
//            Field field = clazz.getField("mTypeTwo");
//            field.setAccessible(true);
//            field.set(reflectTest, "mTypeThree");
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

//        try {
//            Method method = clazz.getMethod("setTypeTwo", String.class);
//            method.setAccessible(true);
//            method.invoke(reflectTest, "zhang");
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

        try {
            Class<?> clazz = Class.forName("com.zhangwx.z_utils.Z_Reflect.ReflectTest");
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            reflectTest = (ReflectTest) constructor.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        tv.setText(reflectTest.getTypeTwo());
    }
}
