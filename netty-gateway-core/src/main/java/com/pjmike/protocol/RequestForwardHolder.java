package com.pjmike.protocol;

import com.pjmike.route.Route;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

/**
 * @description: 进行请求转发的内容容器
 * @author: pjmike
 * @create: 2019/12/01
 */
@Data
@NoArgsConstructor
public class RequestForwardHolder {
    private Route route;
    private URL url;
    private HttpRequest httpRequest;

    public RequestForwardHolder(Route route, URL url, HttpRequest httpRequest) {
        this.route = route;
        this.url = url;
        this.httpRequest = httpRequest;
    }
    //TODO 如何获取URL，也就是将URI转换成URL
}
