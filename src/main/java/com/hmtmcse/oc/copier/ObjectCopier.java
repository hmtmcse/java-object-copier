package com.hmtmcse.oc.copier;

import com.hmtmcse.oc.annotation.DataMapping;
import com.hmtmcse.oc.annotation.DataMappingInfo;
import com.hmtmcse.oc.common.InitCustomProcessor;
import com.hmtmcse.oc.common.OCConstant;
import com.hmtmcse.oc.common.ObjectCopierException;
import com.hmtmcse.oc.common.ProcessCustomCopy;
import com.hmtmcse.oc.data.*;
import com.hmtmcse.oc.reflection.ReflectionProcessor;

import java.lang.reflect.Field;
import java.util.*;

public class ObjectCopier {

    private ReflectionProcessor reflectionProcessor;
    private LinkedHashMap<String, CopyReport> errorReports = new LinkedHashMap<>();
    public InitCustomProcessor initCustomProcessor = null;

    public ObjectCopier() {
        reflectionProcessor = new ReflectionProcessor();
    }


    private void addReport(String name, String errorType, String nestedKey) {
        if (name == null) {
            name = "Source or Destination";
        }
        if (nestedKey == null) {
            errorReports.put(name, new CopyReport(name, errorType));
        } else {
            if (errorReports.get(nestedKey) != null) {
                errorReports.get(nestedKey).addNestedReport(new CopyReport(name, errorType));
            }
        }
    }

    private Boolean isValidateTypeOrReport(CopySourceDstField copySourceDstField, String nestedKey) {
        Boolean isValid = false;
        if (copySourceDstField.source == null) {
            addReport(copySourceDstField.sourceFieldName, CopyReportError.DST_PROPERTY_UNAVAILABLE.label, nestedKey);
        }
        if (copySourceDstField.destination == null) {
            addReport(copySourceDstField.sourceFieldName, CopyReportError.DST_PROPERTY_UNAVAILABLE.label, nestedKey);
        } else if (copySourceDstField.source.getType() != copySourceDstField.destination.getType()) {
            addReport(copySourceDstField.source.getName(), CopyReportError.DATA_TYPE_MISMATCH.label, nestedKey);
        } else {
            isValid = true;
        }
        return isValid;
    }

    private Boolean isDataMapperAnnotationAvailable(Field field) {
        return field.isAnnotationPresent(DataMapping.class);
    }

    private Boolean isFieldCustomCall(Field field) {
        if (isDataMapperAnnotationAvailable(field)) {
            return field.getAnnotation(DataMapping.class).customProcess();
        }
        return false;
    }

    private String getSourceFieldName(Field field, Boolean isStrict) {
        if (isDataMapperAnnotationAvailable(field)) {
            return field.getAnnotation(DataMapping.class).source();
        }
        if (!isStrict) {
            return field.getName();
        }
        return null;
    }


    private Boolean isDataMapperAnnotationAvailable(List<Field> fields) {
        for (Field field : fields) {
            if (isDataMapperAnnotationAvailable(field)) {
                return true;
            }
        }
        return false;
    }

    private Boolean isDataMappingInfoAnnotation(Class<?> klass) {
        if (klass.isAnnotationPresent(DataMappingInfo.class)) {
            return true;
        }
        return false;
    }

    private Boolean isStrictMapping(Class<?> klass) {
        if (isDataMappingInfoAnnotation(klass)) {
            return klass.getAnnotation(DataMappingInfo.class).isStrict();
        }
        return OCConstant.isStrictCopy;
    }


    private String copierDefaultName(Class<?> klass) {
        if (isDataMappingInfoAnnotation(klass)) {
            return klass.getAnnotation(DataMappingInfo.class).name();
        }
        return OCConstant.copierDefaultName;
    }

    private Class<?> customProcessor(Class<?> klass) {
        if (isDataMappingInfoAnnotation(klass)) {
            return klass.getAnnotation(DataMappingInfo.class).customProcessor();
        }
        return null;
    }

