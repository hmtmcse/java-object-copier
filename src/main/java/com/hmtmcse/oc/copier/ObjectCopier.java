package com.hmtmcse.oc.copier;

import java.lang.reflect.InvocationTargetException;

public class ObjectCopier {


    public <D> D copy(Object source, Class<D> destination) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return destination.getDeclaredConstructor().newInstance();
    }

}
