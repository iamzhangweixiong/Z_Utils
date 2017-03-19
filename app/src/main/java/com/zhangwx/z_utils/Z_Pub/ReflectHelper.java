package com.zhangwx.z_utils.Z_Pub;

import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/9/5.
 */
public class ReflectHelper {
    /**
     * 通过构造器取得实例
     *
     * @param className    访问类名
     * @param intArgsClass 参数类型
     * @param intArgs      参数值
     * @return Object
     */
    public static Object invokeConstructor(ClassLoader loader, String className, Class[] intArgsClass, Object[] intArgs) {
        if (loader == null || TextUtils.isEmpty(className)) {
            return null;
        }

        Object obj = null;
        try {
            Class<?> classType = loader.loadClass(className);
            Constructor constructor = classType.getDeclaredConstructor(intArgsClass);
            constructor.setAccessible(true);
            obj = constructor.newInstance(intArgs);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    /**
     * 通过构造器取得实例
     *
     * @param className    访问类名
     * @param intArgsClass 参数类型
     * @param intArgs      参数值
     * @return Object
     */
    public static Object invokeConstructor(String className, Class[] intArgsClass,
                                           Object[] intArgs) {
        Object obj = null;
        try {
            Class<?> classType = Class.forName(className);
            Constructor constructor = classType.getDeclaredConstructor(intArgsClass);
            constructor.setAccessible(true);
            obj = constructor.newInstance(intArgs);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    /**
     * 修改成员变量的值
     *
     * @param object     访问对象
     * @param filedName  成员变量名
     * @param filedValue 修改值
     */
    public static void modifyFieldValue(Object object, String filedName,
                                        String filedValue) {
        Class classType = object.getClass();
        Field field;
        try {
            field = classType.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(object, filedValue);
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 访问类成员变量
     *
     * @param object 访问对象
     * @param member 成员变量名
     * @return Object       成员变量值
     */
    public static Object getFieldValue(Object object, String member) {
        Class classType = object.getClass();
        Field field;
        while (classType != Object.class) {
            try {
                field = classType.getDeclaredField(member);
                field.setAccessible(true);
                return field.get(object);
            } catch (Exception e) {
                classType = classType.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 调用类方法，包括私有
     *
     * @param object     访问对象
     * @param methodName 成员变量名
     * @param type       参数类型
     * @param value      参数值
     */
    public static Object invokeMethod(Object object, String methodName,
                                      Class[] type, Object[] value) {
        Class<?> classType = object.getClass();
        Object obj = null;
        try {
            Method method = classType.getDeclaredMethod(methodName, type);
            method.setAccessible(true);
            obj = method.invoke(object, value);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    /**
     * 调用类方法，包括私有
     *
     * @param object     访问对象
     * @param methodName 成员变量名
     * @param type       参数类型
     * @param value      参数值
     */
    public static Object invokeMethodStrictly(Object object, String methodName, Class[] type, Object[] value) {
        if (object == null || TextUtils.isEmpty(methodName)) {
            return null;
        }

        Method method = null;
        Object obj = null;

        int count = 0;
        for (Class<?> classType = object.getClass(); classType != Object.class; classType = classType.getSuperclass()) {
            try {
                if (count++ > 3) break;
                method = classType.getDeclaredMethod(methodName, type);
                break;
            } catch (NoSuchMethodException ex) {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (method != null) {
            try {
                method.setAccessible(true);
                obj = method.invoke(object, value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return obj;
    }

    public static Object invokeStaticMethod(Class<?> cls, String methodName,
                                            Class[] parameterTypes, Object[] args) {
        Method method = null;
        if (cls != null) {
            try {
                method = cls.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        Object retVal = null;
        if (method != null) {
            method.setAccessible(true);
            try {
                retVal = method.invoke(null, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retVal;
    }
}
