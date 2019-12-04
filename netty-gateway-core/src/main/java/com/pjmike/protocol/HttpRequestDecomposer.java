package com.pjmike.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.*;

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
        QueryStringDecoder stringDecoder = new QueryStringDecoder(getUri(), CharsetUtil.UTF_8);
        return stringDecoder.path();
    }

    /**
     * 获取请求参数
     *
     * @return
     */
    public Map<String, List<String>> getParams() {
        Map<String, List<String>> paramMap = new HashMap<>();
        HttpMethod method = httpRequest.method();
        if (Objects.equals(HttpMethod.GET,method)) {
            QueryStringDecoder stringDecoder = new QueryStringDecoder(getUri(), CharsetUtil.UTF_8);
            paramMap = stringDecoder.parameters();
        } else if (Objects.equals(HttpMethod.POST, method)) {
            paramMap = getPostParamMap(httpRequest);
        }
        return paramMap;
    }

    /**
     * 获取POST请求参数
     *
     * @param httpRequest
     * @return
     */
    private Map<String, List<String>> getPostParamMap(FullHttpRequest httpRequest) {
        Map<String, List<String>> paramMap = new HashMap<>();
        String contentType = getContentType(httpRequest.headers());
        if (HttpHeaderValues.APPLICATION_JSON.toString().equals(contentType)) {
            //TODO
            String content = httpRequest.content().toString();
            JSONObject jsonContent = JSON.parseObject(content);

        } else if (HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString().equals(contentType)) {
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.content().toString(CharsetUtil.UTF_8));
            paramMap = queryStringDecoder.parameters();
        }
        return paramMap;
    }

    /**
     * 获取ContentType
     * @param headers http请求头
     * @return
     */
    private String getContentType(HttpHeaders headers) {
        String contentType = headers.get(HttpHeaderNames.CONTENT_TYPE);
        String[] strings = contentType.split(";");
        return strings[0];
    }

    /**
     * 获取请求头
     *
     * @return
     */
    public Map<String, List<String>> getHeader() {
        Map<String, List<String>> header = new HashMap<>();
        httpRequest.headers().entries()
                .forEach(entry-> {
                    List<String> values = header.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());
                    values.add(entry.getValue());
                });
        return header;
    }

}
