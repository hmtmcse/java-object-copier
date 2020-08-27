package com.hmtmcse.oc.test.data.custom;

import com.hmtmcse.oc.common.ProcessCustomCopy;
import com.hmtmcse.oc.test.data.entitydto.DtoBasic;
import com.hmtmcse.oc.test.data.entitydto.EntityBasic;

public class ProcessCustomCopyImpl implements ProcessCustomCopy<EntityBasic, DtoBasic> {


    @Override
    public void meAsSrc(DtoBasic source, EntityBasic destination) {

    }

    @Override
    public void meAsDst(EntityBasic source, DtoBasic destination) {

    }
}
