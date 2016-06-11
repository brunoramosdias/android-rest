package com.devopcience.android_rest.client;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brunodias on 08/06/2016.
 */
public class APIClient {
    /*
     * Connection TimeOut Defaults 15 seconds
     * to change use seTimoutInMils() for Connection Time Out
     * or setReadTimeout
     */
    private int timeoutMillis = 15000;
    private int readTimeout = 15000;

    public enum RequestMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    public enum ContentType{
        PARAMS("text/plain"),
        JSON("application/json"),
        XML("application/xml");

        private final String value;

        ContentType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private String response = "";

    private int responseCode = 0;

    protected  void cleanParams(){
        response = "";
        responseCode = 0;
    }

    protected void execute(final RequestMethod method, final HashMap<String, String> params, ContentType contentType , String requestURL, Context context) throws Exception {
        HttpURLConnection connection;
        switch (method){
            case GET:
                connection = getHttpURLConnection(requestURL,method,context);
                getResponse(connection);
                connection.disconnect();
                break;
            case POST:
                connection = getHttpURLConnection(requestURL,method,context);
                if(params != null){
                    writeOutputStream(params,contentType, connection);
                }
                connection.connect();
                getResponse(connection);
                connection.disconnect();
                break;
            case PUT:
                connection = getHttpURLConnection(requestURL,method,context);
                if(params != null){
                    writeOutputStream(params, contentType, connection);
                }
                connection.connect();
                getResponse(connection);
                connection.disconnect();
                break;
            case DELETE:

                break;
        }
    }

    private void writeOutputStream(HashMap<String, String> params, ContentType contentType, HttpURLConnection connection) throws Exception {
        OutputStream os;
        BufferedWriter writer;
        os = connection.getOutputStream();
        writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        switch (contentType){

            case PARAMS:
                writer.write(getPostDataString(params));
                break;
            case JSON:
                writer.write(getJsonString(params));
                break;
            case XML:
                writer.write(getXMlString(params));
                break;
        }
        writer.flush();
        writer.close();
        os.close();
    }

    //TODO Implement JSON
    private String getXMlString(HashMap<String, String> stringStringHashMap) {
        return "";
    }

    //TODO evalueate  this over Jackson Parser
    private String getJsonString(HashMap<String, String> params) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            jsonObject.put(entry.getKey(),entry.getValue());
        }
        return jsonObject.toString();
    }

    protected void getResponse(HttpURLConnection connection) throws IOException {
        BufferedReader br;
        StringBuilder sb;
        String line;
        int responseCode = connection.getResponseCode();
        setResponseCode(responseCode);
        if(responseCode >= 200 && responseCode <= 300) {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        setResponse(sb.toString());
    }

    protected HttpURLConnection getHttpURLConnection(String requestURL, RequestMethod method, Context context) throws IOException {
        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.name());
        connection.setRequestProperty("Accept", "application/json");
        connection.setReadTimeout(readTimeout);
        connection.setConnectTimeout(timeoutMillis);
        return connection;
    }



    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else{
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }


    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
