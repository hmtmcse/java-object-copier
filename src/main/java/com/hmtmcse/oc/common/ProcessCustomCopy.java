package com.hmtmcse.oc.common;

public interface ProcessCustomCopy<S, D> {

    default void meAsSrc(S source, D destination, String fieldName) {
    }

    default void meAsDst(D source, S destination, String fieldName) {
    }

    default void meAsSrc(S source, D destination) {
    }

    default void meAsDst(D source, S destination) {
    }

    default void csvExport(S source, S destination) {
    }

    default void csvImport(D source, S destination) {
    }
}
