package com.hmtmcse.oc.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionProcessor {

    public List<Class<?>> getAllSuperClass(Class<?> klass) {
        List<Class<?>> classes = new ArrayList<>();
        if (klass != null) {
            Class<?> superclass = klass.getSuperclass();
            while (superclass != null) {
                classes.add(superclass);
                superclass = superclass.getSuperclass();
            }
        }
        return classes;
    }

    public List<Class<?>> getAllClass(Class<?> klass) {
        if (klass == null){
            return new ArrayList<>();
        }
        List<Class<?>> classes = getAllSuperClass(klass);
        classes.add(klass);
        return classes;
    }

    public List<Field> getAllField(Class<?> klass) {
        List<Field> fields = new ArrayList<>();
        if (klass != null) {
            List<Class<?>> classes = getAllClass(klass);
            for (Class<?> pClass : classes) {
                fields.addAll(Arrays.asList(pClass.getDeclaredFields()));
            }
        }
        return fields;
    }

    private Field getDeclaredField(Object object, String fieldName) {
        try {
            return object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignore) {
        }
        return null;
    }

    private Field getField(Object object, String fieldName) {
        try {
            return object.getClass().getField(fieldName);
        } catch (NoSuchFieldException ignore) {
        }
        return null;
    }

    public Field getFieldFromObject(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            field = getField(object, fieldName);
        }
        if (field != null) {
            field.setAccessible(true);
        }
        return field;
    }


    public <D> D newInstance(Class<D> klass) {
        try {
            return klass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore) {
        }
        return null;
    }


}
