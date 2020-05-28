package com.pjmike.commons.lb;

import org.apache.dubbo.common.extension.ExtensionLoader;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/25
 */
public class RandomILoadBalance implements ILoadBalance {
    public static final String NAME = "random";

    @Override
    public String choose(List<String> targets, URI uri) {
        //TODO
        return targets.get(ThreadLocalRandom.current().nextInt(targets.size()));
    }

    public static void main(String[] args) {
            ExtensionLoader<ILoadBalance> extensionLoader =
                    ExtensionLoader.getExtensionLoader(ILoadBalance.class);
        RandomILoadBalance randomILoadBalance = (RandomILoadBalance) extensionLoader.getDefaultExtension();
        System.out.println(randomILoadBalance.getClass().getName());

    }
}
