package com.hmtmcse.oc.copier;

import com.hmtmcse.oc.annotation.DataMapping;
import com.hmtmcse.oc.common.ObjectCopierException;
import com.hmtmcse.oc.data.CopyReport;
import com.hmtmcse.oc.data.CopyReportError;
import com.hmtmcse.oc.data.CopySourceDstField;
import com.hmtmcse.oc.reflection.ReflectionProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

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
        return getSourceFieldName(field, false);
    }

    private String getSourceFieldName(Field field, Boolean isStrictAnnotationCheck) {
        if (field.getAnnotation(DataMapping.class) != null) {
            String name = field.getAnnotation(DataMapping.class).source();
            if (!name.isEmpty()) {
                return name;
            }
        }
        return field.getName();
    }

    private CopySourceDstField compareReportAndGetObjectField(CopySourceDstField copySourceDstField, String nestedKey) {
        copySourceDstField.source.setAccessible(true);
        if (copySourceDstField.source == null) {
            addReport(copySourceDstField.sourceFieldName, CopyReportError.DST_PROPERTY_UNAVAILABLE.label, nestedKey);
        } else if (copySourceDstField.source.getType() != copySourceDstField.destination.getType()) {
            addReport(copySourceDstField.source.getName(), CopyReportError.DATA_TYPE_MISMATCH.label, nestedKey);
            copySourceDstField.source = null;
        } else {
            copySourceDstField.source.setAccessible(true);
        }

        if (copySourceDstField.destination != null) {
            copySourceDstField.destination.setAccessible(true);
        }

        return copySourceDstField;
    }

    private CopySourceDstField compareReportAndGetObjectField(Field field, Object object, String nestedKey) {
        return compareReportAndGetObjectField(field, object.getClass(), nestedKey);
    }

    private CopySourceDstField compareReportAndGetObjectField(Field field, Class<?> klass, String nestedKey) {
        String sourceFieldName = getSourceFieldName(field);
        if (sourceFieldName == null) {
            return null;
        }
        Field objectField = reflectionProcessor.getAnyFieldFromKlass(klass, sourceFieldName);
        return compareReportAndGetObjectField(new CopySourceDstField(field, objectField, sourceFieldName), nestedKey);
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

    private Boolean isMapperAvailable(List<Field> fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(DataMapping.class)) {
                return true;
            }
        }
        return false;
    }

    private List<CopySourceDstField> mapNonAnnotatedToAnnotated(List<Field> annotatedFields, Object nonAnnotatedObject, String nestedKey) {
        List<CopySourceDstField> list = new ArrayList<>();
        CopySourceDstField copySourceDstField;
        for (Field field : annotatedFields) {
            copySourceDstField = compareReportAndGetObjectField(field, nonAnnotatedObject, nestedKey);
            if (copySourceDstField != null && copySourceDstField.source != null) {
                list.add(copySourceDstField);
            }
        }
        return list;
    }

    private List<CopySourceDstField> mapAnnotatedToNonAnnotated(List<Field> annotatedFields, Class<?> nonAnnotatedKlass, String nestedKey) {
        List<CopySourceDstField> list = new ArrayList<>();
        CopySourceDstField copySourceDstField;
        for (Field field : annotatedFields) {
            copySourceDstField = compareReportAndGetObjectField(field, nonAnnotatedKlass, nestedKey);
            if (copySourceDstField != null && copySourceDstField.destination != null) {
                list.add(new CopySourceDstField(copySourceDstField.source, copySourceDstField.destination, copySourceDstField.sourceFieldName));
            }
        }
        return list;
    }


    private List<CopySourceDstField> mapCommonField(List<Field> fields, Object object, String nestedKey) {
        return mapNonAnnotatedToAnnotated(fields, object, nestedKey);
    }

    private List<CopySourceDstField> getFieldMapping(Object fromObject, Class<?> toKlass, String nestedKey) {

        List<Field> toKlassFields = reflectionProcessor.getAllField(toKlass);
        if (isMapperAvailable(toKlassFields)) {
            return mapNonAnnotatedToAnnotated(toKlassFields, fromObject, nestedKey);
        }

        List<Field> fromObjectFields = reflectionProcessor.getAllField(fromObject.getClass());
        if (isMapperAvailable(fromObjectFields)) {
            return mapAnnotatedToNonAnnotated(fromObjectFields, toKlass, nestedKey);
        }

        return mapCommonField(toKlassFields, fromObject, nestedKey);
    }

    public <D> D copy(Object fromObject, Class<D> toKlass, String nestedKey) throws ObjectCopierException {
        try {
            if (fromObject == null) {
                return null;
            }

            D toInstance = reflectionProcessor.newInstance(toKlass);
            List<CopySourceDstField> copySourceDstFields = this.getFieldMapping(fromObject, toKlass, nestedKey);
            for (CopySourceDstField copySourceDstField : copySourceDstFields) {
                copySourceDstField.destination.set(toInstance, processAndGetValue(fromObject, copySourceDstField.source));
            }
            return toInstance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ObjectCopierException(e.getMessage());
        }
    }

    public <D> D copy2(Object fromObject, Class<D> toKlass, String nestedKey) throws ObjectCopierException {
        try {
            if (fromObject == null) {
                return null;
            }

            D toInstance = reflectionProcessor.newInstance(toKlass);
            CopySourceDstField copySourceDstField;
            for (Field toField : reflectionProcessor.getAllField(toKlass)) {
                copySourceDstField = compareReportAndGetObjectField(toField, fromObject, nestedKey);
                if (copySourceDstField.destination != null) {
                    copySourceDstField.destination.setAccessible(true);
                    toField.set(toInstance, processAndGetValue(fromObject, copySourceDstField.destination));
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
