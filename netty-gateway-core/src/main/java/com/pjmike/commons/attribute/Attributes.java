package com.pjmike.commons.attribute;

import com.pjmike.http.NettyClientHttpRequest;
import com.pjmike.route.Route;
import io.netty.channel.Channel;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.AttributeKey;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/26
 */
public class Attributes {
    public static final AttributeKey<FullHttpRequest> REQUEST = AttributeKey.newInstance("httpRequest");
    public static final AttributeKey<Throwable> EXCEPTION = AttributeKey.newInstance("exception");

    public static final AttributeKey<NettyClientHttpRequest> NETTY_PROXY_HTTP_REQUEST = AttributeKey.newInstance("netty_proxy_http_request");
    public static final AttributeKey<Boolean> KEEPALIVE = AttributeKey.newInstance("keepAlive");
    public static final AttributeKey<Route> GATEWAY_ROUTE_ATTR = AttributeKey.newInstance("gateway_route_attr");
    public static final AttributeKey<Channel> SERVER_CHANNEL = AttributeKey.newInstance("server_channel");
    public static final AttributeKey<FullHttpResponse> RESPONSE = AttributeKey.newInstance("httpResponse");
    public static final AttributeKey<SimpleChannelPool> CLIENT_POOL = AttributeKey.newInstance("clientPool");

    public static final AttributeKey<String> RPC_TYPE = AttributeKey.newInstance("rpc_type");


}
