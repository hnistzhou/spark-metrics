package com.ffan.marketing.eagle.bean;

import lombok.Data;
import org.apache.commons.collections.map.HashedMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bob on 2016/11/30.
 */
@Data
public class Line implements Serializable {
    private String magic;
    private long timestamp;
    private String traceID;
    private String appName;
    private String ip;
    private String metric;
    private Map<String, Double> fields = new HashedMap();
    private Map<String, String> tags = new HashMap<>();
}