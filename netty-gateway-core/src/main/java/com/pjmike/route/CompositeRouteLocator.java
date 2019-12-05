package com.pjmike.route;

import io.netty.channel.Channel;

import java.util.List;

/**
 * @description: 将所有路由定位器实现进行组合
 * @author: pjmike
 * @create: 2019/12/05
 */
public class CompositeRouteLocator extends AbstractRouteLocator{
    @Override
    public List<Route> getRoutes() {
        //TODO
        return null;
    }
}
