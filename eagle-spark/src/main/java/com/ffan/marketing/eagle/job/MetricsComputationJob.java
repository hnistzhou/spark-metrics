package com.ffan.marketing.eagle.job;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by bob on 2016/11/29.
 */
public class MetricsComputationJob {
    public static void main(String[] args) {
        compute();
    }

    public static void compute() {
        String logFile = "/Users/bob/Devtools/sparkdata/simple.txt"; // Should be some file on your system
        SparkConf conf = new SparkConf().setAppName("Marketing Metrics");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> logData = sc.textFile(logFile).cache();

        long numAs = logData.filter(e -> e.contains("a")).count();

        long numBs = logData.filter(s -> s.contains("b")).count();

        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
        sc.stop();

    }
}