    private ProcessCustomCopy<?, ?> initCustomProcessor(Object object) {
        Class<?> callbackClass = customProcessor(object.getClass());
        if (callbackClass == null || !ProcessCustomCopy.class.isAssignableFrom(callbackClass)) {
            return null;
        }
        ProcessCustomCopy<?, ?> customCopy = null;
        if (initCustomProcessor != null) {
            customCopy = initCustomProcessor.init(callbackClass);
        } else {
            customCopy = (ProcessCustomCopy<?, ?>) reflectionProcessor.newInstance(callbackClass);
        }
        return customCopy;
    }

    private ObjectCopierInfo processInfo(Object object) {
        ObjectCopierInfo objectCopierInfo = new ObjectCopierInfo();
        objectCopierInfo.isStrictMapping = isStrictMapping(object.getClass());
        objectCopierInfo.mappingClassName = copierDefaultName(object.getClass());
        objectCopierInfo.processCustomCopy = initCustomProcessor(object);
        return objectCopierInfo;
    }

    private Field getField(Field field, CopySourceDstField copySourceDstField) {
        copySourceDstField.sourceFieldName = getSourceFieldName(field, copySourceDstField.isStrictMapping);
        if (copySourceDstField.sourceFieldName != null && copySourceDstField.dataObject != null) {
            Field sourceField = reflectionProcessor.getAnyFieldFromObject(copySourceDstField.dataObject, copySourceDstField.sourceFieldName);
            if (sourceField != null) {
                copySourceDstField.isCallback = isFieldCustomCall(field);
            }
            return sourceField;
        }
        return null;
    }

    private CopySourceDstField getCopiableSrcDstField(CopySourceDstField copySourceDstField) {
        if (copySourceDstField.destination != null) {
            copySourceDstField.source = getField(copySourceDstField.destination, copySourceDstField);
        } else if (copySourceDstField.source != null) {
            copySourceDstField.destination = getField(copySourceDstField.source, copySourceDstField);
        }
        return copySourceDstField;
    }

    private List<CopySourceDstField> dstAnnotatedNotSrc(List<Field> dstFields, Object dataObject, String nestedKey) {
        List<CopySourceDstField> list = new ArrayList<>();
        CopySourceDstField copySourceDstField;
        for (Field field : dstFields) {
            copySourceDstField = new CopySourceDstField();
            copySourceDstField.setDataObject(dataObject);
            copySourceDstField.setDestination(field);
            copySourceDstField = getCopiableSrcDstField(copySourceDstField);
            if (isValidateTypeOrReport(copySourceDstField, nestedKey)) {
                list.add(copySourceDstField);
            }
        }
        return list;
    }

    private List<CopySourceDstField> srcAnnotatedNotDst(List<Field> srcFields, Object dataObject, String nestedKey) {
        List<CopySourceDstField> list = new ArrayList<>();
        CopySourceDstField copySourceDstField;
        for (Field field : srcFields) {
            copySourceDstField = new CopySourceDstField();
            copySourceDstField.setDataObject(dataObject);
            copySourceDstField.setSource(field);
            copySourceDstField = getCopiableSrcDstField(copySourceDstField);
            if (isValidateTypeOrReport(copySourceDstField, nestedKey)) {
                list.add(copySourceDstField);
            }
        }
        return list;
    }

    private List<CopySourceDstField> srcDstNotAnnotated(List<Field> fields, Object dataObject, String nestedKey) {
        return dstAnnotatedNotSrc(fields, dataObject, nestedKey);
    }

