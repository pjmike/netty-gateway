package com.pjmike.filter.post;

import com.pjmike.attribute.Attributes;
import com.pjmike.context.ChannelContextUtil;
import com.pjmike.filter.GlobalFilter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;


/**
 * @description: 接收内部服务的响应Filter
 * @author: pjmike
 * @create: 2019/11/29
 */
@Slf4j
public class NettyWriteResponseFilter extends GlobalFilter {

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 100;
    }

    @Override
    public void filter(Channel channel) {
        log.info("serverChannel id : {} ", channel.id());
        FullHttpResponse response = ChannelContextUtil.getResponse(channel);
        Boolean keepAlive = ChannelContextUtil.getKeepAlive(channel);
        if (response == null) {
            return;
        }
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        } else {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        }
        ChannelFuture future = channel.writeAndFlush(response);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
