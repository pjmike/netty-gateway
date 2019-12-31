package com.pjmike.netty.client.handler;

import com.pjmike.attribute.Attributes;
import com.pjmike.utils.LockUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;


/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/19
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        log.info("Netty Client 读取响应数据: {}", msg.content().toString(Charset.forName("UTF-8")));

        Channel clientChannel = ctx.channel();
        Channel serverChannel = clientChannel.attr(Attributes.SERVER_CHANNEL).get();
        //将响应写回
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(msg.content()));
        response.headers().set(msg.headers());
        serverChannel.attr(Attributes.RESPONSE).set(response);
        clientChannel.attr(Attributes.CLIENT_POOL).get().release(clientChannel);
        LockUtil.countdownlatch.countDown();
    }
}
