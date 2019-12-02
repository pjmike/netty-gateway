package com.pjmike.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
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
    public static HttpResponse get(String url,Map<String,String> params) throws Exception {
        return get(url, null, params);
    }
    public static HttpResponse get(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            params.forEach(uriBuilder::setParameter);
        }
        //创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_TIMEOUT)
                .build();

        httpGet.setConfig(requestConfig);

        //设置请求头
        setHeader(headers, httpGet);
        //创建HttpClient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(httpclientPool)
                .build();

        //创建HttpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            return getHttpClientResult(httpResponse, httpClient, httpGet);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    public static HttpResponse post(String url) throws Exception{
        return null;
    }
    public static HttpResponse post(String url,Map<Object,Object> params) throws Exception {
        return null;
    }
    public static HttpResponse post(String url,Map<String,String> header,Map<Object,Object> params) throws Exception {
        return null;
    }

    private static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
        
    }

    /**
     * 获取响应结果
     *
     * @param httpResponse
     * @param httpClient
     * @param httpMethod
     * @return
     */
    private static HttpResponse getHttpClientResult(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws IOException {
        //执行请求
        httpResponse = httpClient.execute(httpMethod);
        return httpResponse;
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
}
