package com.ffan.marketing.eagle.job;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by bob on 2016/11/29.
 */
public class MetricsKafkaStreammingJob {
    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) throws Exception {
        /*if (args.length < 4) {
            System.err.println("Usage: JavaKafkaWordCount <zkQuorum> <group> <topics> <numThreads>");
            System.exit(1);
        }*/

        SparkConf sparkConf = new SparkConf().setMaster("local[4]").setAppName("JavaKafkaWordCount");
        // Create the context with 2 seconds batch size
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(10000));

        //int numThreads = Integer.parseInt(args[3]);
        Map<String, Integer> topicMap = new HashMap<>();
//        String[] topics = args[2].split(",");
//        for (String topic : topics) {
//            topicMap.put(topic, numThreads);
//        }
        topicMap.put("test", 1);

        JavaPairReceiverInputDStream<String, String> messages =
                KafkaUtils.createStream(jssc, "localhost:2181", "test-group", topicMap);
        JavaDStream<String> lines = messages.map(tuple2 -> tuple2._2());
        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(SPACE.split(x)));

        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1))
                .reduceByKey((i1, i2) -> i1 + i2);

        wordCounts.print();
        jssc.start();
        jssc.awaitTermination();
    }
}
