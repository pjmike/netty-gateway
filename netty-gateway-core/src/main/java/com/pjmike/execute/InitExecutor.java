package com.pjmike.execute;


import com.pjmike.constants.CommonConstants;
import com.pjmike.filter.FilterRegistry;
import com.pjmike.filter.GatewayFilter;
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
    public static Map<String, Object> gatewayConfig;

    public static void init() {
        if (!init.compareAndSet(false, true)) {
            return;
        }
        gatewayConfig = new LinkedHashMap<>();
        //初始化RouteLocator
        CompositeRouteLocator compositeRouteLocator = initRouteLocator();
        //加载Filters
        List<GatewayFilter> filters = getAllGlobalFilters();
        //init GatewayExecutor
        GatewayExecutor gatewayExecutor = new GatewayExecutor(compositeRouteLocator,FilterWebHandler.getInstance());

        gatewayConfig.put(CommonConstants.GATEWAY_EXECUTOR_NAME, gatewayExecutor);
        gatewayConfig.put(CommonConstants.GLOBAL_FILTER_NAME, filters);

    }

    public static CompositeRouteLocator initRouteLocator() {
        List<RouteLocator> routeLocators = new ArrayList<>();
        routeLocators.add(new PropertiesRouteLocator());
        routeLocators.add(new DiscoveryClientRouteLocator());
        routeLocators.add(new AnnotationRouteLocator());
        CompositeRouteLocator compositeRouteLocator = new CompositeRouteLocator(routeLocators);
        gatewayConfig.put(CompositeRouteLocator.class.getName(), compositeRouteLocator);
        return compositeRouteLocator;
    }

    public static List<GatewayFilter> getAllGlobalFilters() {
        return FilterRegistry.INSTANCE.loadGlobalFilters();
    }


}
