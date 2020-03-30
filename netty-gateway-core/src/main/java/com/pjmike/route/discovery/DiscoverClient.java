package com.pjmike.route.discovery;

import org.apache.dubbo.common.extension.SPI;

import java.util.List;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/26
 */
@SPI(ZookeeperDiscoveryClient.NAME)
public interface DiscoverClient {

    List<String> getServices();
}
