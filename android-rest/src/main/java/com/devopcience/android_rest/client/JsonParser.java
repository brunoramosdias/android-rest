package com.devopcience.android_rest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by brunodias on 09/06/2016
 */
public class JsonParser {

    public static <T> T parseObject(Class<T> tClass, String json, String rootObject,
                                    boolean failUnkonownProperties,
                                    boolean failMissingCreatorProperties) throws IOException,
            JSONException {
        String simpleJson = new JSONObject(json).getString(rootObject);
        ObjectMapper objectMapper = getObjectMapper(failUnkonownProperties,
                failMissingCreatorProperties);
       return objectMapper.readValue(simpleJson, tClass);
    }

    public static <T> T parseObject(Class<T> tClass, String json,
                                    boolean failUnkonownProperties,
                                    boolean failMissingCreatorProperties) throws IOException {
        ObjectMapper objectMapper = getObjectMapper(failUnkonownProperties,
                failMissingCreatorProperties);
       return objectMapper.readValue(json, tClass);
    }

    public static <T> T parseObject(Class<T> tClass, InputStream inputStream,
                                    boolean failUnkonownProperties,
                                    boolean failMissingCreatorProperties) throws IOException {
        ObjectMapper objectMapper = getObjectMapper(failUnkonownProperties,
                failMissingCreatorProperties);
        return objectMapper.readValue(inputStream, tClass);
    }


    private static ObjectMapper getObjectMapper(boolean failUnkonownProperties,
                                                boolean failMissingCreatorProperties) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                failUnkonownProperties);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES,
                failMissingCreatorProperties);
        return objectMapper;
    }

    public static String convertObject(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}
