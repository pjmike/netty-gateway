package com.pjmike.commons.lb;

import org.apache.dubbo.common.extension.SPI;

import java.net.URI;
import java.util.List;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/25
 */
@SPI(RandomLoadBalance.NAME)
public interface LoadBalance {

    /**
     * select proxy url
     *
     * @param uri
     * @param targets
     * @return proxyUrl
     */
    String choose(List<String> targets, URI uri);
}
