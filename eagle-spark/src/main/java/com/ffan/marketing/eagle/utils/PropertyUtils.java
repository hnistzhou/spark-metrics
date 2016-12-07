package com.ffan.marketing.eagle.utils;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;

/**
 * Created by bob on 2016/11/30.
 */
public class PropertyUtils {
    public static String getProperty(String key) {

        String result = null;
        java.util.Properties props;
        try {
            props = PropertiesLoaderUtils.loadAllProperties("config.properties");
            result = props.getProperty(key);//根据name得到对应的value
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static Integer getInt(String propertyKey) {
        String property = getProperty(propertyKey);
        return NumberUtils.toInt(property);
    }

}
