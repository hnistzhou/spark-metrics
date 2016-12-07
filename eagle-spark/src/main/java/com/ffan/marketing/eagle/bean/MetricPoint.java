package com.ffan.marketing.eagle.bean;

/**
 * Created by bob on 2016/12/1.
 */

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Data;
import org.apache.commons.lang.math.NumberUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Representation of a InfluxDB database MetricPoint.
 *
 * @author stefan.majer [at] gmail.com
 */
@Data
public class MetricPoint implements Serializable {
    private String measurement;
    private Map<String, String> tags;
    private Long time;
    private TimeUnit precision = TimeUnit.NANOSECONDS;
    private Map<String, Double> fields;

    /**
     * Create a new MetricPoint Build build to create a new MetricPoint in a fluent manner-
     *
     * @param measurement the name of the measurement.
     * @return the Builder to be able to add further Builder calls.
     */

    public static MetricPoint.Builder measurement(final String measurement) {
        return new MetricPoint.Builder(measurement);
    }


    /**
     * Builder for a new MetricPoint.
     *
     * @author stefan.majer [at] gmail.com
     */
    public static final class Builder {
        private final String measurement;
        private final Map<String, String> tags = new TreeMap<>();
        private final Map<String, Double> fields = new TreeMap<>();
        private Long time;
        private TimeUnit precision = TimeUnit.NANOSECONDS;

        /**
         * @param measurement
         */
        Builder(final String measurement) {
            this.measurement = measurement;
        }

        /**
         * Add a tag to this point.
         *
         * @param tagName the tag name
         * @param value   the tag value
         * @return the Builder instance.
         */
        public MetricPoint.Builder tag(final String tagName, final String value) {
            Preconditions.checkArgument(tagName != null);
            Preconditions.checkArgument(value != null);
            tags.put(tagName, value);
            return this;
        }

        /**
         * Add a Map of tags to add to this point.
         *
         * @param tagsToAdd the Map of tags to add
         * @return the Builder instance.
         */
        public MetricPoint.Builder tag(final Map<String, String> tagsToAdd) {
            for (Map.Entry<String, String> tag : tagsToAdd.entrySet()) {
                tag(tag.getKey(), tag.getValue());
            }
            return this;
        }

        /**
         * Add a field to this point.
         *
         * @param field the field name
         * @param value the value of this field
         * @return the Builder instance.
         */


        public MetricPoint.Builder addField(final String field, final long value) {

            fields.put(field, NumberUtils.toDouble(value + ""));
            return this;
        }

        public MetricPoint.Builder addField(final String field, final double value) {
            fields.put(field, value);
            return this;
        }

        public MetricPoint.Builder addField(String field, Double value) {
            fields.put(field, value);
            return this;
        }


        /**
         * Add a Map of fields to this point.
         *
         * @param fieldsToAdd the fields to add
         * @return the Builder instance.
         */
        public MetricPoint.Builder fields(final Map<String, Double> fieldsToAdd) {
            this.fields.putAll(fieldsToAdd);
            return this;
        }

        /**
         * Add a time to this point
         *
         * @param precisionToSet
         * @param timeToSet
         * @return the Builder instance.
         */
        public MetricPoint.Builder time(final long timeToSet, final TimeUnit precisionToSet) {
            Preconditions.checkNotNull(precisionToSet, "Precision must be not null!");
            this.time = timeToSet;
            this.precision = precisionToSet;
            return this;
        }

        /**
         * Create a new MetricPoint.
         *
         * @return the newly created MetricPoint.
         */
        public MetricPoint build() {
            Preconditions
                    .checkArgument(!Strings.isNullOrEmpty(this.measurement), "MetricPoint name must not be null or empty.");
            Preconditions.checkArgument(this.fields.size() > 0, "MetricPoint must have at least one field specified.");
            MetricPoint metricPoint = new MetricPoint();
            metricPoint.setFields(this.fields);
            metricPoint.setMeasurement(this.measurement);
            if (this.time != null) {
                metricPoint.setTime(this.time);
                metricPoint.setPrecision(this.precision);
            } else {
                metricPoint.setTime(System.currentTimeMillis());
                metricPoint.setPrecision(TimeUnit.MILLISECONDS);
            }
            metricPoint.setTags(this.tags);
            return metricPoint;
        }
    }

}

