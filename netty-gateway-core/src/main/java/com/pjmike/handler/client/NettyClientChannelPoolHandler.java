package com.pjmike.handler.client;

import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/01
 */
public class NettyClientChannelPoolHandler implements ChannelPoolHandler {
    @Override
    public void channelReleased(Channel ch) throws Exception {
        //TODO
    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {
        //TODO
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        //TODO
    }
}
