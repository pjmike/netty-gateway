package com.pjmike.filter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;


import com.pjmike.annotation.Order;
import com.pjmike.attribute.Attributes;
import com.pjmike.http.NettyHttpResponseUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/01/10
 */
@Order(-1)
@Slf4j
public class SentinelGatewayFilter implements GatewayFilter{
    public static final String GATEWAY_CONTEXT_ROUTE_PREFIX = "sentinel_gateway_context";
    public static final String TOTAL_URL_REQUEST = "total-url-request";
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) throws Exception{
        Entry entry = null;
        try {
            String requestPath = getRequestPath(channel);
            ContextUtil.enter(GATEWAY_CONTEXT_ROUTE_PREFIX+requestPath);
            entry = SphU.entry(requestPath);
            filterChain.filter(channel);
        } catch (Exception e) {
            fallbackHandler(channel);
        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    private void fallbackHandler(Channel channel) {
        channel.writeAndFlush(NettyHttpResponseUtil.buildFailResponse("proxy request failed"))
                .addListener(ChannelFutureListener.CLOSE);
    }

    private String getRequestPath(Channel channel) throws URISyntaxException {
        FullHttpRequest httpRequest = channel.attr(Attributes.REQUEST).get();
        URI uri = new URI(httpRequest.uri());
        return uri.getPath();
    }
}
