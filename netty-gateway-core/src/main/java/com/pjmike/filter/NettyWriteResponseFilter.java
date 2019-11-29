package com.pjmike.filter;

import io.netty.channel.Channel;

/**
 * @description: 接收内部服务的响应Filter
 * @author: pjmike
 * @create: 2019/11/29
 */
public class NettyWriteResponseFilter implements GatewayFilter{
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) {
        //TODO
    }
}
