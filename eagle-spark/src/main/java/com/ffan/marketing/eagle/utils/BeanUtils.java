package com.ffan.marketing.eagle.utils;

import com.ffan.marketing.eagle.bean.Line;
import com.ffan.marketing.eagle.bean.MetricPoint;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bob on 2016/11/30.
 */
public class BeanUtils {
    public static Line toLine(String lineStr) {
        String[] lineArr = lineStr.split("\\|");
        if (lineArr.length != 11) {
            return null;
        }
        String magic = lineArr[0];
        String timestamp = lineArr[4];
        String traceID = lineArr[5];
        String appName = lineArr[6];
        String ip = lineArr[7];
        String metric = lineArr[8];
        String fieldStr = lineArr[9];
        String tagStr = lineArr[10];
        Line line = new Line();
        line.setMagic(magic);
        line.setTimestamp(NumberUtils.toLong(timestamp));
        line.setTraceID(traceID);
        line.setAppName(appName);
        line.setIp(ip);
        line.setMetric(metric);
        line.setFields(toMap(fieldStr));
        line.setTags(toTagMap(tagStr));
        return line;
    }


    private static Map<String, String> toTagMap(String str) {
        String[] fields = str.split(",");
        Map<String, String> map = new HashMap<>();
        for (String field : fields) {
            String[] kv = field.split("=");
            String v = kv[1];
            map.put(kv[0], kv[1]);
        }
        return map;
    }

    private static Map<String, Double> toMap(String str) {
        String[] fields = str.split(",");
        Map<String, Double> map = new HashMap<>();
        for (String field : fields) {
            String[] kv = field.split("=");
            map.put(kv[0], NumberUtils.toDouble(kv[1]));
        }
        return map;
    }

    public static MetricPoint toPoint(Line line) {
        MetricPoint metricPoint = MetricPoint.measurement(line.getMetric())
                .tag("appName", line.getAppName())
                .tag("ip", line.getIp())
                .tag(line.getTags())
                .fields(line.getFields()).build();
        return metricPoint;
    }

    public static Iterable<MetricPoint> toPoints(Line line) {
        List<MetricPoint> result = new ArrayList<>();
        Map<String, String> tags = line.getTags();
        tags.entrySet().forEach(e -> {
            String key = e.getKey();
            String value = e.getValue();
            MetricPoint metricPoint = MetricPoint.measurement(line.getMetric())
                    .fields(line.getFields())
                    .tag(key, value).build();
            result.add(metricPoint);
        });
        MetricPoint appNameMetricPoint = MetricPoint.measurement(line.getMetric())
                .fields(line.getFields())
                .tag("appName", line.getAppName()).build();
        result.add(appNameMetricPoint);
        MetricPoint ipMetricPoint = MetricPoint.measurement(line.getMetric())
                .fields(line.getFields())
                .tag("ip", line.getIp()).build();
        result.add(ipMetricPoint);
        return result;
    }

}