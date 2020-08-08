package com.hmtmcse.oc.test.data.entitydto;

import com.hmtmcse.oc.annotation.DataMapping;

public class DtoBasic {

    public Long id;

    @DataMapping(source = "name")
    public String other;
}
