package com.hmtmcse.oc.test.data.entitydto;

import com.hmtmcse.oc.annotation.DataMappingInfo;

import javax.validation.constraints.*;

@DataMappingInfo(isStrict = false)
public class ValidationDto {


    @NotNull(message = "Name cannot be null")
    public String name;

    @AssertTrue(message = "Must need working status.")
    public boolean working;

    @Size(min = 10, max = 200, message = "About Me must be between 10 and 200 characters")
    public String aboutMe;

    @Min(value = 18, message = "Age should not be less than 18")
    @Max(value = 150, message = "Age should not be greater than 150")
    public int age;

    @Email(message = "Email should be valid")
    public String email;
}
