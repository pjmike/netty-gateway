package com.pjmike.filter.error;

import com.pjmike.commons.context.ChannelContext;
import com.pjmike.filter.GlobalFilter;
import com.pjmike.http.NettyHttpResponseUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/02/28
 */
public class NettyErrorFilter extends GlobalFilter {
    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 1000;
    }

    @Override
    public void filter(Channel channel) throws Exception {
        Throwable exception = ChannelContext.getException(channel);
        if (exception != null) {
            channel.writeAndFlush(NettyHttpResponseUtil.buildFailResponse(exception.getMessage()))
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }
}
