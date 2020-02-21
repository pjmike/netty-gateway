package com.pjmike.context;

import com.pjmike.attribute.Attributes;
import com.pjmike.http.NettyHttpRequest;
import com.pjmike.route.Route;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/26
 */
public class RequestContextUtil {
    public static void setRequest(Channel channel, FullHttpRequest request) {
        channel.attr(Attributes.REQUEST).set(request);
    }

    public static FullHttpRequest getRequest(Channel channel) {
        return channel.attr(Attributes.REQUEST).get();
    }

    public static void setNettyHttpRequest(Channel channel, NettyHttpRequest nettyHttpRequest) {
        channel.attr(Attributes.NETTY_PROXY_HTTP_REQUEST).set(nettyHttpRequest);
    }
    public static void setKeepAlive(Channel channel, Boolean keepAlive) {
        channel.attr(Attributes.KEEPALIVE).set(keepAlive);
    }

    public static void setRoute(Channel channel, Route route) {
        channel.attr(Attributes.GATEWAY_ROUTE_ATTR).set(route);
    }

    public static Route getRoute(Channel channel) {
        return channel.attr(Attributes.GATEWAY_ROUTE_ATTR).get();
    }
    public static void setResponse(Channel channel, FullHttpResponse httpResponse) {
        channel.attr(Attributes.RESPONSE).set(httpResponse);
    }

    public static FullHttpResponse getResponse(Channel channel) {
        return channel.attr(Attributes.RESPONSE).get();
    }
}
