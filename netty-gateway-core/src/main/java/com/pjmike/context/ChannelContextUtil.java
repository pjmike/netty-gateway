package com.pjmike.context;

import com.pjmike.attribute.Attributes;
import com.pjmike.http.NettyClientHttpRequest;
import com.pjmike.route.Route;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/26
 */
public class ChannelContextUtil {
    public static void setRequest(Channel channel, FullHttpRequest request) {
        channel.attr(Attributes.REQUEST).set(request);
    }

    public static FullHttpRequest getRequest(Channel channel) {
        return channel.attr(Attributes.REQUEST).get();
    }

    public static void setNettyHttpRequest(Channel channel, NettyClientHttpRequest nettyClientHttpRequest) {
        channel.attr(Attributes.NETTY_PROXY_HTTP_REQUEST).set(nettyClientHttpRequest);
    }
    public static void setKeepAlive(Channel channel, Boolean keepAlive) {
        channel.attr(Attributes.KEEPALIVE).set(keepAlive);
    }

    public static Boolean getKeepAlive(Channel channel) {
        return channel.attr(Attributes.KEEPALIVE).get();
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

    public static void setException(Channel channel, Throwable throwable) {
        channel.attr(Attributes.EXCEPTION).set(throwable);
    }

    public static Throwable getException(Channel channel) {
        return channel.attr(Attributes.EXCEPTION).get();
    }
}
