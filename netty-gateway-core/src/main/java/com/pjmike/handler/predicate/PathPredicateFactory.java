package com.pjmike.handler.predicate;

import com.pjmike.common.attribute.Attributes;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.function.Predicate;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/08
 */
public class PathPredicateFactory implements PredicateFactory{
    private final PathMatcher pathMatcher = new AntPathMatcher();
    @Override
    public Predicate<Channel> apply(PredicateDefinition predicateDefinition) {
        return channel -> {
            FullHttpRequest httpRequest = channel.attr(Attributes.REQUEST).get();
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.uri(), CharsetUtil.UTF_8);
            //利用 spring中的AntPathMatcher进行 Ant-Style Path Match
            return pathMatcher.match(predicateDefinition.getValue(), queryStringDecoder.path());
        };
    }
}
