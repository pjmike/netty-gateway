package com.pjmike.netty.server;

import com.pjmike.handler.HttpHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/22
 */
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
    public void start() throws Throwable {
        serverBootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss", true));
        workGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("worker", true));

        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childHandler(new HttpHandlerInitializer());
        //bind
        ChannelFuture future = serverBootstrap.bind(port);
        future.addListener(future1 -> {
            if (future1.isSuccess()) {
                System.out.println("端口绑定成功");
            } else {
                System.out.println("失败");
            }
        });
        //等待服务端Socket关闭
        future.channel().closeFuture().sync();
    }
}
