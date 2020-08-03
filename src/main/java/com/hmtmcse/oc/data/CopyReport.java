package com.hmtmcse.oc.data;

import java.util.LinkedHashMap;

public class CopyReport {
    public String name;
    public String errorType;
    public String reason;
    public LinkedHashMap<String, CopyReport> nested;
}
