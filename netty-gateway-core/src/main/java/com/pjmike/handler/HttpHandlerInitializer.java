package com.pjmike.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/26
 */
public class HttpHandlerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("httpServerCodec", new HttpServerCodec(8192,102400,102400));
        //聚合 HttpMessage 和 HttpContent
        pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(1024*1024));
        pipeline.addLast("httpServerHandler", new DispatcherHandler());
    }
}
