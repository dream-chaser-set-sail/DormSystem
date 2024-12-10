package com.mywork.dorm.Utils;

import java.lang.reflect.Field;

public class SetDefaultProfileImage {
    public static<T> T setHeadImage(T data) {
        Class<?> clazz = data.getClass();
        try {
            Field genderField = clazz.getDeclaredField("gender");
            Field imageField = clazz.getDeclaredField("image");
            genderField.setAccessible(true);
            imageField.setAccessible(true);
            String gender = (String) genderField.get(data);
            String image = (String) imageField.get(data);

            if (image != null && !image.equals("")) {
                return data;
            }

            if (gender.equals("男")) {
                imageField.set(data, "https://java-v-z.oss-cn-beijing.aliyuncs.com/909abebeb4f5498893135b2136b7844d.jpeg");
            } else if (gender.equals("女")) {
                imageField.set(data, "https://java-v-z.oss-cn-beijing.aliyuncs.com/de710b248b7a47bd9800385be00548ac.jpeg");
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
