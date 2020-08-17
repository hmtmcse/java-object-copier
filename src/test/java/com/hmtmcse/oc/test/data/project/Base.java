package com.hmtmcse.oc.test.data.project;

import java.util.Date;

public class Base {
    public Long id;
    private Date created;
    private Date lastUpdated;
    public String uuid;
    private Boolean isDeleted = false;
    public Identity createdBy;
    public Identity updatedBy;
}
