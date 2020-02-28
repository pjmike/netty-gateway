package com.pjmike.filter.pre;

import com.google.common.util.concurrent.RateLimiter;
import com.pjmike.constants.CommonConstants;
import com.pjmike.filter.GlobalFilter;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/16
 */
@Slf4j
public class RateLimitFilter extends GlobalFilter {
    private RateLimiter rateLimiter = RateLimiter.create(CommonConstants.PERMIT_SPER_SECOND);
    @Override
    public void filter(Channel channel) throws Exception {
        double acquire = rateLimiter.acquire();
        log.info("time spent sleeping to enforce rate : {}", acquire);
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return -5;
    }
}
