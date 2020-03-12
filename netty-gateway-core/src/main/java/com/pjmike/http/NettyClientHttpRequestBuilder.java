package com.pjmike.http;


import com.pjmike.exception.GatewayException;
import com.pjmike.route.Route;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
public class NettyClientHttpRequestBuilder {
    private FullHttpRequest newRequest;
    private FullHttpRequest nativeRequest;
    private HttpRequestDecompose httpRequestDecompose;
    private Route route;

    public NettyClientHttpRequestBuilder(FullHttpRequest nativeRequest, Route route) {
        this.nativeRequest = nativeRequest;
        this.route = route;
        this.httpRequestDecompose = new HttpRequestDecompose(nativeRequest);
    }

    public NettyClientHttpRequest buildHttpRequest() throws Exception {
        URI uri = this.route.getUri();
        log.info("proxy_url is {}", uri.toString());
        QueryStringEncoder queryStringEncoder = buildNewRequestParams(uri.toString());
        this.newRequest = new DefaultFullHttpRequest(this.nativeRequest.protocolVersion(), this.nativeRequest.method(), queryStringEncoder.toUri().toASCIIString());

        //将原URL中的请求头提取出来，放入新的URL中
        buildNewRequestHeader();
        //设置长连接
        HttpUtil.setKeepAlive(this.newRequest, true);
        //设置请求体
        buildBody();
        //返回封装结果
        return new NettyClientHttpRequest(this.route, uri, this.newRequest);
    }
    /**
     * 构建转发请求的请求头
     *
     */
    private void buildNewRequestHeader() {
        HttpHeaders headers = this.nativeRequest.headers();
        headers.remove(HttpHeaderNames.COOKIE);
        headers.set(HttpHeaderNames.HOST, this.route.getUri().getHost());
        this.newRequest.headers().set(headers);
    }

    /**
     * build new uri
     * @param newUri
     */
    private QueryStringEncoder buildNewRequestParams(String newUri) throws GatewayException {
        try {
            URI nativeUri = new URI(this.nativeRequest.uri());
            newUri = newUri + nativeUri.getPath();
            QueryStringEncoder queryStringEncoder = new QueryStringEncoder(newUri);
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(this.nativeRequest.uri());
            Map<String, List<String>> parameters = queryStringDecoder.parameters();
            parameters.forEach((key, values) -> values.forEach(value -> {
                queryStringEncoder.addParam(key, value);
            }));
            return queryStringEncoder;
        } catch (URISyntaxException e) {
            throw new GatewayException(e, HttpResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    private void  buildBody() throws Exception {
        String contentType = getContentType();
        if (StringUtils.isEmpty(contentType)) {
            return;
        }
        ByteBuf content = Unpooled.copiedBuffer(this.nativeRequest.content());
        if (HttpHeaderValues.APPLICATION_JSON.toString().equals(contentType)) {
            this.newRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            this.newRequest.content().writeBytes(content);
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
            this.newRequest = (FullHttpRequest) requestEncoder.finalizeRequest();
        } else if (HttpHeaderValues.MULTIPART_FORM_DATA.toString().equals(contentType)) {
            //TODO
        } else {
            this.newRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            this.newRequest.content().writeBytes(content);
        }
    }

    /**
     * 获取ContentType
     *
     * @return
     */
    private String getContentType() {
        HttpHeaders headers = this.newRequest.headers();
        String contentType = headers.get(HttpHeaderNames.CONTENT_TYPE);
        if (StringUtils.containsIgnoreCase(contentType, ";")) {
            String[] strings = contentType.split(";");
            return strings[0];
        } else {
            return contentType;
        }
    }
}
