package com.pjmike.server;

import java.net.InetSocketAddress;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/22
 */
public abstract class AbstractServer {
    private InetSocketAddress bindAddress;
    public AbstractServer(String host,Integer port) {
        this.bindAddress = new InetSocketAddress(host, port);
        try {
            start();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    protected abstract void start() throws Throwable;

    protected abstract void stop() throws Throwable;

    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }
}
