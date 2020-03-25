package com.pjmike.filter.handle;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pjmike.common.context.ChannelContext;
import com.pjmike.exception.GatewayException;
import com.pjmike.filter.*;

import com.pjmike.http.NettyHttpResponseUtil;
import com.pjmike.route.Route;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/28
 */
@Slf4j
public class FilterWebHandler implements WebHandler {
    private static final String SENTINEL_GATEWAY_NETTY_ROUTE = "sentinel_gateway_netty_route";

    private static final FilterWebHandler INSTANCE = new FilterWebHandler();

    public static FilterWebHandler getInstance() {
        return INSTANCE;
    }

    private ExecutorService routePool = new ThreadPoolExecutor(5, 100, 30L, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(), new ThreadFactoryBuilder().setNameFormat("Route-%d-thread").build());

    private FilterProcessor filterProcessor = new FilterProcessor();


    @Override
    public void handle(Channel channel) throws Exception {
        Route route = ChannelContext.getRoute(channel);
        List<GatewayFilter> gatewayFilters = route.getGatewayFilters();
        this.filterProcessor.setFilterLoaderGatewayFilters(gatewayFilters);
        forwardAction(channel);
    }

    public void forwardAction(Channel channel) {
        try {
            preRoute(channel);
        } catch (GatewayException e) {
            error(e, channel);
        }
    }

    public void postAction(Channel channel) throws GatewayException {
        postRoute(channel);
    }

    public void routeAction(Channel channel) throws GatewayException {
        Entry entry = null;
        String requestPath = getRequestPath(channel);
        try {
            ContextUtil.enter(SENTINEL_GATEWAY_NETTY_ROUTE + requestPath);
            entry = SphU.entry(requestPath);
            route(channel);
        } catch (Exception e) {
            // 熔断降级
            log.info("开启降级模式");
            fallbackHandler(channel);
        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    /**
     * 熔断降级
     *
     * @param channel
     */
    private void fallbackHandler(Channel channel) throws GatewayException {
        ChannelContext.setResponse(channel, NettyHttpResponseUtil.buildFailResponse("service degrade"));
        postAction(channel);
    }

    private String getRequestPath(Channel channel) {
        FullHttpRequest httpRequest = ChannelContext.getRequest(channel);
        return URI.create(httpRequest.uri()).getPath();
    }

    public void errorAction(Channel channel, Throwable throwable) {
        error(throwable, channel);
    }

    private void route(Channel channel) throws GatewayException {
        this.filterProcessor.route(channel);
    }

    private void error(Throwable e, Channel channel) {
        ChannelContext.setException(channel, e);
        this.filterProcessor.error(channel);
    }

    private void postRoute(Channel channel) throws GatewayException {
        this.filterProcessor.postRoute(channel);
    }

    private void preRoute(Channel channel) throws GatewayException {
        this.filterProcessor.preRoute(channel);
    }
}
