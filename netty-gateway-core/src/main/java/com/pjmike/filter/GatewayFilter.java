package com.pjmike.filter;


import io.netty.channel.Channel;

import java.io.IOException;

/**
 * @author: pjmike
 * @create: 2019/11/28
 */
public interface GatewayFilter {
    /**
     * 过滤
     * @param channel
     * @param filterChain
     */
    void filter(Channel channel,GatewayFilterChain filterChain) throws Exception;
}