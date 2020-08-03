package com.hmtmcse.oc.copier;

import com.hmtmcse.oc.common.ObjectCopierException;
import com.hmtmcse.oc.data.CopyReport;
import com.hmtmcse.oc.data.CopyReportError;
import com.hmtmcse.oc.reflection.ReflectionProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
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

    private Field compareReportAndGetObjectField(Field field, Object object, String nestedKey) {
        Field newInstanceField = reflectionProcessor.getAllFieldFromObject(object, field.getName());
        field.setAccessible(true);
        if (newInstanceField == null) {
            addReport(field.getName(), CopyReportError.DST_PROPERTY_UNAVAILABLE.label, nestedKey);
        } else if (field.getType() != newInstanceField.getType()) {
            addReport(field.getName(), CopyReportError.DATA_TYPE_MISMATCH.label, nestedKey);
            newInstanceField = null;
        } else {
            newInstanceField.setAccessible(true);
        }
        return newInstanceField;
    }


    private Object processAndGetValue(Object object, Field field) throws IllegalAccessException, ObjectCopierException {
        if (reflectionProcessor.isPrimitive(field.getType())) {
            return field.get(object);
        } else if (reflectionProcessor.isMap(field.getType())) {

        } else if (reflectionProcessor.isList(field.getType())) {

        } else {
            Object nestedObject = field.get(object);
            if (nestedObject != null) {
                return copy(nestedObject, field.getType(), field.getName());
            }
        }
        return null;
    }

    public <D> D copy(Object source, Class<D> destination, String nestedKey) throws ObjectCopierException {
        try {
            if (source == null) {
                return null;
            }

            D newInstance = reflectionProcessor.newInstance(destination);
            Field newField;
            for (Field objectField : reflectionProcessor.getAllField(source.getClass())) {
                newField = compareReportAndGetObjectField(objectField, newInstance, nestedKey);
                if (newField != null) {
                    newField.setAccessible(true);
                    newField.set(newInstance, processAndGetValue(source, objectField));
                }
            }
            return newInstance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ObjectCopierException(e.getMessage());
        }
    }

    public <D> D copy(Object source, Class<D> destination) throws ObjectCopierException {
        return copy(source, destination, null);
    }

}
