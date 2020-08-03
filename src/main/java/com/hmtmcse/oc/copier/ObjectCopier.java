package com.hmtmcse.oc.copier;

import com.hmtmcse.oc.common.ObjectCopierException;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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

    private void validateObject(Object object){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validator.validate(object);
    }

    public <D> D copy(Object source, Class<D> destination) throws ObjectCopierException {
        try {
            return destination.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ObjectCopierException(e.getMessage());
        }
    }

}
