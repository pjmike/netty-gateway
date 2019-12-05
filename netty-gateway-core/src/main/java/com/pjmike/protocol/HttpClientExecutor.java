package com.pjmike.protocol;

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
public class HttpClientExecutor {
    public static FullHttpResponse execute(FullHttpRequest httpRequest, HttpRequestDecomposer requestDecomposer) throws IOException {
        HttpMethod method = httpRequest.method();
        String uri = requestDecomposer.getUri();
        Map<String, List<String>> params = requestDecomposer.getParams();
        Map<String, List<String>> header = requestDecomposer.getHeader();
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
        FullHttpResponse result = convert(response, httpRequest);
        return result;
    }

    /**
     * 将 {@link org.apache.http.HttpResponse} 转换成 {@link FullHttpResponse}
     *
     * @param httpResponse
     * @param originHttpRequest
     * @return
     * @throws IOException
     */
    private static FullHttpResponse convert(org.apache.http.HttpResponse httpResponse,FullHttpRequest originHttpRequest) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        String content = EntityUtils.toString(entity, CharsetUtil.UTF_8);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        HttpResponseStatus httpResponseStatus = new HttpResponseStatus(statusCode, httpResponse.getStatusLine().toString());
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(content.getBytes());
        return new DefaultFullHttpResponse(originHttpRequest.protocolVersion(), httpResponseStatus,byteBuf);
    }
}
