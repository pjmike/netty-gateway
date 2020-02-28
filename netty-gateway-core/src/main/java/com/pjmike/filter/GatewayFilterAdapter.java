package com.pjmike.filter;

import io.netty.channel.Channel;

/**
 * @description: 适配器
 * @author: pjmike
 * @create: 2020/02/25
 */
public class GatewayFilterAdapter extends GatewayFilter{
    private final GlobalFilter delegate;

    public GatewayFilterAdapter(GlobalFilter delegate) {
        this.delegate = delegate;
    }

    @Override
    public String filterType() {
        return this.delegate.filterType();
    }

    @Override
    public int filterOrder() {
        return this.delegate.filterOrder();
    }

    @Override
    public void filter(Channel channel) throws Exception {
        this.delegate.filter(channel);
    }
}
