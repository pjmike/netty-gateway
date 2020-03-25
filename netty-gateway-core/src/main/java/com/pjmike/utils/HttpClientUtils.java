package com.pjmike.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pjmike.common.constants.HttpClientConstants.*;
/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/02
 */
public class HttpClientUtils {


    private static PoolingHttpClientConnectionManager httpclientPool = new PoolingHttpClientConnectionManager();

    static {
        httpclientPool.setMaxTotal(DEFAULT_MAX_TOTAL);
        httpclientPool.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        httpclientPool.setValidateAfterInactivity(6000);
    }

    public static HttpResponse get(String url) throws Exception {
        return get(url, null, null);
    }

    public static HttpResponse get(String url, Map<String, List<String>> params) throws Exception {
        return get(url, null, params);
    }

    public static HttpResponse get(String url, Map<String, List<String>> headers, Map<String, List<String>> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            params.forEach((param,valueList)-> valueList.forEach(value-> uriBuilder.setParameter(param, value)));
        }
        //创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        httpGet.setConfig(getRequestConfig());

        //设置请求头
        setHeader(headers, httpGet);
        //创建HttpClient对象
        CloseableHttpClient httpClient = getHttpClient();
        return getHttpResponse(httpClient, httpGet);

    }

    public static HttpResponse post(String url) throws Exception {
        return post(url, null, null);
    }

    public static HttpResponse post(String url, Map<String, List<String>> params) throws Exception {
        return post(url, null, params);
    }

    public static HttpResponse post(String url, Map<String, List<String>> header, Map<String, List<String>> params) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(getRequestConfig());
        //设置请求头
        setHeader(header, httpPost);
        //设置请求参数
        setParams(params, httpPost);
        //获取响应结果
        return getHttpResponse(httpClient, httpPost);
    }

    private static void release(HttpResponse httpResponse, HttpClient httpClient) {
        //TODO
    }

    /**
     * 获取响应结果
     *
     * @param httpClient
     * @param httpMethod
     * @return
     */
    private static HttpResponse getHttpResponse(HttpClient httpClient, HttpRequestBase httpMethod) throws IOException {
        //创建HttpResponse对象
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpMethod);
            return httpResponse;
        } finally {
            release(httpResponse, httpClient);
        }
    }

    /**
     * 设置请求头
     *
     * @param headers
     * @param httpRequestBase
     */
    private static void setHeader(Map<String, List<String>> headers, HttpRequestBase httpRequestBase) {
        if (headers == null) {
            return;
        }
        headers.forEach((key,valueList)-> valueList.forEach(value -> httpRequestBase.setHeader(key, value)));
    }

    /**
     * 设置请求体
     *
     * @param params
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    private static void setParams(Map<String, List<String>> params, HttpEntityEnclosingRequestBase httpMethod) throws UnsupportedEncodingException {
        if (params == null) {
            return;
        }
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        params.forEach((key, valueList) -> valueList.forEach(value -> {
            nameValuePairs.add(new BasicNameValuePair(key, value));
        }));
        httpMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs, ENCODING));
    }

    /**
     * 创建HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        //创建HttpClient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(httpclientPool)
                .build();
        return httpClient;
    }

    /**
     * 配置请求的超时设置
     *
     * @return
     */
    private static RequestConfig getRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_TIMEOUT)
                .build();
        return requestConfig;
    }
}
