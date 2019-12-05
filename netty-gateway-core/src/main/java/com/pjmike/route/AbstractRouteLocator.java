package com.pjmike.route;

import io.netty.channel.Channel;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/05
 */
public abstract class AbstractRouteLocator implements RouteLocator{

    @Override
    public Route lookupRoute(Channel channel) {
        return getRoutes()
                .stream()
                .filter(route -> route.getPredicate().test(channel))
                .findFirst()
                .orElse(null);
    }
}
