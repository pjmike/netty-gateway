package com.pjmike.handler.client;

import com.pjmike.attribute.Attributes;
import com.pjmike.context.RequestContextUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 读取下游响应数据的Handler
 * @author: pjmike
 * @create: 2019/12/01
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        //TODO 读取下游响应数据
        Channel channel = ctx.channel();
        Channel serverChannel = channel.attr(Attributes.SERVER_CHANNEL).get();
        //将响应数据保存到serverChannel中的attr
        RequestContextUtil.setResponse(serverChannel,msg);

        //TODO 将响应数据进行传递
    }
}
