package com.pjmike.route;

import com.pjmike.handler.predicate.PredicateUtils;
import com.pjmike.route.discovery.DiscoverClient;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/05
 */
public class DiscoveryClientRouteLocator extends AbstractRouteLocator{
    private final DiscoverClient discoverClient;

    public DiscoveryClientRouteLocator(DiscoverClient discoverClient) {
        this.discoverClient = discoverClient;
    }

    @Override
    public List<Route> getRoutes() {
        List<Route> routes = discoverClient.getServices()
                .stream()
                .map(serviceId -> {
                    Route route = Route.builder()
                            .id(discoverClient.getClass().getSimpleName() + "_" + serviceId)
                            .uri("lb://" + serviceId)
                            .predicate(PredicateUtils.path("/" + serviceId + "/**"))
                            .build();
                    return route;
                }).collect(Collectors.toList());
        return routes;
    }
}
