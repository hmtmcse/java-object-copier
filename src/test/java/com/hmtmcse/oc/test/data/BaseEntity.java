package com.hmtmcse.oc.test.data;

import java.time.LocalDate;
import java.util.Date;

public class BaseEntity {

    public Long id;
    public Date created;
    public LocalDate lastUpdated;
    public String uuid;
    public boolean deleted;
    public User createdBy;
    public User updatedBy;

}
