package com.pjmike.http;


import com.pjmike.route.Route;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @description: HTTP请求构造器
 * @author: pjmike
 * @create: 2019/12/19
 */
@Slf4j
public class NettyHttpRequestBuilder {
    private FullHttpRequest newRequest;
    private FullHttpRequest nativeRequest;
    private Route route;

    public NettyHttpRequest buildHttpRequest(final FullHttpRequest nativeRequest, final Route route) throws Exception {
        setFields(nativeRequest, route);
        URI uri = route.getUri();
        log.info("proxy_url is {}", uri.toString());
        QueryStringEncoder queryStringEncoder = buildNewRequestParams(uri.toString(), nativeRequest);
        newRequest = new DefaultFullHttpRequest(nativeRequest.protocolVersion(), nativeRequest.method(), queryStringEncoder.toUri().toASCIIString());

        //将原URL中的请求头提取出来，放入新的URL中
        buildNewRequestHeader();
        //设置长连接
        HttpUtil.setKeepAlive(newRequest, true);
        //设置请求体
        buildBody();
        //返回封装结果
        return new NettyHttpRequest(route, uri, newRequest);
    }

    private void setFields(FullHttpRequest nativeRequest,Route route) {
        this.route = route;
        this.nativeRequest = nativeRequest;
    }
    /**
     * 构建转发请求的请求头
     *
     */
    private void buildNewRequestHeader() {
        HttpHeaders headers = nativeRequest.headers();
        headers.remove(HttpHeaderNames.COOKIE);
        headers.set(HttpHeaderNames.HOST, route.getUri().getHost());
        newRequest.headers().set(headers);
    }

    /**
     * build new uri
     * @param newUri
     * @param nativeRequest
     */
    private QueryStringEncoder buildNewRequestParams(String newUri,FullHttpRequest nativeRequest) {
        try {
            URI nativeUri = new URI(nativeRequest.uri());
            newUri = newUri + nativeUri.getPath();
            QueryStringEncoder queryStringEncoder = new QueryStringEncoder(newUri);
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(nativeRequest.uri());
            Map<String, List<String>> parameters = queryStringDecoder.parameters();
            parameters.forEach((key, values) -> values.forEach(value -> {
                queryStringEncoder.addParam(key, value);
            }));
            return queryStringEncoder;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("system error");
        }
    }
    private void buildBody() throws Exception {
        String contentType = getContentType();
        if (StringUtils.isEmpty(contentType)) {
            //TODO
            return;
        }
        ByteBuf content = nativeRequest.content();
        if (HttpHeaderValues.APPLICATION_JSON.toString().equals(contentType)) {
            newRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            newRequest.content().writeBytes(content);
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
            newRequest = (FullHttpRequest) requestEncoder.finalizeRequest();
        } else if (HttpHeaderValues.MULTIPART_FORM_DATA.toString().equals(contentType)) {
            //TODO
        } else {
            newRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            newRequest.content().writeBytes(content);
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
