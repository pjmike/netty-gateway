package com.pjmike.filter;


import io.netty.channel.Channel;

/**
 * @author: pjmike
 * @create: 2019/11/28
 */
public interface WebHandler {
    /**
     * 执行业务逻辑
     *
     * @param channel
     */
    void handle(Channel channel);
}
