package com.mywork.dorm.Utils;


import java.lang.reflect.Field;

public class CopyClassUtil {
    /**
     * 将源对象的数据复制到目标对象。
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <T>    源对象的类型
     * @param <U>    目标对象的类型
     */
    public static<T, U> void copyClass(T source, U target) {
        // 检查源对象和目标对象是否为 null
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source or Target cannot be null");
        }

        // 获取源对象和目标对象的类信息
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        // 获取源对象的所有字段
        Field[] sourceFields = sourceClass.getDeclaredFields();
        // 遍历源对象的字段
        for (Field sourceField : sourceFields) {
            try {
                // 设置字段为可访问，以便可以读取其值
                sourceField.setAccessible(true);
                // 获取源对象中当前字段的值
                Object value = sourceField.get(source);

                // 尝试在目标对象中找到同名的字段
                Field targetField = targetClass.getDeclaredField(sourceField.getName());
                // 设置目标字段为可访问，以便可以写入新值
                targetField.setAccessible(true);
                // 将值复制到目标对象的对应字段
                // 获取对象的值时，必须是Object类型，但是在赋值时，Java会自动尝试进行拆装箱操作，进行类型转换
                targetField.set(target, value);
            } catch (NoSuchFieldException e) {
                // 如果目标类没有这个字段，可以选择忽略或记录日志
                System.out.println("Field " + sourceField.getName() + " not found in target class.");
            } catch (IllegalAccessException e) {
                // 捕获非法访问异常（一般情况下不会发生）
                e.printStackTrace();
            }
        }
    }
}
