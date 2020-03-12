package com.pjmike.context;


import com.alibaba.ttl.TransmittableThreadLocal;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/12
 */
public class RequestContext {

    private final Map<String, Object> requestMap = new ConcurrentHashMap<>();
    private static TransmittableThreadLocal<RequestContext> threadLocal = new TransmittableThreadLocal<>();
    static {
        threadLocal.set(new RequestContext());
    }
    public static RequestContext getCurrentContext() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

    public void setRequest(HttpRequest httpRequest) {
        put("request", httpRequest);
    }

    public HttpRequest getRequest() {
        return (HttpRequest) this.requestMap.get("request");
    }

    public void setResponse(HttpResponse response) {
        put("response", response);
    }

    public HttpResponse getResponse() {
        return (HttpResponse) this.requestMap.get("response");
    }

    private void put(String key, Object object) {
        this.requestMap.put(key, object);
    }
}
