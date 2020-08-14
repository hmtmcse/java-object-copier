package com.hmtmcse.oc.copier;

import com.hmtmcse.oc.common.ProcessCustomCopy;
import com.hmtmcse.oc.data.CopySourceDstField;

import java.util.List;

public class ObjectCopierInfo {
    public Boolean isStrictMapping = true;
    public Boolean amIDestination = true;
    public List<CopySourceDstField> fieldPairs;
    public ProcessCustomCopy<?, ?> processCustomCopy;
}
