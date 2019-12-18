package com.pjmike.filter;

import com.pjmike.annotation.Order;
import io.netty.channel.Channel;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/16
 */
@Order(1)
public class LimitFilter implements GatewayFilter{
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) {
        //限流Filter TODO
    }
}
