package com.pjmike.http;

import io.netty.buffer.Unpooled;
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
     * @param msg
     * @return
     */
    public static  FullHttpResponse buildSuccessResponse(FullHttpResponse msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(msg.content()));
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, msg.content().readableBytes());
        return response;
    }

    public static  FullHttpResponse buildResponse(String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(msg.getBytes()));
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, msg.getBytes().length);
        return response;
    }

    public static  FullHttpResponse buildTimeoutResponse() {
        String msg = "proxy request timeout";
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8)));
        // the headers need to be set
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    /**
     * build fail response
     * @return response
     */
    public static FullHttpResponse buildFailResponse(String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH.toString(), response.content().readableBytes());
        return response;
    }
}
