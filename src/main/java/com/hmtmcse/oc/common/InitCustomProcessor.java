package com.hmtmcse.oc.common;

public interface InitCustomProcessor {
    public ProcessCustomCopy<?,?> init(Class<?> klass);
}
