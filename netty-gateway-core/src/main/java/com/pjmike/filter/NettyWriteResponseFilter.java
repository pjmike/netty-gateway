package com.pjmike.filter;

import com.pjmike.annotation.Order;
import com.pjmike.attribute.Attributes;
import com.pjmike.context.RequestContextUtil;
import com.pjmike.http.NettyHttpResponseUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @description: 接收内部服务的响应Filter
 * @author: pjmike
 * @create: 2019/11/29
 */
@Order(3)
@Slf4j
public class NettyWriteResponseFilter implements GatewayFilter{
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) {
        log.info("开始执行netty write response filter");
        FullHttpResponse response = channel.attr(Attributes.RESPONSE).get();
        if (response == null) {
            //TODO
            log.info("response is null, because on the blocking");
            response = NettyHttpResponseUtil.getBlockResponse();
            log.info("temper response, {}",response.content().toString(CharsetUtil.UTF_8));
        }
        channel.writeAndFlush(response);
        filterChain.filter(channel);
    }
}
