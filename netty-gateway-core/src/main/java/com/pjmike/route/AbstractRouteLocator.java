package com.pjmike.route;

import com.pjmike.exception.GatewayException;
import io.netty.channel.Channel;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/05
 */
public abstract class AbstractRouteLocator implements RouteLocator{

    @Override
    public Route lookupRoute(Channel channel) throws Exception{
        return getRoutes()
                .stream()
                .filter(route -> {
                    if (route.getPredicate() == null) {
                        return true;
                    } else {
                        return route.getPredicate().test(channel);
                    }
                })
                .findFirst()
                .orElseThrow(()->new GatewayException("no available route"));
    }
}
