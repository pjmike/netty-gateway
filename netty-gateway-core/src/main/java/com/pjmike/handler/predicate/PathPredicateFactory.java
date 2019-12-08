package com.pjmike.handler.predicate;

import com.pjmike.attribute.Attributes;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;

import java.util.function.Predicate;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/08
 */
public class PathPredicateFactory implements PredicateFactory{
    @Override
    public Predicate<Channel> apply(PredicateDefinition predicateDefinition) {
        return channel -> {
            FullHttpRequest httpRequest = channel.attr(Attributes.REQUEST).get();
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.uri(), CharsetUtil.UTF_8);
            //路径
            String path = queryStringDecoder.path();
            if (StringUtils.equals(path, predicateDefinition.getValue())) {
                return true;
            }
            return false;
        };
    }
}
