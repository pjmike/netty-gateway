package com.pjmike.route;

import io.netty.channel.Channel;

import java.net.URISyntaxException;
import java.util.List;

/**
 * @description: 路由定位器
 * @author: pjmike
 * @create: 2019/11/28
 */
public interface RouteLocator {
    /**
     * 获取路由列表
     *
     * @return
     * @throws Exception
     */
    List<Route> getRoutes() throws Exception;

    /**
     * 查找符合路由条件的 Route
     *
     * @param channel
     * @return
     * @throws Exception
     */
    Route lookupRoute(Channel channel) throws Exception;
}
