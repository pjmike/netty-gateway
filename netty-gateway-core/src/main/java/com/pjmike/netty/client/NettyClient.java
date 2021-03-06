package com.pjmike.netty.client;

import com.pjmike.commons.attribute.Attributes;
import com.pjmike.http.NettyClientHttpRequest;

import com.pjmike.netty.client.handler.NettyClientPoolHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import lombok.extern.slf4j.Slf4j;


/**
 * @description: netty_proxy_client
 * @author: pjmike
 * @create: 2019/12/19
 */
@Slf4j
public class NettyClient {
    private final EventLoopGroup group = new NioEventLoopGroup(4 * 2);
    private final Bootstrap bootstrap = new Bootstrap();
    private ChannelPoolMap<NettyClientHttpRequest, SimpleChannelPool> channelPoolMap;

    private static NettyClient instance = new NettyClient();
    public static NettyClient getInstance() {
        return instance;
    }
    public NettyClient() {
        this.bootstrap.group(this.group)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 500)
                .option(ChannelOption.TCP_NODELAY, true)
                .channel(NioSocketChannel.class);
        this.channelPoolMap = new AbstractChannelPoolMap<NettyClientHttpRequest, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(NettyClientHttpRequest key) {

                return new FixedChannelPool(bootstrap.remoteAddress(key.getSocketAddress()), new NettyClientPoolHandler(), 50);
            }
        };
    }

    public void request(final NettyClientHttpRequest httpRequest, final Channel serverChannel) throws InterruptedException {

        SimpleChannelPool pool = channelPoolMap.get(httpRequest);
        Future<Channel> channelFuture = pool.acquire().sync();
        channelFuture.addListener((FutureListener<Channel>) future -> {
            if (future.isSuccess()) {
                Channel clientChannel = future.getNow();
                //clientChannel bind with serverChannel
                clientChannel.attr(Attributes.SERVER_CHANNEL).set(serverChannel);
                clientChannel.attr(Attributes.CLIENT_POOL).set(pool);

                //write data
                clientChannel.writeAndFlush(httpRequest.getHttpRequest());
            }
        });
    }
}
