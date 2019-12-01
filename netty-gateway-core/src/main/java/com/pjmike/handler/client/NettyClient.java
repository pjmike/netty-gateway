package com.pjmike.handler.client;

import com.pjmike.protocol.RequestForwardHolder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * <p>
 *  访问下游服务的客户端
 * </p>
 *
 * <p>
 * 使用Netty自有的连接池，也就是先建立若干连接，当有请求过来时，从连接池中取一个
 * </p>
 * @author: pjmike
 * @create: 2019/12/01
 */
public class NettyClient {
    private final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private final Bootstrap bootstrap = new Bootstrap();
    private ChannelPoolMap<RequestForwardHolder, SimpleChannelPool> poolMap;
    private static final NettyClient INSTANCE = new NettyClient();

    public NettyClient() {
        bootstrap.group(eventLoopGroup)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class);
        poolMap = new AbstractChannelPoolMap<RequestForwardHolder, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(RequestForwardHolder key) {
                return new FixedChannelPool(bootstrap, new NettyClientChannelPoolHandler(), 8);
            }
        };
    }
    //TODO 建立连接，发送请求

    public static NettyClient getInstance() {
        return INSTANCE;
    }
}
