package com.pjmike;

import com.pjmike.netty.server.NettyServer;

/**
 * @author: pjmike
 * @create: 2019/12/08
 */
public class Application {
    public static void main(String[] args) {
        NettyServer server = new NettyServer(8989);
        try {
            server.start();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
        //钩子函数
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    }
}
