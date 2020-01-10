package com.pjmike.filter;

import io.netty.channel.Channel;

import java.util.List;

/**
 * @description: 过滤器链
 * @author: pjmike
 * @create: 2019/11/28
 */
public class GatewayFilterChain {
    private int index = 0;
    private final List<GatewayFilter> filters;

    public GatewayFilterChain(List<GatewayFilter> filters) {
        this.filters = filters;
    }

    public void filter(Channel channel) throws Exception {
        //TODO
        if (this.index < filters.size()) {
            GatewayFilter filter = filters.get(index++);
            filter.filter(channel,this);
        }
    }
}
