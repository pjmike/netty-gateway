package com.pjmike.handler.predicate;

import io.netty.channel.Channel;

import java.util.function.Predicate;

/**
 * @author: pjmike
 * @create: 2019/12/08
 */
public interface PredicateFactory {
    Predicate<Channel> apply(PredicateDefinition predicateDefinition);
}
