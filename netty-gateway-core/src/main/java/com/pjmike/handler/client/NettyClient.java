package com.pjmike.handler.client;

import com.pjmike.attribute.Attributes;
import com.pjmike.protocol.RequestForwardHolder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * <p>
 *  访问下游服务的客户端
 * </p>
 * @author: pjmike
 * @create: 2019/12/01
 */
public class NettyClient {
    private final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private final Bootstrap bootstrap = new Bootstrap();
    private static final NettyClient INSTANCE = new NettyClient();

    public NettyClient() {
        bootstrap.group(eventLoopGroup)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
    }
    public static NettyClient getInstance() {
        return INSTANCE;
    }

    public synchronized void sendRequest(RequestForwardHolder requestForwardHolder, Channel serverChannel) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(requestForwardHolder.getSocketAddress()).sync();
        Channel channel = channelFuture.channel();
        channel.attr(Attributes.SERVER_CHANNEL).set(serverChannel);
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                //TODO 发送的数据还需要进一步封装，去掉多余的信息
                channel.writeAndFlush(requestForwardHolder);
            }
        });
    }
}
