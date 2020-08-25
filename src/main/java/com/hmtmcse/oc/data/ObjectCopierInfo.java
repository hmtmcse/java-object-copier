package com.hmtmcse.oc.data;

import com.hmtmcse.oc.common.ProcessCustomCopy;


public class ObjectCopierInfo<S, D> {
    public Boolean isStrictMapping = true;

    // According to presents of DataMapping Annotation
    public Boolean amIDestination = true;
    public ProcessCustomCopy<S, D> processCustomCopy;
    public String mappingClassName;

    public void callGlobalCallBack(S source, D destination) {
        if (processCustomCopy != null) {
            if (amIDestination) {
                processCustomCopy.meAsDst(destination, source);
            } else {
                processCustomCopy.meAsSrc(source, destination);
            }
        }
    }
}
