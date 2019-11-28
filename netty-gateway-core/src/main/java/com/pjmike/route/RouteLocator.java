package com.pjmike.route;

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
     */
    List<Route> getRoutes();
}
