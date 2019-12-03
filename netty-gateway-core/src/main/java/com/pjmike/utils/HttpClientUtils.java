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

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/02
 */
public class HttpClientUtils {
    private static final String ENCODING = "UTF-8";
    /**
     * 建立连接时间
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 6000;
    /**
     * 等待数据的超时时间
     */
    private static final int DEFAULT_SOCKET_TIMEOUT = 6000;
    /**
     * 从连接池中获取连接的超时时间
     */
    private static final int DEFAULT_TIMEOUT = 6000;

    /**
     * 最大支持的连接数
     */
    private static final int DEFAULT_MAX_TOTAL = 512;
    /**
     * 针对某个域名的最大连接数
     */
    private static final int DEFAULT_MAX_PER_ROUTE = 64;

    private static PoolingHttpClientConnectionManager httpclientPool = new PoolingHttpClientConnectionManager();

    static {
        httpclientPool.setMaxTotal(DEFAULT_MAX_TOTAL);
        httpclientPool.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        httpclientPool.setValidateAfterInactivity(6000);
    }

    public static HttpResponse get(String url) throws Exception {
        return get(url, null, null);
    }

    public static HttpResponse get(String url, Map<String, String> params) throws Exception {
        return get(url, null, params);
    }

    public static HttpResponse get(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            params.forEach(uriBuilder::setParameter);
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

    public static HttpResponse post(String url, Map<Object, Object> params) throws Exception {
        return post(url, null, params);
    }

    public static HttpResponse post(String url, Map<String, String> header, Map<Object, Object> params) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(getRequestConfig());
        //设置请求头
        setHeader(header, httpPost);
        //设置请求体
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
    private static void setHeader(Map<String, String> headers, HttpRequestBase httpRequestBase) {
        if (headers == null) {
            return;
        }
        headers.forEach(httpRequestBase::setHeader);
    }

    /**
     * 设置请求体
     *
     * @param params
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    private static void setParams(Map<Object, Object> params, HttpEntityEnclosingRequestBase httpMethod) throws UnsupportedEncodingException {
        if (params == null) {
            return;
        }
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        params.forEach((key, value) -> nameValuePairs.add(new BasicNameValuePair(String.valueOf(key), String.valueOf(value))));
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
