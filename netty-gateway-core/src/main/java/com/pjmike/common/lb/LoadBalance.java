package com.pjmike.common.lb;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/25
 */
public interface LoadBalance {

    /**
     * select proxy url
     *
     * @param url
     * @return proxyUrl
     */
    String choose(String url);
}
