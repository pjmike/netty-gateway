package com.pjmike.handler;

import com.pjmike.context.RequestContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  一个完整的HTTP请求{@link io.netty.handler.codec.http.FullHttpRequest}
 *  由HttpRequest {@link HttpRequest}, HttpContent {@link io.netty.handler.codec.http.HttpContent}，
 *  和LastHttpContent {@link io.netty.handler.codec.http.LastHttpContent} 组成
 * </p>
 *
 * <p>
 *  HttpRequest: 包含HTTP的头部信息
 *  HttpContent: 包含了数据部分,后面可能跟着一个或多个HttpContent部分
 *  LastHttpContent: 标记该HTTP请求的结束
 * </p>
 *
 * @author: pjmike
 * @create: 2019/11/25
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        Channel channel = ctx.channel();
        boolean keepAlive = HttpUtil.isKeepAlive(httpRequest);
        RequestContext.setRequest(channel, httpRequest);
        RequestContext.setKeepAlive(channel, keepAlive);
        //TODO 执行GatewayExecutor的逻辑
        //TODO 需要将FilterWebHandler和RouteLocator注入GatewayExecutor
    }
}
