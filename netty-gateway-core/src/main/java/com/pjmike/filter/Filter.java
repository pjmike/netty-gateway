package com.pjmike.filter;

import io.netty.channel.Channel;

/**
 * @author: pjmike
 * @create: 2020/02/24
 */
public interface Filter {
    /**
     * 过滤
     *
     * @param channel
     * @throws Exception
     */
    void filter(Channel channel) throws Exception;
}
