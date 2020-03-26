package com.pjmike.common.lb;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/25
 */
public class RandomLoadBalance implements LoadBalance {
    public static final String NAME = "random";

    @Override
    public String choose(List<String> targets, URI uri) {
        //TODO
        return targets.get(ThreadLocalRandom.current().nextInt(targets.size()));
    }
}
