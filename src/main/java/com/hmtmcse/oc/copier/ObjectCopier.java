package com.hmtmcse.oc.copier;

import com.hmtmcse.oc.annotation.DataMapping;
import com.hmtmcse.oc.annotation.DataMappingInfo;
import com.hmtmcse.oc.common.OCConstant;
import com.hmtmcse.oc.common.ObjectCopierException;
import com.hmtmcse.oc.common.ProcessCustomCopy;
import com.hmtmcse.oc.data.*;
import com.hmtmcse.oc.reflection.ReflectionProcessor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ObjectCopier {

    private ReflectionProcessor reflectionProcessor;
    private LinkedHashMap<String, CopyReport> errorReports = new LinkedHashMap<>();

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
        }if (copySourceDstField.destination == null) {
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

    private Boolean isObjectHasCustomProcessor(Object object) {
        return object instanceof ProcessCustomCopy;
    }

    private ObjectCopierInfo processInfo(Object object) {
        ObjectCopierInfo objectCopierInfo = new ObjectCopierInfo();
        objectCopierInfo.isStrictMapping = isStrictMapping(object.getClass());
        objectCopierInfo.mappingClassName = copierDefaultName(object.getClass());
        if (isObjectHasCustomProcessor(object)) {
            objectCopierInfo.processCustomCopy = (ProcessCustomCopy<?, ?>) object;
        }
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
            objectCopierInfoDetails.copySourceDstFields = srcAnnotatedNotDst(fromObjectFields, toKlass, nestedKey);
            return objectCopierInfoDetails;
        }

        if (!objectCopierInfoDetails.isStrictMapping) {
            objectCopierInfoDetails.copySourceDstFields = srcDstNotAnnotated(toKlassFields, fromObject, nestedKey);
        }

        return objectCopierInfoDetails;
    }

    private <D> D processCopy(Object source, D destination, String nestedKey) throws ObjectCopierException {

    }


    public <D> D copy(Object source, D destination) throws ObjectCopierException {

    }

    public <D> D copy(Object source, Class<D> destination) throws ObjectCopierException {

    }

}
