package com.pjmike.filter.pre;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.pjmike.commons.context.ChannelContext;
import com.pjmike.exception.GatewayException;
import com.pjmike.filter.GlobalFilter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @description: 反爬虫：在某段时间内限制IP的请求次数
 * <p>
 *  暂时使用Guava 的 {@link RateLimiter}
 * </p>
 * <p>
 *  RateLimiter目前有2种实现：SmoothBursty和SmoothWarmingUp。
 *     - SmoothBursty是一种令牌桶的设计，桶的容量为一秒的QPS；
 *     - SmoothWarmingUp则可看做是一种漏桶设计
 * </p>
 * @author: pjmike
 * @create: 2020/01/16
 */
@Slf4j
public class AntiSpiderFilter extends GlobalFilter {
    private LoadingCache<String, RateLimiter> limiterCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            //每天清理缓存
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<String, RateLimiter>() {
                @Override
                public RateLimiter load(String key) throws Exception {
                    //每个IP每秒限流2个令牌
                    return RateLimiter.create(10);
                }
            });


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return -10;
    }
    @Override
    public void filter(Channel channel) throws Exception {
        FullHttpRequest request = ChannelContext.getRequest(channel);
        String clientIp = request.headers().get("X-Forwarded-For");
        if (clientIp == null) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
            clientIp = inetSocketAddress.getAddress().getHostAddress();
        }
        // limit clientIp
        RateLimiter rateLimiter = limiterCache.get(clientIp);
        if (!rateLimiter.tryAcquire()) {
            log.warn("同一个IP在1s内访问次数不能超过10次");
            throw new GatewayException(HttpResponseStatus.INTERNAL_SERVER_ERROR,"同一个IP在1s内访问次数不能超过10次");
        }
    }
}
