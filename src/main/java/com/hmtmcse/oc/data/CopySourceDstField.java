package com.hmtmcse.oc.data;

import java.lang.reflect.Field;

public class CopySourceDstField {
    public Field source;
    public Field destination;
    public String sourceFieldName;

    public CopySourceDstField() {}

    public CopySourceDstField(Field destination, Field source, String sourceFieldName) {
        this.source = source;
        this.destination = destination;
        this.sourceFieldName = sourceFieldName;
    }
}
