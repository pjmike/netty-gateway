package com.pjmike.util;

import com.pjmike.attribute.Attributes;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/26
 */
public class ContextUtil {
    public static void setRequset(Channel channel, FullHttpRequest request) {
        channel.attr(Attributes.REQUEST).set(request);
    }

    public static void setKeepAlive(Channel channel, Boolean keepAlive) {
        channel.attr(Attributes.KEEPALIVE).set(keepAlive);
    }
}
