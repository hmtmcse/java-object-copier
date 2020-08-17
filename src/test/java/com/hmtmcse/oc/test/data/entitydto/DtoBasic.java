package com.hmtmcse.oc.test.data.entitydto;

import com.hmtmcse.oc.annotation.DataMapping;
import com.hmtmcse.oc.annotation.DataMappingInfo;

@DataMappingInfo(isStrict = false)
public class DtoBasic {

    public Long id;

    @DataMapping(source = "name")
    public String other;

    public String email;

    public ValidationDto validationDto;
}
