package com.pjmike.protocol;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/04
 */
public class HttpRequestDecomposer {
    private FullHttpRequest httpRequest;

    public HttpRequestDecomposer(FullHttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    /**
     * 获取请求的uri
     *
     * @return
     */
    public String getUri() {
        return httpRequest.uri();
    }

    /**
     * 获取请求路径
     *
     * @return
     */
    public String getPath() {
        QueryStringDecoder stringDecoder = new QueryStringDecoder(getUri(), StandardCharsets.UTF_8);
        return stringDecoder.path();
    }

    /**
     * 获取请求参数
     *
     * @return
     */
    public Map<String, List<String>> getParams() {
        QueryStringDecoder stringDecoder = new QueryStringDecoder(getUri(), StandardCharsets.UTF_8);
        return stringDecoder.parameters();
    }

    public Map<String, List<String>> getHeader() {
        Map<String, List<String>> header = new HashMap<>();
        httpRequest.headers().entries()
                .forEach(stringStringEntry -> {
                    //TODO
                });
        return header;
    }
}
