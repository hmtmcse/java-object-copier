package com.hmtmcse.oc.data;

import com.hmtmcse.oc.common.ProcessCustomCopy;


public class ObjectCopierInfo {
    public Boolean isStrictMapping = true;

    // According to presents of DataMapping Annotation
    public Boolean amIDestination = true;
    public ProcessCustomCopy<?, ?> processCustomCopy;
    public String mappingClassName;
}
