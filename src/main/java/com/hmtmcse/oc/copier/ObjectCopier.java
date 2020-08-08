package com.hmtmcse.oc.copier;

import com.hmtmcse.oc.common.ObjectCopierException;
import com.hmtmcse.oc.data.CopyReport;
import com.hmtmcse.oc.data.CopyReportError;
import com.hmtmcse.oc.reflection.ReflectionProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;

public class ObjectCopier {

    private Boolean isValidateSource = true;
    private Boolean isValidateDestination = true;
    private LinkedHashMap<String, CopyReport> errorReports = new LinkedHashMap<>();
    private ReflectionProcessor reflectionProcessor;

    public ObjectCopier() {
        reflectionProcessor = new ReflectionProcessor();
    }

    public ObjectCopier disableValidateSource() {
        isValidateSource = false;
        return this;
    }

    public ObjectCopier disableValidateDestination() {
        isValidateDestination = false;
        return this;
    }

    public LinkedHashMap<String, CopyReport> getErrorReports() {
        return this.errorReports;
    }

    private void validateObject(Object object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validator.validate(object);
    }

    private void addReport(String name, String errorType, String nestedKey) {
        if (nestedKey == null) {
            errorReports.put(name, new CopyReport(name, errorType));
        } else {
            if (errorReports.get(nestedKey) != null) {
                errorReports.get(nestedKey).addNestedReport(new CopyReport(name, errorType));
            }
        }
    }

    private String getSourceFieldName(Field field) {
        return field.getName();
    }

    private Field compareReportAndGetObjectField(Field field, Object object, String nestedKey) {
        String sourceFieldName = getSourceFieldName(field);
        Field objectField = reflectionProcessor.getAnyFieldFromObject(object, sourceFieldName);
        field.setAccessible(true);
        if (objectField == null) {
            addReport(sourceFieldName, CopyReportError.DST_PROPERTY_UNAVAILABLE.label, nestedKey);
        } else if (field.getType() != objectField.getType()) {
            addReport(field.getName(), CopyReportError.DATA_TYPE_MISMATCH.label, nestedKey);
            objectField = null;
        } else {
            objectField.setAccessible(true);
        }
        return objectField;
    }


    private Object processMap(Object object, Field field) {

        return null;
    }

    private Object processList(Object sourceObject, Class<?> destinationProperty) throws IllegalAccessException, ObjectCopierException {
        if (sourceObject == null || destinationProperty == null) {
            return null;
        }
        Collection<?> list = (Collection<?>) sourceObject;
        Collection response = reflectionProcessor.instanceOfList(destinationProperty);
        for (Object data : list) {
            if (data != null) {
                response.add(processAndGetValue(data, data.getClass()));
            }
        }
        if (response.size() == 0) {
            return null;
        }
        return response;
    }

    private Object processSet(Object object, Field field) {
        return null;
    }

    private Object processQueue(Object object, Field field) {
        return null;
    }


    private Object processAndGetValue(Object fieldObject, Class<?> destinationProperty) throws IllegalAccessException, ObjectCopierException {
        if (fieldObject == null) {
            return null;
        } else if (reflectionProcessor.isPrimitive(destinationProperty)) {
            return fieldObject;
        } else if (reflectionProcessor.isMap(destinationProperty)) {

        } else if (reflectionProcessor.isList(destinationProperty)) {
            return processList(fieldObject, destinationProperty);
        } else if (reflectionProcessor.isSet(destinationProperty)) {

        } else if (reflectionProcessor.isQueue(destinationProperty)) {

        }
        return copy(fieldObject, destinationProperty, destinationProperty.getSimpleName());
    }

    private Object processAndGetValue(Object object, Field field) throws IllegalAccessException, ObjectCopierException {
        return processAndGetValue(field.get(object), field.getType());
    }

    public <D> D copy(Object fromObject, Class<D> toKlass, String nestedKey) throws ObjectCopierException {
        try {
            if (fromObject == null) {
                return null;
            }

            D toInstance = reflectionProcessor.newInstance(toKlass);
            Field fromField;
            for (Field toField : reflectionProcessor.getAllField(toKlass)) {
                fromField = compareReportAndGetObjectField(toField, fromObject, nestedKey);
                if (fromField != null) {
                    fromField.setAccessible(true);
                    toField.set(toInstance, processAndGetValue(fromObject, fromField));
                }
            }
            return toInstance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ObjectCopierException(e.getMessage());
        }
    }

    public <D> D copy(Object source, Class<D> destination) throws ObjectCopierException {
        return copy(source, destination, null);
    }

}
