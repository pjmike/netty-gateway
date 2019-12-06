package com.pjmike.filter;

import com.pjmike.attribute.Attributes;
import com.pjmike.context.RequestContextUtil;
import com.pjmike.execute.HttpClientExecutor;
import com.pjmike.protocol.HttpRequestDecompose;
import com.pjmike.route.Route;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;


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
 *  2. 使用Netty自定义Netty客户端，比较繁琐
 * </p>
 * @author: pjmike
 * @create: 2019/11/29
 */
public class NettyRoutingFilter implements GatewayFilter{
    @Override
    public void filter(Channel channel, GatewayFilterChain chain) {
        FullHttpRequest httpRequest = channel.attr(Attributes.REQUEST).get();
        Route route = channel.attr(Attributes.GATEWAY_ROUTE_ATTR).get();
        HttpRequestDecompose requestDecompose = new HttpRequestDecompose(httpRequest, route);
        FullHttpResponse httpResponse = null;
        try {
            httpResponse = HttpClientExecutor.getInstance().execute(httpRequest, requestDecompose);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestContextUtil.setResponse(channel, httpResponse);
        chain.filter(channel);
    }
}
