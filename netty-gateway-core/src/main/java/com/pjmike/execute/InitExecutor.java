package com.pjmike.execute;

import com.pjmike.filter.handle.FilterWebHandler;
import com.pjmike.route.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/16
 */
public class InitExecutor {
    private static AtomicBoolean init = new AtomicBoolean(false);
    public static Map<String, Object> routeLocatorMap;

    public static void init() {
        if (!init.compareAndSet(false, true)) {
            return;
        }
        routeLocatorMap = new LinkedHashMap<>();
        //初始化RouteLocator
        CompositeRouteLocator compositeRouteLocator = initRouteLocator();
        //init GatewayExecutor
        GatewayExecutor gatewayExecutor = new GatewayExecutor(compositeRouteLocator,FilterWebHandler.getInstance());
        routeLocatorMap.put(GatewayExecutor.class.getName(), gatewayExecutor);
    }

    public static CompositeRouteLocator initRouteLocator() {
        List<RouteLocator> routeLocators = new ArrayList<>();
        routeLocators.add(new PropertiesRouteLocator());
        routeLocators.add(new DiscoveryClientRouteLocator());
        routeLocators.add(new AnnotationRouteLocator());
        CompositeRouteLocator compositeRouteLocator = new CompositeRouteLocator(routeLocators);
        routeLocatorMap.put(CompositeRouteLocator.class.getName(), compositeRouteLocator);
        return compositeRouteLocator;
    }
}
