package com.pjmike.filter.handle;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pjmike.context.ChannelContextUtil;
import com.pjmike.exception.GatewayException;
import com.pjmike.filter.*;

import com.pjmike.route.Route;
import io.netty.channel.Channel;

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
public class FilterWebHandler implements WebHandler {
    private static final FilterWebHandler INSTANCE = new FilterWebHandler();

    public static FilterWebHandler getInstance() {
        return INSTANCE;
    }

    private FilterProcessor filterProcessor = new FilterProcessor();

    private ExecutorService routePool = new ThreadPoolExecutor(5, 100, 30L, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(), new ThreadFactoryBuilder().setNameFormat("Route-%d-thread").build());

    @Override
    public void handle(Channel channel) throws Exception {
        Route route = ChannelContextUtil.getRoute(channel);
        List<GatewayFilter> gatewayFilters = route.getGatewayFilters();
        this.filterProcessor.setFilterLoaderGatewayFilters(gatewayFilters);
        forwardAction(channel);
    }

    public void forwardAction(Channel channel) {
        try {
            preRoute(channel);
        } catch (GatewayException e) {
            error(e,channel);
        }
        routePool.execute(()-> {
            try {
                route(channel);
            } catch (GatewayException e) {
                e.printStackTrace();
            }
        });
    }

    public void postAction(Channel channel) throws GatewayException {
        postRoute(channel);
    }

    private void route(Channel channel) throws GatewayException{
        this.filterProcessor.route(channel);
    }

    private void error(Throwable e,Channel channel) {
        ChannelContextUtil.setException(channel,e);
        this.filterProcessor.error(channel);
    }
    private void postRoute(Channel channel) throws GatewayException{
        this.filterProcessor.postRoute(channel);
    }

    private void preRoute(Channel channel) throws GatewayException{
        this.filterProcessor.preRoute(channel);
    }
}
