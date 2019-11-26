package com.pjmike.attribute;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/26
 */
public class Attributes {
    public static final AttributeKey<FullHttpRequest> REQUEST = AttributeKey.newInstance("httpRequest");
    public static final AttributeKey<Boolean> KEEPALIVE = AttributeKey.newInstance("keepAlive");

}
