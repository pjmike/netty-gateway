package com.pjmike.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/02/24
 */
public class GatewayException extends RuntimeException{
    private HttpResponseStatus status;
    private String message;

    public GatewayException(Throwable e,HttpResponseStatus status,String message) {
        super(message, e);
        this.status = status;
        this.message = message;
    }

    public GatewayException(HttpResponseStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
