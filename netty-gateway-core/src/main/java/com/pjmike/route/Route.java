package com.pjmike.route;

import com.pjmike.filter.GatewayFilter;
import io.netty.channel.Channel;
import lombok.*;

import java.net.URI;
import java.util.List;
import java.util.function.Predicate;

/**
 * @description: 路由信息
 * @author: pjmike
 * @create: 2019/11/28
 */
@Builder
@Getter
@Setter
public class Route {
    /**
     * 路由id
     */
    private final String id;
    /**
     * 路由向的URI
     */
    private final URI uri;
    /**
     * 过滤器数组
     */
    private final List<GatewayFilter> gatewayFilters;
    /**
     * 断言
     */
    private final Predicate<Channel> predicate;

    public Route(String id, URI uri, List<GatewayFilter> gatewayFilters, Predicate<Channel> predicate) {
        this.id = id;
        this.uri = uri;
        this.gatewayFilters = gatewayFilters;
        this.predicate = predicate;
    }
}
