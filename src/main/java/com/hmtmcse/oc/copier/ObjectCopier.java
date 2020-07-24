package com.hmtmcse.oc.copier;

import java.lang.reflect.InvocationTargetException;

public class ObjectCopier {

    public Boolean isValidateSource = true;
    public Boolean isValidateDestination = true;

    public ObjectCopier setValidateSource(Boolean validateSource) {
        isValidateSource = validateSource;
        return this;
    }

    public ObjectCopier setValidateDestination(Boolean validateDestination) {
        isValidateDestination = validateDestination;
        return this;
    }

    public <D> D copy(Object source, Class<D> destination) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return destination.getDeclaredConstructor().newInstance();
    }

}
