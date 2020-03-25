package com.pjmike.handler;


import com.pjmike.common.context.ApplicationContext;
import com.pjmike.common.context.ChannelContext;
import com.pjmike.http.NettyHttpResponseUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;



/**
 * <p>
 * 一个完整的HTTP请求{@link io.netty.handler.codec.http.FullHttpRequest}
 * 由HttpRequest {@link HttpRequest}, HttpContent {@link io.netty.handler.codec.http.HttpContent}，
 * 和LastHttpContent {@link io.netty.handler.codec.http.LastHttpContent} 组成
 * </p>
 *
 * <p>
 * HttpRequest: 包含HTTP的头部信息
 * HttpContent: 包含了数据部分,后面可能跟着一个或多个HttpContent部分
 * LastHttpContent: 标记该HTTP请求的结束
 * </p>
 *
 * @author: pjmike
 * @create: 2019/11/25
 */
@Slf4j
public class DispatcherHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        Channel channel = ctx.channel();
        boolean keepAlive = HttpUtil.isKeepAlive(httpRequest);
        ChannelContext.setRequest(channel, httpRequest);
        ChannelContext.setKeepAlive(channel, keepAlive);
        //execute the http proxy request
        ApplicationContext.getInstance().getGatewayExecutor().execute(channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception",cause);
        ctx.channel().writeAndFlush(NettyHttpResponseUtil.buildFailResponse(cause.getMessage()))
                .addListener(ChannelFutureListener.CLOSE);
    }
}
