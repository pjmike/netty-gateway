package com.pjmike.filter;

import com.pjmike.annotation.Order;
import com.pjmike.attribute.Attributes;
import com.pjmike.context.RequestContextUtil;
import com.pjmike.http.NettyHttpResponseUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @description: 接收内部服务的响应Filter
 * @author: pjmike
 * @create: 2019/11/29
 */
@Order(3)
public class NettyWriteResponseFilter implements GatewayFilter{
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) {
        FullHttpResponse response = channel.attr(Attributes.RESPONSE).get();
        if (response == null) {
            //TODO
            response = NettyHttpResponseUtil.getBlockResponse();
        } else {
            //将响应写回，TODO
            channel.writeAndFlush(response);
        }
        filterChain.filter(channel);
    }
}
