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
                .uri(new URI("http://localhost:8089"))
                .predicate(PredicateUtils.path("/"))
                .build();
    }

    @Bean
    public Route dubboRoute() throws URISyntaxException {
        return Route.builder()
                .id("path_dubbo_route")
                .uri(new URI("dubbo://127.0.0.1:20880/org.apache.dubbo.demo.DemoService"))
                .build();
    }
}
