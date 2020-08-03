package com.hmtmcse.oc.copier;

import java.lang.reflect.InvocationTargetException;

public class ObjectCopier {

    public Boolean isValidateSource = true;
    public Boolean isValidateDestination = true;

    public ObjectCopier disableValidateSource() {
        isValidateSource = false;
        return this;
    }

    public ObjectCopier disableValidateDestination() {
        isValidateDestination = false;
        return this;
    }

    public <D> D copy(Object source, Class<D> destination) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return destination.getDeclaredConstructor().newInstance();
    }

}
