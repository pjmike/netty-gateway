package com.pjmike.execute;

import com.pjmike.http.HttpRequestDecompose;
import com.pjmike.utils.HttpClientUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/05
 */
public class HttpClientExecutor extends AbstractExecutor<FullHttpResponse> {

    private static HttpClientExecutor INSTANCE = new HttpClientExecutor();

    public static HttpClientExecutor getInstance() {
        return INSTANCE;
    }
    /**
     * 执行HTTP请求
     *
     * @param httpRequest
     * @param requestDecompose
     * @return {@link FullHttpResponse}
     * @throws IOException
     */
    private FullHttpResponse execute(FullHttpRequest httpRequest, HttpRequestDecompose requestDecompose) throws IOException {
        HttpMethod method = httpRequest.method();
        String uri = requestDecompose.getUri();
        Map<String, List<String>> params = requestDecompose.getParams();
        Map<String, List<String>> header = requestDecompose.getHeader();
        org.apache.http.HttpResponse response = null;
        try {
            if (Objects.equals(HttpMethod.GET, method)) {
                response = HttpClientUtils.get(uri, header, params);
            } else if (Objects.equals(HttpMethod.POST, method)) {
              response = HttpClientUtils.post(uri, header, params);
            } else {
                //TODO
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convert(response, httpRequest);
    }

    /**
     * 将 {@link org.apache.http.HttpResponse} 转换成 {@link FullHttpResponse}
     *
     * @param httpResponse
     * @param originHttpRequest
     * @return
     * @throws IOException
     */
    private FullHttpResponse convert(org.apache.http.HttpResponse httpResponse,FullHttpRequest originHttpRequest) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        String content = EntityUtils.toString(entity, CharsetUtil.UTF_8);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        HttpResponseStatus httpResponseStatus = new HttpResponseStatus(statusCode, httpResponse.getStatusLine().toString());
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(content.getBytes());
        return new DefaultFullHttpResponse(originHttpRequest.protocolVersion(), httpResponseStatus,byteBuf);
    }

    @Override
    protected FullHttpResponse doExecute(Object... args) throws IOException {
        FullHttpRequest httpRequest = (FullHttpRequest)args[0];
        HttpRequestDecompose requestDecompose = (HttpRequestDecompose)args[1];
        return execute(httpRequest, requestDecompose);
    }
}
