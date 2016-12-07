package com.ffan.marketing.eagle.utils;

import com.ffan.marketing.eagle.bean.MetricPoint;
import com.ffan.marketing.eagle.constant.PropertyConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.List;
import java.util.Map;

/**
 * Created by bob on 2016/12/2.
 */
public class InfluxDBUtils {
    public static void persist(List<MetricPoint> metricPoints) {
        if (CollectionUtils.isEmpty(metricPoints)) {
            return;
        }
        String database = PropertyUtils.getProperty(PropertyConstant.INFLUXDB_DATABASE);
        String url = PropertyUtils.getProperty(PropertyConstant.INFLUXDB_URL);
        String user = PropertyUtils.getProperty(PropertyConstant.INFLUXDB_USER);
        String password = PropertyUtils.getProperty(PropertyConstant.INFLUXDB_PASSWORD);
        InfluxDB influxDB = InfluxDBFactory.connect(url, user, password);

        BatchPoints batchPoints = BatchPoints
                .database(database)
                .retentionPolicy("")
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();
        metricPoints.forEach(e -> {
            Map<String, Object> fields = transform(e.getFields());
            Point point = Point.measurement(e.getMeasurement())
                    .tag(e.getTags()).fields(fields).build();
            batchPoints.point(point);
        });
        influxDB.write(batchPoints);
    }

    private static Map<String, Object> transform(Map<String, Double> source) {
        if (MapUtils.isEmpty(source)) {
            return new HashedMap();
        }
        Map<String, Object> target = new HashedMap();
        source.entrySet().forEach(e -> {
            target.put(e.getKey(), e.getValue());
        });
        return target;
    }
}
