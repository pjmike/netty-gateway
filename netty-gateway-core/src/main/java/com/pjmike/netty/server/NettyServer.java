package com.pjmike.netty.server;

import com.pjmike.handler.HttpHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/22
 */
@Slf4j
public class NettyServer {
    /**
     * netty server bootstrap
     */
    private ServerBootstrap serverBootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private int port;
    public NettyServer(Integer port) {
        this.port = port;
    }
    public void start() {
        this.serverBootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss", true));
        this.workGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("worker", true));
        this.serverBootstrap.group(this.bossGroup, this.workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childHandler(new HttpHandlerInitializer());
        //bind
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) {
                log.info("server successfully started in {}", port);
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.warn("server start failed",e);
        }
    }

    public void shutdown() {
        try {
            if (this.bossGroup != null) {
                this.bossGroup.shutdownGracefully();
            }
            if (this.workGroup != null) {
                this.workGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            log.error("NettyServer shutdown exception, {}", e);
        }
    }
}
