package com.hmtmcse.oc.copier;

import com.hmtmcse.oc.annotation.DataMapping;
import com.hmtmcse.oc.annotation.DataMappingInfo;
import com.hmtmcse.oc.common.OCConstant;
import com.hmtmcse.oc.common.ObjectCopierException;
import com.hmtmcse.oc.reflection.ReflectionProcessor;

import java.lang.reflect.Field;
import java.util.List;

public class ObjectCopier {

    private ReflectionProcessor reflectionProcessor;

    public ObjectCopier() {
        reflectionProcessor = new ReflectionProcessor();
    }

    private Boolean isMapperAvailable(List<Field> fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(DataMapping.class)) {
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

    private ObjectCopierInfo processInfo() {
        ObjectCopierInfo objectCopierInfo = new ObjectCopierInfo();

        return objectCopierInfo;
    }

    private <D> D copy(Object source, D destination, String nestedKey) throws ObjectCopierException {

    }


    public <D> D copy(Object source, D destination) throws ObjectCopierException {

    }

    public <D> D copy(Object source, Class<D> destination) throws ObjectCopierException {

    }

}
