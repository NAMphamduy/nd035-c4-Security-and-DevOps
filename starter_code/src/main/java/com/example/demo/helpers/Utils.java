package com.example.demo.helpers;

import java.lang.reflect.Field;

public class Utils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {
        try {
            boolean wasPrivate = false;
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, toInject);
            f.setAccessible(false);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            e.getCause();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
