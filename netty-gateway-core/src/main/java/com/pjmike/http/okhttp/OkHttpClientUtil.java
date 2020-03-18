package com.pjmike.http.okhttp;


import com.pjmike.exception.GatewayException;
import io.netty.handler.codec.http.HttpResponseStatus;
import okhttp3.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: Okhttp
 * @author: pjmike
 * @create: 2020/03/18
 */
public class OkHttpClientUtil {
    private OkHttpClient okHttpClient;
    private static final MediaType JSON = MediaType.parse("application/json");
    private static final OkHttpClientUtil INSTANCE = new OkHttpClientUtil();

    public OkHttpClientUtil() {
        this.okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public OkHttpClientUtil getInstance() {
        return INSTANCE;
    }

    public String get(String url, Map<String, Object> headers) throws GatewayException {
        Request request = getRequest(url, headers);
        try (Response response = this.okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new GatewayException("okhttp response failed");
            }
            String responseBody = response.body().string();
            return responseBody;
        } catch (IOException e) {
            // ... handle IO exception
            return null;
        }
    }

    private Request getRequest(String url, Map<String, Object> headers) {
        Request request = new Request.Builder().url(url).build();
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach((k, v) -> request.newBuilder().addHeader(k, String.valueOf(v)));
        }
        return request;
    }

    private Request getRequestWithBody(String url, Map<String, Object> headers, String content) {
        RequestBody body = RequestBody.create(JSON, content);
        Request request = getRequest(url, headers);
        return request.newBuilder().post(body).build();
    }

    public String post(String url, Map<String, Object> headers, String content) throws GatewayException {
        Request request = getRequestWithBody(url, headers, content);
        try (Response response = this.okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new GatewayException("okhttp response failed");
            }
            return response.body().string();
        } catch (IOException e) {
            throw new GatewayException(e, HttpResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
