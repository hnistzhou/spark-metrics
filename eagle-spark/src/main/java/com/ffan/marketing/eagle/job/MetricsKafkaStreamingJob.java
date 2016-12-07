package com.ffan.marketing.eagle.job;

import com.ffan.marketing.eagle.constant.PropertyConstant;
import com.ffan.marketing.eagle.utils.InfluxDBUtils;
import com.ffan.marketing.eagle.utils.PropertyUtils;
import com.ffan.marketing.eagle.wrapper.KafkaWrapper;
import com.ffan.marketing.eagle.wrapper.SparkComputeWrapper;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Created by bob on 2016/11/29.
 */
public class MetricsKafkaStreamingJob {

    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().setMaster("local[4]").setAppName("marketing-monitoring");
        Long duration = PropertyUtils.getLong(PropertyConstant.SPARK_BATCH_DURATION);
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(duration));

        JavaPairReceiverInputDStream<String, String> messages = KafkaWrapper.createStream(jssc);

        SparkComputeWrapper.compute(messages).foreachRDD(metricPointRDD -> {
            InfluxDBUtils.persist(metricPointRDD.collect());
        });
        jssc.start();
        jssc.awaitTermination();
    }
}
