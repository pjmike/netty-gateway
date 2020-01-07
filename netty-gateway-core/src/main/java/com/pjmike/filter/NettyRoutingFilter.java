package com.pjmike.filter;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.pjmike.annotation.Order;
import com.pjmike.attribute.Attributes;
import com.pjmike.constants.SentinelConstants;
import com.pjmike.http.NettyHttpRequest;
import com.pjmike.http.NettyHttpRequestBuilder;
import com.pjmike.http.NettyHttpResponseUtil;
import com.pjmike.netty.client.NettyClient;
import com.pjmike.route.Route;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 *  路由转发过滤器
 * </p>
 *
 * <p>
 * 将HTTP请求进行转发，本filter只负责传递参数，调用相应网络类进行请求
 * </p>
 *
 * <p>
 *  有两种做法进行HTTP请求转发：
 *  1. 使用开源的HTTP网络库进行转发，比如HttpClient、OkHttp、Feign、Vert.x等
 *  2. 使用Netty自定义Netty客户端
 * </p>
 * @author: pjmike
 * @create: 2019/11/29
 */
@Order(2)
@Slf4j
public class NettyRoutingFilter implements GatewayFilter{
    private NettyHttpRequest nettyHttpRequest;
    private static NettyHttpRequestBuilder requestBuilder = new NettyHttpRequestBuilder();
    @Override
    @SentinelResource(value = SentinelConstants.KEY,fallback = "filterFallback")
    public void filter(Channel channel, GatewayFilterChain chain) {
        FullHttpRequest httpRequest = channel.attr(Attributes.REQUEST).get();
        Route route = channel.attr(Attributes.GATEWAY_ROUTE_ATTR).get();
        try {
            nettyHttpRequest = requestBuilder.buildHttpRequest(httpRequest, route);
            channel.attr(Attributes.NETTY_PROXY_HTTP_REQUEST).set(nettyHttpRequest);
            //TODO 下一步转发请求
            NettyClient.INSTANCE.request(nettyHttpRequest,channel);
        } catch (Exception e) {
            log.warn("build nettyHttpRequest failed, {}", e.getCause());
            throw new RuntimeException(e);
        }
        chain.filter(channel);
    }

    public void filterFallback(Channel channel, GatewayFilterChain chain, Throwable ex) {
        //TODO
        FullHttpResponse response = NettyHttpResponseUtil.buildFailResponse("proxy request failed");
        channel.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);
    }
}
