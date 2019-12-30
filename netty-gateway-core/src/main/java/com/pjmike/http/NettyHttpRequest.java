package com.pjmike.http;


import com.pjmike.route.Route;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Objects;

import static com.pjmike.constants.CommonConstants.HTTP;
import static com.pjmike.constants.CommonConstants.HTTPS;

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
public class NettyHttpRequest {
    private Route route;
    private URL url;
    private HttpRequest httpRequest;

    public NettyHttpRequest(Route route, URL url, HttpRequest httpRequest) {
        this.route = route;
        this.url = url;
        this.httpRequest = httpRequest;
    }

    /**
     * 获取转发请求的Host
     *
     * @return
     */
    public String getHost() {
        if (url.getHost() == null) {
            throw new RuntimeException("no host found");
        }
        return url.getHost();
    }

    /**
     * 5
     * 获取端口号
     *
     * @return
     */
    public int getPort() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NettyHttpRequest that = (NettyHttpRequest) o;
        return Objects.equals(route, that.route) &&
                Objects.equals(url, that.url) &&
                Objects.equals(httpRequest, that.httpRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(route, url, httpRequest);
    }
}
