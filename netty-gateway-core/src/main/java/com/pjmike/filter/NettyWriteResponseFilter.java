package com.pjmike.filter;

import com.pjmike.annotation.Order;
import com.pjmike.attribute.Attributes;
import com.pjmike.constants.CommonConstants;
import io.netty.channel.Channel;
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
@Order(3)
@Slf4j
public class NettyWriteResponseFilter implements GatewayFilter {
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) {
        log.info("serverChannel id : {} ",channel.id());
        FullHttpResponse response = getResponse(channel);
        buildHeaders(response, channel);
        channel.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);

    }

    /**
     * 消费者同步等待获取响应数据
     *
     * @param channel
     * @return
     */
    private FullHttpResponse getResponse(Channel channel) {
        synchronized (CommonConstants.OBJECT) {
            while (channel.attr(Attributes.RESPONSE).get() == null) {
                try {
                    CommonConstants.OBJECT.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return channel.attr(Attributes.RESPONSE).get();
        }
    }

    /**
     * build headers
     *
     * @param response
     * @param channel
     */
    private void buildHeaders(FullHttpResponse response, Channel channel) {
        if (response == null) {
            return;
        }
        Boolean keepAlive = channel.attr(Attributes.KEEPALIVE).get();
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
    }
}
