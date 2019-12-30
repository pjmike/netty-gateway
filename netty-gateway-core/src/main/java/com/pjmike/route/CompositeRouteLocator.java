package com.pjmike.route;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: 组合多种 RouteLocator 的实现类，提供了统一访问路由的入口
 * @author: pjmike
 * @create: 2019/12/05
 */
@Slf4j
public class CompositeRouteLocator extends AbstractRouteLocator {
    /**
     * RouteLocator集合
     */
    private List<RouteLocator> delegates;

    public CompositeRouteLocator(List<RouteLocator> delegates) {
        this.delegates = delegates;
    }
    @Override
    public List<Route> getRoutes() throws Exception {
        List<Route> routes = new ArrayList<>();
        for (RouteLocator routeLocator : delegates) {
            if (CollectionUtil.isNotEmpty(routeLocator.getRoutes())) {
                routes.addAll(routeLocator.getRoutes());
            }
        }
        return routes;
    }
}
