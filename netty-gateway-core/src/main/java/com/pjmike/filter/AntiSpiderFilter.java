package com.pjmike.filter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.pjmike.annotation.Order;
import com.pjmike.attribute.Attributes;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;

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
@Order(-10)
public class AntiSpiderFilter implements GatewayFilter{
    private LoadingCache<String, RateLimiter> limiterCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            //每天清理缓存
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<String, RateLimiter>() {
                @Override
                public RateLimiter load(String key) throws Exception {
                    //每个IP每秒限流2个令牌
                    return RateLimiter.create(2);
                }
            });
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) throws Exception {
        FullHttpRequest request = channel.attr(Attributes.REQUEST).get();
        String clientIp = request.headers().get("X-Forwarded-For");
        if (clientIp == null) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
            clientIp = inetSocketAddress.getAddress().getHostAddress();
        }
        // limit clientIp
        RateLimiter rateLimiter = limiterCache.get(clientIp);
        if (!rateLimiter.tryAcquire()) {
            //TODO 自定义Exception抛出
            throw new RuntimeException("1s内IP请求次数不能超过2");
        }
        filterChain.filter(channel);
    }
}