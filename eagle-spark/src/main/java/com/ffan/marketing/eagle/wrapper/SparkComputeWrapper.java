package com.ffan.marketing.eagle.wrapper;

import com.ffan.marketing.eagle.bean.Line;
import com.ffan.marketing.eagle.bean.MetricPoint;
import com.ffan.marketing.eagle.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by bob on 2016/12/5.
 */
public class SparkComputeWrapper {
    public final static String MAGIC = "abdae13d";

    public static JavaDStream<MetricPoint> compute(JavaPairReceiverInputDStream<String, String> messages) {
        return messages.filter(tuple2 -> {
            String line = tuple2._2();
            if (StringUtils.isEmpty(line)) {
                return false;
            }
            return line.startsWith(MAGIC);
        }).flatMap(tuple2 -> {
            Line line = BeanUtils.toLine(tuple2._2);
            if (line != null) {
                return BeanUtils.toPoints(line);
            }
            return new ArrayList<>();
        }).mapToPair(metricPoint -> {
            Map<String, String> tagMap = metricPoint.getTags();
            Map.Entry<String, String> tag = tagMap.entrySet().iterator().next();
            String t1 = metricPoint.getMeasurement() + "-" + tag.getKey() + "-" + tag.getValue();
            return new Tuple2<>(t1, metricPoint);
        }).reduceByKey((metricPoint, metricPoint2) -> {
            metricPoint.getFields().entrySet().forEach(e -> {
                String key = e.getKey();
                Double value = e.getValue();
                if (metricPoint2.getFields().containsKey(key)) {
                    Double exist = metricPoint2.getFields().get(key);
                    metricPoint2.getFields().put(key, exist + value);
                }
            });
            return metricPoint2;
        }).map(Tuple2::_2);
    }
}
