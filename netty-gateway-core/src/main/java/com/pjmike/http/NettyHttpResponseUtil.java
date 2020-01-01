package com.pjmike.http;

import com.pjmike.attribute.Attributes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @description: 响应工具类
 * @author: pjmike
 * @create: 2019/12/19
 */
public class NettyHttpResponseUtil {
    /**
     * build success response
     *
     * @param channel
     * @param msg
     * @return
     */
    public static  FullHttpResponse buildSuccessResponse(final Channel channel, FullHttpResponse msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(msg.content()));
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, msg.content().readableBytes());
        Boolean keepAlive = channel.attr(Attributes.KEEPALIVE).get();
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        return response;
    }

    public static  FullHttpResponse buildBlockResponse() {
        String msg = "proxy http request is running";
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8)));
        // the headers need to be set
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    /**
     * build fail response
     * @param status http status
     * @return response
     */
    public static FullHttpResponse buildFailResponse(HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH.toString(), response.content().readableBytes());
        return response;
    }
}
