package com.pjmike.filter;

import com.pjmike.context.ChannelContextUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * @description: 协议转换 HTTP -> Dubbo
 * @author: pjmike
 * @create: 2020/02/15
 */
@Slf4j
public class DubboConvertFilter implements GatewayFilter{
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) throws Exception {
        URI uri = ChannelContextUtil.getRoute(channel).getUri();
        String scheme = uri.getScheme();
        if ("http".equals(scheme) || "https".equals(scheme)) {
            filterChain.filter(channel);
            return;
        }
        log.info("请求路由的uri是 : {} ,协议 {}", uri, scheme);
        //TODO
    }
}
