package com.pjmike.route.config;

import com.pjmike.annotation.Bean;
import com.pjmike.handler.predicate.PredicateUtils;
import com.pjmike.route.Route;

/**
 * @description: 用于自定义Router
 * @author: pjmike
 * @create: 2019/12/18
 */
public class RouteConfig {
    @Bean
    public Route buildRoute() {
        return Route.builder()
                .id("route_1")
                .uri("http://localhost:8089")
                .predicate(PredicateUtils.path("/"))
                .build();
    }

    @Bean
    public Route dubboRoute() {
        return Route.builder()
                .id("path_dubbo_route")
                .uri("dubbo://127.0.0.1:12345/com.pjmike.gateway.api.DemoService")
                .predicate(PredicateUtils.path("/dubbo"))
                .build();
    }

}
