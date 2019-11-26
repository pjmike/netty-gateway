package com.pjmike.execute;

import io.netty.channel.Channel;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/26
 */
public class GatewayExecutor {
    public static final GatewayExecutor INSTANCE = new GatewayExecutor();

    public GatewayExecutor getInstance() {
        return INSTANCE;
    }
    public void execute(Channel channel) {
        //TODO
    }
}
