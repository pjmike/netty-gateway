package com.pjmike.route;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 将所有路由定位器实现进行组合
 * @author: pjmike
 * @create: 2019/12/05
 */
public class CompositeRouteLocator extends AbstractRouteLocator{
    /**
     * RouteLocator集合
     */
    private List<RouteLocator> delegates;

    public CompositeRouteLocator(List<RouteLocator> delegates) {
        this.delegates = delegates;
    }

    @Override
    public List<Route> getRoutes() throws Exception{
        List<Route> routes = new ArrayList<>();
        delegates.forEach(routeLocator -> {
            try {
                routes.addAll(routeLocator.getRoutes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return routes;
    }
}
