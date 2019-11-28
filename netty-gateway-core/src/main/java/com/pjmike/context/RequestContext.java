package com.pjmike.context;

import com.pjmike.attribute.Attributes;
import com.pjmike.route.Route;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/26
 */
public class RequestContext {
    public static void setRequest(Channel channel, FullHttpRequest request) {
        channel.attr(Attributes.REQUEST).set(request);
    }

    public static void setKeepAlive(Channel channel, Boolean keepAlive) {
        channel.attr(Attributes.KEEPALIVE).set(keepAlive);
    }

    public static void setRoute(Channel channel, Route route) {
        channel.attr(Attributes.GATEWAY_ROUTE_ATTR).set(route);
    }
}
