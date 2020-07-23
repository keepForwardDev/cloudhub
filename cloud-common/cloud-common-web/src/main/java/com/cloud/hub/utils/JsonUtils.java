package com.cloud.hub.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JsonUtils {

    public static ObjectMapper objectMapper;

    static {
        objectMapper = SpringUtil.getBean(ObjectMapper.class);
    }


    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readObject(String value, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            return objectMapper.readValue(value, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
