package com.pjmike.handler;

import com.pjmike.constants.CommonConstants;
import com.pjmike.context.RequestContextUtil;
import com.pjmike.execute.GatewayExecutor;
import com.pjmike.execute.InitExecutor;
import io.netty.channel.Channel;
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
    private GatewayExecutor gatewayExecutor = (GatewayExecutor) InitExecutor.gatewayConfig.get(CommonConstants.GATEWAY_EXECUTOR_NAME);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        Channel channel = ctx.channel();
        boolean keepAlive = HttpUtil.isKeepAlive(httpRequest);
        RequestContextUtil.setRequest(channel, httpRequest);
        RequestContextUtil.setKeepAlive(channel, keepAlive);
        //执行HTTP转发请求
        gatewayExecutor.execute(channel);
    }
}
