package com.pjmike.route.discovery;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/26
 */
public class ZookeeperDiscoveryClient implements DiscoverClient {
    public static final String NAME = "zookeeper";
    private CuratorFramework client;

    public ZookeeperDiscoveryClient() {
        this.client = CuratorFrameworkFactory
                .builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        this.client.start();
    }
    @Override
    public List<String> getServices() {
        //TODO
        return null;
    }
}
