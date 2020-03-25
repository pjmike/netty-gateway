package com.pjmike.filter.pre;

import com.pjmike.common.context.ApplicationContext;
import com.pjmike.common.context.ChannelContext;
import com.pjmike.exception.GatewayException;
import com.pjmike.filter.GlobalFilter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.net.URI;

/**
 * @description: 简单的鉴权Filter
 * @author: pjmike
 * @create: 2020/03/01
 */
@Slf4j
public class AuthTokenFilter extends GlobalFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return -50;
    }

    @Override
    public void filter(Channel channel) throws Exception {
        FullHttpRequest request = ChannelContext.getRequest(channel);
        String token = ApplicationContext.getInstance().getTokenMap().get(new URI(request.uri()).getPath());
        if (StringUtils.isBlank(token)) {
            return;
        }
        String authToken = request.headers().get("Auth_Token");
        if (StringUtils.isBlank(authToken)) {
            log.warn("url {} without token,authentication failed", request.uri());
            throw new GatewayException(HttpResponseStatus.BAD_REQUEST, "request without token,authentication failed");
        }
        if (!StringUtils.equals(token, authToken)) {
            log.warn("url {} token is wrong,authentication failed", request.uri());
            throw new GatewayException(HttpResponseStatus.BAD_REQUEST, "token is wrong,authentication failed");
        } else {
            log.info("url {} verify success", request.uri());
        }
    }
}
