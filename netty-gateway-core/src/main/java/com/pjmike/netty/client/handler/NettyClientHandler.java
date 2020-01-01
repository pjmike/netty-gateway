package com.pjmike.netty.client.handler;

import com.pjmike.attribute.Attributes;
import com.pjmike.http.NettyHttpResponseUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * 这里我们继承SimpleChannelInboundHandler类，该类对ChannelInboundHandlerAdapter作了扩展
 * 覆盖了父类的channelRead方法，增加了自动释放消息引用的处理逻辑，也就是在处理消息后，会调用ReferenceCountUtil.release自动释放消息应用
 * </p>
 *
 * <p>
 * 所以如果在channelRead0方法中消息了相应消息，比如下列代码中打印上诉出了{@Code FullHttpResponse},
 * 此时会release {@Code FullHttpResponse},使其引用计数减一，而我们的{@Code FullHttpResponse}实际需要暂存起来，
 * 向下传递，下游服务可能会继续消费 {@Code FullHttpResponse}，但是它的引用计数已经为0，再次消费会引发异常出现
 * </p>
 *
 * <p>
 * 解决方案是新建{@Code FullHttpResponse}，使用 {@Code Unpooled.copiedBuffer(...)} 来复制多一份内存数据
 * </p>
 * @author: pjmike
 * @create: 2019/12/19
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        log.info("Netty Client 读取响应数据: {}", msg.content().toString(CharsetUtil.UTF_8));
        Channel clientChannel = ctx.channel();
        Channel serverChannel = clientChannel.attr(Attributes.SERVER_CHANNEL).get();
        //将响应写回
        FullHttpResponse response = NettyHttpResponseUtil.buildSuccessResponse(serverChannel, msg);
        serverChannel.attr(Attributes.RESPONSE).set(response);
        clientChannel.attr(Attributes.CLIENT_POOL).get().release(clientChannel);
    }
}
