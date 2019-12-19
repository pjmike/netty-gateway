package com.pjmike.netty.client.handler;

import com.pjmike.attribute.Attributes;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;


/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/19
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        Channel clientChannel = ctx.channel();
        Channel serverChannel = clientChannel.attr(Attributes.SERVER_CHANNEL).get();
        //TODO 将响应写回
        clientChannel.attr(Attributes.CLIENT_POOL).get().release(clientChannel);
    }
}
