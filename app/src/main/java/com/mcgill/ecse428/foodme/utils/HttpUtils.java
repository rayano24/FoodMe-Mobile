package com.mcgill.ecse428.foodme.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtils {
    public static final String DEFAULT_BASE_URL = "/https://foodme-428.herokuapp.com";

    private static String baseUrl;
    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        baseUrl = DEFAULT_BASE_URL;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        HttpUtils.baseUrl = baseUrl;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl;
    }

    public static String convertToMeters(String radius){
        // Hard coding this for now...
        switch(radius) {
            case "100m":
                return "100";
            case "500m":
                return "500";
            case "1km":
                return "1000";
            case "5km":
                return "5000";
            case "15km":
                return "15000";
            case "40km":
                return "40000";
            default:
                return "1000";
        }
    }

    public static String convertToPrice(String price){
        return String.valueOf(price.length());
    }
}