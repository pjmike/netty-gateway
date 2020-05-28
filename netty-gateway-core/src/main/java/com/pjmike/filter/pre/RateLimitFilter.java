package com.pjmike.filter.pre;

import com.google.common.util.concurrent.RateLimiter;
import com.pjmike.commons.constants.CommonConstants;
import com.pjmike.filter.GlobalFilter;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/16
 */
@Slf4j
public class RateLimitFilter extends GlobalFilter {
    private RateLimiter rateLimiter = RateLimiter.create(CommonConstants.PERMIT_SPER_SECOND);
    private AtomicLong atomicLong = new AtomicLong();
    @Override
    public void filter(Channel channel) throws Exception {
        double acquire = rateLimiter.acquire();
        log.info("第 {} 个请求，获取令牌需要阻塞等待的时间：{}{}", atomicLong.getAndIncrement(),acquire,"s");
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
