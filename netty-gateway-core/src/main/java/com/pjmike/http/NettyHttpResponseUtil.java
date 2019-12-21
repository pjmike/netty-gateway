package com.pjmike.http;

import com.alibaba.fastjson.JSONObject;
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
    public static  FullHttpResponse getSuccessResponse(final Channel channel,FullHttpResponse response) {
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        Boolean keepAlive = channel.attr(Attributes.KEEPALIVE).get();
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        return response;
    }

    public static  FullHttpResponse getBlockResponse() {
        JSONObject object = new JSONObject();
        object.put("code", 1000);
        object.put("message", "proxy service blocked");
        ByteBuf byteBuf = Unpooled.wrappedBuffer(object.toString().getBytes(CharsetUtil.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,byteBuf);
        return response;
    }
}