    private ObjectCopierInfoDetails processDetailsInfo(Object sourceObject, Object destinationObject, String nestedKey) {
        Class<?> sourceClass = sourceObject.getClass();
        Class<?> destinationClass = destinationObject.getClass();
        ObjectCopierInfoDetails objectCopierInfoDetails = (ObjectCopierInfoDetails) processInfo(destinationObject);
        objectCopierInfoDetails.amIDestination = true;

        List<Field> toKlassFields = reflectionProcessor.getAllField(destinationClass);
        if (isDataMapperAnnotationAvailable(toKlassFields)) {
            objectCopierInfoDetails.copySourceDstFields = dstAnnotatedNotSrc(toKlassFields, sourceObject, nestedKey);
            return objectCopierInfoDetails;
        }

        List<Field> fromObjectFields = reflectionProcessor.getAllField(sourceClass);
        if (isDataMapperAnnotationAvailable(fromObjectFields)) {
            objectCopierInfoDetails = (ObjectCopierInfoDetails) processInfo(sourceObject);
            objectCopierInfoDetails.amIDestination = false;
            objectCopierInfoDetails.copySourceDstFields = srcAnnotatedNotDst(fromObjectFields, sourceObject, nestedKey);
            return objectCopierInfoDetails;
        }

        if (!objectCopierInfoDetails.isStrictMapping) {
            objectCopierInfoDetails.copySourceDstFields = srcDstNotAnnotated(toKlassFields, sourceObject, nestedKey);
        }

        return objectCopierInfoDetails;
    }

    private Object processMap(Object sourceObject, Class<?> destinationProperty) throws IllegalAccessException, ObjectCopierException {
        if (sourceObject == null || destinationProperty == null) {
            return null;
        }
        Map<?, ?> map = (Map<?, ?>) sourceObject;
        Map response = reflectionProcessor.instanceOfMap(destinationProperty);
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            response.put(processAndGetValue(entry.getKey(), entry.getKey().getClass()), processAndGetValue(entry.getValue(), entry.getValue().getClass()));
        }
        if (response.size() == 0) {
            return null;
        }
        return response;
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

    private Object processSet(Object sourceObject, Class<?> destinationProperty) throws ObjectCopierException, IllegalAccessException {
        if (sourceObject == null || destinationProperty == null) {
            return null;
        }
        Set<?> list = (Set<?>) sourceObject;
        Set response = reflectionProcessor.instanceOfSet(destinationProperty);
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

    private Object processQueue(Object sourceObject, Class<?> destinationProperty) throws ObjectCopierException, IllegalAccessException {
        if (sourceObject == null || destinationProperty == null) {
            return null;
        }
        Queue<?> list = (Queue<?>) sourceObject;
        Queue response = reflectionProcessor.instanceOfQueue(destinationProperty);
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

    private Object processAndGetValue(Object object, Field field) throws IllegalAccessException, ObjectCopierException {
        Object fieldValue = field.get(object);
        Class<?> fieldClass = field.getClass();

        if (fieldValue == null) {
            return null;
        } else if (reflectionProcessor.isPrimitive(fieldClass)) {
            return fieldValue;
        } else if (reflectionProcessor.isMap(fieldClass)) {
            return processMap(fieldValue, fieldClass);
        } else if (reflectionProcessor.isList(fieldClass)) {
            return processList(fieldValue, fieldClass);
        } else if (reflectionProcessor.isSet(fieldClass)) {
            return processSet(fieldValue, fieldClass);
        } else if (reflectionProcessor.isQueue(fieldClass)) {
            return processQueue(fieldValue, fieldClass);
        }

        return null;
    }

    private <D> D processCopy(Object source, D destination, String nestedKey) throws ObjectCopierException {
        try{
            if (source == null || destination == null){
                return null;
            }
            ObjectCopierInfoDetails details = processDetailsInfo(source, destination, nestedKey);
            for (CopySourceDstField copySourceDstField : details.copySourceDstFields) {
                copySourceDstField.destination.set(destination, processAndGetValue(source, copySourceDstField.source));
            }
            return destination;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new ObjectCopierException(e.getMessage());
        }
    }

    private <D> D processCopy(Object source, Class<D> klass, String nestedKey) throws ObjectCopierException {
        D toInstance = reflectionProcessor.newInstance(klass);
        return processCopy(source, toInstance, nestedKey);
    }


    public <D> D copy(Object source, D destination) throws ObjectCopierException {
        return processCopy(source, destination, null);
    }

    public <D> D copy(Object source, Class<D> destination) throws ObjectCopierException {
        return processCopy(source, destination, null);
    }

}
