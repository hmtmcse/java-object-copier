package com.hmtmcse.oc.common;

public interface ProcessCustomCopy<E, D> {

    void meAsSrc(D source, E destination);

    void meAsDst(E source, D destination);

    default void csvExport(E source, E destination) {
    }

    default void csvImport(D source, E destination) {
    }

    default void whyNotCalled(String message) {
    }
}
