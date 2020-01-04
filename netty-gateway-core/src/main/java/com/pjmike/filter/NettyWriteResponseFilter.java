package com.pjmike.filter;

import com.pjmike.annotation.Order;
import com.pjmike.attribute.Attributes;
import com.pjmike.constants.CommonConstants;
import com.pjmike.http.NettyHttpResponseUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import io.netty.util.Timeout;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description: 接收内部服务的响应Filter
 * @author: pjmike
 * @create: 2019/11/29
 */
@Order(3)
@Slf4j
public class NettyWriteResponseFilter implements GatewayFilter{
    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) {
        FullHttpResponse response = getResponse(channel);
        channel.writeAndFlush(response);
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
}
