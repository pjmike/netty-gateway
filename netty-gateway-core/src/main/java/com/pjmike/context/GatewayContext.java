package com.pjmike.context;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/01/26
 */
@Slf4j
@Getter
public class GatewayContext {
    private static ThreadLocal<GatewayContext> CONTEXT_HOLDER;
    static {
        CONTEXT_HOLDER = ThreadLocal.withInitial(GatewayContext::new);
    }
    private HttpRequest request;
    private HttpResponse response;
    public  GatewayContext setRequest(HttpRequest request) {
        this.request = request;
        return this;
    }
    public GatewayContext setResponse(HttpResponse response) {
        this.response = response;
        return this;
    }
}
