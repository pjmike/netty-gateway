package com.pjmike.common.lb;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/25
 */
public class RandomLoadBalance implements LoadBalance {
    public static final String NAME = "random";
    private final List<String> urls;
    public RandomLoadBalance(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public String choose(String url) {
        //TODO
        return urls.get(ThreadLocalRandom.current().nextInt(urls.size()));
    }
}
