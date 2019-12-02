package com.pjmike.filter;

import com.pjmike.attribute.Attributes;
import com.pjmike.route.Route;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @description: 路由转发过滤器
 * @author: pjmike
 * @create: 2019/11/29
 */
public class NettyRoutingFilter implements GatewayFilter{
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) {
        FullHttpRequest httpRequest = channel.attr(Attributes.REQUEST).get();
        Route route = channel.attr(Attributes.GATEWAY_ROUTE_ATTR).get();
        //获得uri
        String uri = httpRequest.uri();
        //TODO 将后端转发逻辑交给新类去处理，本次filter只负责传递传参并调用

        //TODO 将本Channel的Request进行转发到Route对应的URI中去
        //TODO 所以是需要构建一个HttpClient，进行请求转发
        //TODO 两种做法：
        //TODO 1. 可以考虑使用开源的HttpClient进行转发,2. 自定义构建一个Client,使用Netty进行转发
        //我们先采用开源的HttpClient库进行使用


    }
}
