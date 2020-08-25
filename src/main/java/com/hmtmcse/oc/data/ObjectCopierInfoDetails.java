package com.hmtmcse.oc.data;

import java.util.ArrayList;
import java.util.List;

public class ObjectCopierInfoDetails<S, D> extends ObjectCopierInfo<S, D> {
 public List<CopySourceDstField> copySourceDstFields = new ArrayList<>();
}
