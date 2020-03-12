package com.pjmike.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * All Handled exceptions in gateway are GatewayException，it's unchecked
 * @author: pjmike
 * @create: 2020/02/24
 */
public class GatewayException extends Exception{
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
