package com.pjmike.http;


import com.pjmike.route.Route;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * @description: HTTP请求构造器
 * @author: pjmike
 * @create: 2019/12/19
 */
@Slf4j
public class NettyHttpRequestBuilder {
    private HttpRequest newRequest;
    private Route route;

    public NettyHttpRequest buildHttpRequest(final FullHttpRequest nativeRequest, final Route route) throws Exception {
        this.route = route;
        URL url = getURL();
        log.info("proxy_url is {}", url.toString());
        //请求路径
        QueryStringEncoder queryStringEncoder = new QueryStringEncoder(url.getPath());
        newRequest = new DefaultFullHttpRequest(nativeRequest.protocolVersion(), nativeRequest.method(), url.toString());
        //将原URL中的请求参数提取出来，放入新的URL中
        buildNewRequestParams(queryStringEncoder);

        //将原URL中的请求头提取出来，放入新的URL中
        buildNewRequestHeader(nativeRequest);

        //设置长连接
        HttpUtil.setKeepAlive(newRequest, true);

        //设置请求体
        buildBody(nativeRequest);

        //返回封装结果
        return new NettyHttpRequest(route, url, newRequest);
    }

    private URL getURL() throws Exception{
        return route.getUri().toURL();
    }

    /**
     * 构建转发请求的请求头
     *
     * @param nativeRequest
     */
    private void buildNewRequestHeader(HttpRequest nativeRequest) throws Exception{
        HttpHeaders headers = nativeRequest.headers();
        headers.remove(HttpHeaderNames.COOKIE);
        headers.set(HttpHeaderNames.HOST, getURL().getHost());
        newRequest.headers().set(headers);
    }

    /**
     * 构建转发请求的参数
     *
     * @param queryStringEncoder
     */
    private void buildNewRequestParams(QueryStringEncoder queryStringEncoder) {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(newRequest.uri());
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        parameters.forEach((key, values) -> values.forEach(value -> {
            queryStringEncoder.addParam(key, value);
        }));
    }

    private void buildBody(FullHttpRequest nativeRequest) throws Exception {
        String contentType = getContentType();
        if (StringUtils.isEmpty(contentType)) {
            //TODO
            return;
        }
        ByteBuf content = nativeRequest.content();
        if (HttpHeaderValues.APPLICATION_JSON.toString().equals(contentType)) {
            newRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            ((FullHttpRequest) newRequest).content().writeBytes(content);
        } else if (HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString().equals(contentType)) {
            HttpPostRequestEncoder requestEncoder = new HttpPostRequestEncoder(newRequest, false);
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(nativeRequest.content().toString(CharsetUtil.UTF_8));
            Map<String, List<String>> parameters = queryStringDecoder.parameters();
            parameters.forEach((key, values) -> values.forEach(value -> {
                try {
                    requestEncoder.addBodyAttribute(key, value);
                } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
                    e.printStackTrace();
                }
            }));
            newRequest = requestEncoder.finalizeRequest();
        } else if (HttpHeaderValues.MULTIPART_FORM_DATA.toString().equals(contentType)) {
            //TODO
        } else {
            newRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            ((FullHttpRequest) newRequest).content().writeBytes(content);
        }
    }

    /**
     * 获取ContentType
     *
     * @return
     */
    private String getContentType() {
        HttpHeaders headers = newRequest.headers();
        String contentType = headers.get(HttpHeaderNames.CONTENT_TYPE);
        if (StringUtils.containsIgnoreCase(contentType, ";")) {
            String[] strings = contentType.split(";");
            return strings[0];
        } else {
            return contentType;
        }
    }
}
