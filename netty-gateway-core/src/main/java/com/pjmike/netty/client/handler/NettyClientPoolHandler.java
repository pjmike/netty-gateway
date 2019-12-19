package com.pjmike.netty.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.stream.ChunkedWriteHandler;
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

    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {

    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        log.info("channelCreated. Channel_ID is {}", ch.id());
        NioSocketChannel nioSocketChannel = (NioSocketChannel) ch;
        nioSocketChannel.config().setTcpNoDelay(true);
        nioSocketChannel.config().setKeepAlive(true);
        nioSocketChannel.pipeline()
                .addLast(new HttpClientCodec())
                .addLast(new HttpObjectAggregator(1024))
                .addLast(new NettyClientHandler());
    }
}
