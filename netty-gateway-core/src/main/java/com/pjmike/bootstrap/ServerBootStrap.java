package com.pjmike.bootstrap;

import com.pjmike.netty.server.NettyServer;

/**
 * @author: pjmike
 * @create: 2019/12/08
 */
public class ServerBootStrap {
    public static void main(String[] args) {
        NettyServer server = new NettyServer(8989);
        try {
            server.start();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
