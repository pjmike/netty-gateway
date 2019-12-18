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
    private  String id;
    /**
     * 路由向的URI
     */
    private  URI uri;
    /**
     * 过滤器数组
     */
    private  List<GatewayFilter> gatewayFilters;
    /**
     * 断言
     */
    private  Predicate<Channel> predicate;

    public Route() {
    }

    public Route(String id, URI uri) {
        this(id, uri, null);
    }

    public Route(String id, URI uri, Predicate<Channel> predicate) {
        this(id, uri, null,predicate);
    }

    public Route(String id, URI uri, List<GatewayFilter> gatewayFilters, Predicate<Channel> predicate) {
        this.id = id;
        this.uri = uri;
        this.gatewayFilters = gatewayFilters;
        this.predicate = predicate;
    }

}
