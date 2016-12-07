package com.ffan.marketing.eagle.wrapper;

import com.ffan.marketing.eagle.constant.PropertyConstant;
import com.ffan.marketing.eagle.utils.PropertyUtils;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bob on 2016/11/30.
 */
public class KafkaWrapper {
    public static JavaPairReceiverInputDStream<String, String> createStream(JavaStreamingContext jssc) {
        String zookeeper = PropertyUtils.getProperty(PropertyConstant.ZOOKEEPER);
        String consumerGroup = PropertyUtils.getProperty(PropertyConstant.CONSUMER_GROUP);
        Map<String, Integer> topicMap = new HashMap();
        String topic = PropertyUtils.getProperty(PropertyConstant.TOPIC);
        Integer partition = PropertyUtils.getInt(PropertyConstant.PARTITION);
        topicMap.put(topic, partition);

        return KafkaUtils.createStream(jssc, zookeeper, consumerGroup, topicMap);
    }
}
