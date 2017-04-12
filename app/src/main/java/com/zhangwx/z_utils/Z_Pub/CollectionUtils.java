package com.zhangwx.z_utils.Z_Pub;

import android.support.v4.util.LongSparseArray;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Created by zhangwx on 2016/9/5.
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(LongSparseArray<String> array) {
        return array == null || array.size() == 0;
    }

    public static boolean isMapKeyEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty() || map.keySet()==null || map.keySet().isEmpty();
    }

    public static Object getFirstElement(Collection<?> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        for (Object object : collection) {
            return object;
        }
        return null;
    }

    /**
     * 获取list的一个区间集合
     * @param sourceList 源集合
     * @param index 起始index
     * @param length 长度
     * @return 1.如果源数据为空，或者参数异常，则返回null;
     *         2.如果length超出sourceList,则返回所能截取的集合;
     *         3.否则返回截取区间.
     */
    public static <T> List<T> subList(List<T> sourceList, int index, int length) {
        if (isEmpty(sourceList) || index < 0 || length < 0 || index > sourceList.size() - 1) {
            return null;
        }
        final int maxLength = sourceList.size() - index;
        if (length > maxLength) {
            return sourceList.subList(index, sourceList.size());
        } else {
            return sourceList.subList(index, index + length);
        }
    }

}
