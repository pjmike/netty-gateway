package com.pjmike.server;

import com.pjmike.handler.HttpHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/22
 */
public class NettyServer extends AbstractServer{
    /**
     * netty server bootstrap
     */
    private ServerBootstrap serverBootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    public NettyServer(String host, Integer port) {
        super(host,port);
    }
    @Override
    protected void start() throws Throwable {
        serverBootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss", true));
        workGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("worker", true));

        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childHandler(new HttpHandlerInitializer());
        //bind
        ChannelFuture channelFuture = serverBootstrap.bind(getBindAddress());
        channelFuture.syncUninterruptibly();
    }

    @Override
    protected void stop() throws Throwable {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
