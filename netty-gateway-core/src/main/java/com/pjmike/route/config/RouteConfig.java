package com.pjmike.route.config;

import com.pjmike.annotation.Bean;
import com.pjmike.handler.predicate.PredicateUtils;
import com.pjmike.route.Route;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @description: 用于自定义Router
 * @author: pjmike
 * @create: 2019/12/18
 */
public class RouteConfig {
    @Bean
    public Route buildRoute() throws URISyntaxException {
        return Route.builder()
                .id("route_1")
                .uri(new URI("http://localhost:8888/"))
                .predicate(PredicateUtils.path("/path/**"))
                .build();
    }
}
