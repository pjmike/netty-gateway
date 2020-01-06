package com.pjmike.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.pjmike.annotation.Order;
import com.pjmike.constants.CommonConstants;
import io.netty.channel.Channel;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/16
 */
@Order(1)
public class RateLimitFilter implements GatewayFilter{
    private RateLimiter rateLimiter = RateLimiter.create(CommonConstants.PERMIT_SPER_SECOND);
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) {
        if (rateLimiter.tryAcquire()) {
            filterChain.filter(channel);
        } else {
            //TODO
            throw new RuntimeException("access limit");
        }
    }
}
