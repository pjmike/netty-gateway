package com.pjmike.filter;

import com.alibaba.fastjson.JSONObject;
import com.pjmike.context.ChannelContextUtil;
import com.pjmike.http.HttpRequestDecompose;
import com.pjmike.utils.DubboUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @description: 协议转换 HTTP -> Dubbo
 * @author: pjmike
 * @create: 2020/02/15
 */
@Slf4j
public class DubboConvertFilter implements GatewayFilter{
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) throws Exception {
        FullHttpRequest request = ChannelContextUtil.getRequest(channel);
        URI route_uri = ChannelContextUtil.getRoute(channel).getUri();
        String scheme = route_uri.getScheme();
        if ("http".equals(scheme) || "https".equals(scheme)) {
            filterChain.filter(channel);
            return;
        }
        log.info("请求路由的uri是 : {} ,协议 {}", route_uri, scheme);
        //TODO
        String url = route_uri.toASCIIString();
        // 1. 解析url -> RPC接口name
        // 2. 解析出url中包含的body
//        HttpRequestDecompose httpRequestDecompose = new HttpRequestDecompose(request);
//        Map<String, List<String>> params = httpRequestDecompose.getParams();

        String interfaceClassName = url.substring(24);
        JSONObject result = DubboUtil.sendRequest(interfaceClassName);
        channel.writeAndFlush(request).addListener(ChannelFutureListener.CLOSE);
        return;
    }
}
