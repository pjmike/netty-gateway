package com.pjmike.filter.route;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;


import com.pjmike.attribute.Attributes;
import com.pjmike.filter.GlobalFilter;
import com.pjmike.http.NettyHttpResponseUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/01/10
 */
@Slf4j
public class SentinelFilter extends GlobalFilter {
    public static final String GATEWAY_CONTEXT_ROUTE_PREFIX = "sentinel_gateway_context";
    @Override
    public void filter(Channel channel) throws Exception{
        Entry entry = null;
        try {
            String requestPath = getRequestPath(channel);
            ContextUtil.enter(GATEWAY_CONTEXT_ROUTE_PREFIX+requestPath);
            entry = SphU.entry(requestPath);
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
        System.err.println(httpRequest.refCnt());
        return uri.getPath();
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return -1;
    }
}
