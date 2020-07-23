package com.cloud.hub.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class ReflectUtil {

    /**
     * 反射获取值
     * @param fieldName
     * @param clazz
     * @param target
     * @return
     */
    public static Object getFieldValue(String fieldName, Class clazz, Object target) {
        Field field = ReflectionUtils.findField(clazz, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                return field.get(target);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 反射获取值
     * @param field
     * @param target
     * @return
     */
    public static Object getFieldValue(Field field, Object target) {
        field.setAccessible(true);
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 反射赋值
     * @param fieldName
     * @param value
     * @param clazz
     * @param target
     */
    public static void setFieldValue(String fieldName, Object value, Class clazz, Object target) {
        Field field = ReflectionUtils.findField(clazz, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(target, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T instanceClazz(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
