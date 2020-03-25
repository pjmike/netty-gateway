package com.pjmike.http;


import com.pjmike.route.Route;
import io.netty.handler.codec.http.HttpRequest;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static com.pjmike.common.constants.CommonConstants.HTTP;
import static com.pjmike.common.constants.CommonConstants.HTTPS;

/**
 * <p>
 * 此时Netty作为客户端转发HTTP请求，最简单的方法就是使用Netty提供的HttpRequest，略加封装
 * </p>
 * <p>
 * 一些开源项目选择自己定义HttpRequest和HttpResponse
 * </p>
 *
 * @author: pjmike
 * @create: 2019/12/19
 */
public class NettyClientHttpRequest {
    private Route route;
    private URI uri;
    private HttpRequest httpRequest;

    public NettyClientHttpRequest(Route route, URI uri, HttpRequest httpRequest) {
        this.route = route;
        this.uri = uri;
        this.httpRequest = httpRequest;
    }

    /**
     * 获取转发请求的Host
     *
     * @return
     */
    public String getHost() {
        if (uri.getHost() == null) {
            throw new RuntimeException("no host found");
        }
        return uri.getHost();
    }

    /**
     * 5
     * 获取端口号
     *
     * @return
     */
    public int getPort() {
        URL url = null;
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String protocol = url.getProtocol() == null ? HTTP : url.getProtocol();
        int port = url.getPort();
        if (port == -1) {
            if (HTTP.equalsIgnoreCase(protocol)) {
                port = 80;
            } else if (HTTPS.equalsIgnoreCase(protocol)) {
                port = 443;
            }
        }
        return port;
    }


    public InetSocketAddress getSocketAddress() {
        return new InetSocketAddress(getHost(), getPort());
    }

    public HttpRequest getHttpRequest() {
        return this.httpRequest;
    }

}
