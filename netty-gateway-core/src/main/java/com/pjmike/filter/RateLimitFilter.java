package com.pjmike.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.pjmike.annotation.Order;
import com.pjmike.constants.CommonConstants;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/16
 */
@Order(1)
@Slf4j
public class RateLimitFilter implements GatewayFilter{
    private RateLimiter rateLimiter = RateLimiter.create(CommonConstants.PERMIT_SPER_SECOND);
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) throws Exception {
        double acquire = rateLimiter.acquire();
        log.info("time spent sleeping to enforce rate : {}", acquire);
        filterChain.filter(channel);
    }
}
