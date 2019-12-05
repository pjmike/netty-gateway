package com.pjmike.filter;

import com.pjmike.attribute.Attributes;
import com.pjmike.context.RequestContextUtil;
import com.pjmike.protocol.HttpClientExecutor;
import com.pjmike.protocol.HttpRequestDecomposer;
import com.pjmike.route.Route;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.io.IOException;

/**
 * @description: 路由转发过滤器
 * @author: pjmike
 * @create: 2019/11/29
 */
public class NettyRoutingFilter implements GatewayFilter{
    @Override
    public void filter(Channel channel, GatewayFilterChain chain) {
        FullHttpRequest httpRequest = channel.attr(Attributes.REQUEST).get();
        Route route = channel.attr(Attributes.GATEWAY_ROUTE_ATTR).get();
        HttpRequestDecomposer requestDecomposer = new HttpRequestDecomposer(httpRequest, route);
        //TODO 将后端转发逻辑交给新类去处理，本次filter只负责传递传参并调用
        //      将本Channel的Request进行转发到Route对应的URI中去
        //        所以是需要构建一个HttpClient，进行请求转发
        //         两种做法：
        //           1. 可以考虑使用开源的Http网络库进行转发,比如HttpClient,OkHttp,Feign
        //           2. 自定义构建一个Client,使用Netty进行转发
        try {
             FullHttpResponse httpResponse = HttpClientExecutor.execute(httpRequest, requestDecomposer);
            RequestContextUtil.setResponse(channel, httpResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        chain.filter(channel);
    }
}
