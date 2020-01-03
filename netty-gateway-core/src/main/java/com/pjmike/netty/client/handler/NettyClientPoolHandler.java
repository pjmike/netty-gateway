package com.pjmike.netty.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/19
 */
@Slf4j
public class NettyClientPoolHandler implements ChannelPoolHandler {
    @Override
    public void channelReleased(Channel ch) throws Exception {
        log.info("channelCreated. Channel_ID is {}", ch.id());
    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {
        log.info("channelCreated. Channel_ID is {}", ch.id());
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        log.info("channelCreated. Channel_ID is {}", ch.id());
        NioSocketChannel socketChannel = (NioSocketChannel) ch;
        socketChannel.config().setTcpNoDelay(true);
        socketChannel.config().setKeepAlive(true);
        socketChannel.pipeline()
                .addLast(new HttpClientCodec())
                .addLast(new HttpObjectAggregator(1024))
                .addLast("ReadTimeoutHandler",new ReadTimeoutHandler(30))
                .addLast("WriteTimeoutHandler",new WriteTimeoutHandler(5))
                .addLast(new NettyClientHandler());
    }
}
