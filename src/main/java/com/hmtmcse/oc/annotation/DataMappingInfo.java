package com.hmtmcse.oc.annotation;

import com.hmtmcse.oc.common.OCConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataMappingInfo {
    public boolean isStrict() default true;
    public String name() default "anonymous";
    public Class<?> customProcessor() default void.class;
}
