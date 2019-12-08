package com.pjmike.handler.predicate;

import io.netty.channel.Channel;

import java.util.function.Predicate;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/08
 */
public class PredicateUtils {
    public static Predicate<Channel> path(String text) {
        return new PathPredicateFactory().apply(new PredicateDefinition(text));
    }
    //TODO
}
