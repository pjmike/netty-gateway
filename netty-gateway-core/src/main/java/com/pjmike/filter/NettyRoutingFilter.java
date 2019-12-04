package com.pjmike.filter;

import com.pjmike.attribute.Attributes;
import com.pjmike.constants.HttpContants;
import com.pjmike.route.Route;
import com.pjmike.utils.HttpClientUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

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
        //获取请求方法
        HttpMethod method = httpRequest.method();
        ByteBuf content = httpRequest.content();
        //TODO 将后端转发逻辑交给新类去处理，本次filter只负责传递传参并调用

        //TODO 将本Channel的Request进行转发到Route对应的URI中去
        //TODO 所以是需要构建一个HttpClient，进行请求转发
        //TODO 两种做法：
        //TODO 1. 可以考虑使用开源的HttpClient进行转发,2. 自定义构建一个Client,使用Netty进行转发
        //我们先采用开源的HttpClient库进行使用
        try {
            switch (method.name()) {
                case HttpContants.GET:
                    HttpClientUtils.get(uri);
                    break;
                case HttpContants.POST:
                    HttpClientUtils.post(uri);
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
