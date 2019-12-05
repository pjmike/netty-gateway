package com.pjmike.filter;

import com.pjmike.context.RequestContextUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @description: 接收内部服务的响应Filter
 * @author: pjmike
 * @create: 2019/11/29
 */
public class NettyWriteResponseFilter implements GatewayFilter{
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) {
        FullHttpResponse response = RequestContextUtil.getResponse(channel);
        if (response == null) {
            //TODO
            return;
        }
        //将响应写回，TODO
        ChannelFuture channelFuture = channel.writeAndFlush(response);
    }
}
