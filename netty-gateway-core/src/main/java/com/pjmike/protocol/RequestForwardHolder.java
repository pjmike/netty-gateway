package com.pjmike.protocol;

import com.pjmike.route.Route;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @description: 进行请求转发的内容容器
 * @author: pjmike
 * @create: 2019/12/01
 */
@Data
@NoArgsConstructor
public class RequestForwardHolder {
    private Route route;
    private URI uri;
    private HttpRequest httpRequest;

    public RequestForwardHolder(Route route, URI uri, HttpRequest httpRequest) {
        this.route = route;
        this.uri = uri;
        this.httpRequest = httpRequest;
    }

    private String getHost() {
        String host = uri.getHost();
        if (host == null) {
            throw new RuntimeException("no host found");
        }
        return host;
    }

    public InetSocketAddress getSocketAddress() {
        return new InetSocketAddress(getHost(), getPort());
    }
    private int getPort() {
        return uri.getPort();
    }
    //TODO 如何获取URL，也就是将URI转换成URL
}
