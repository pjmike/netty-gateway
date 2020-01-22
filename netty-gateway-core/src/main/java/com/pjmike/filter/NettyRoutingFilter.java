package com.pjmike.filter;

import com.pjmike.annotation.Order;
import com.pjmike.attribute.Attributes;
import com.pjmike.context.RequestContextUtil;
import com.pjmike.http.NettyHttpRequest;
import com.pjmike.http.NettyHttpRequestBuilder;
import com.pjmike.netty.client.NettyClient;
import com.pjmike.route.Route;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;



/**
 * <p>
 * 路由转发过滤器
 * </p>
 *
 * <p>
 * 将HTTP请求进行转发，本filter只负责传递参数，调用相应网络类进行请求
 * </p>
 *
 * <p>
 * 有两种做法进行HTTP请求转发：
 * 1. 使用开源的HTTP网络库进行转发，比如HttpClient、OkHttp、Feign、Vert.x等
 * 2. 使用Netty自定义Netty客户端
 * </p>
 *
 * @author: pjmike
 * @create: 2019/11/29
 */
@Order(50)
@Slf4j
public class NettyRoutingFilter implements GatewayFilter {
    private NettyHttpRequest nettyHttpRequest;
    private static NettyHttpRequestBuilder requestBuilder = new NettyHttpRequestBuilder();
    @Override
    public void filter(Channel channel, GatewayFilterChain chain) throws Exception {
        FullHttpRequest httpRequest = RequestContextUtil.getRequest(channel);
        Route route = RequestContextUtil.getRoute(channel);

        nettyHttpRequest = requestBuilder.buildHttpRequest(httpRequest, route);
        RequestContextUtil.setNettyHttpRequest(channel,nettyHttpRequest);

        NettyClient.getInstance().request(nettyHttpRequest, channel);
        chain.filter(channel);
    }
}
