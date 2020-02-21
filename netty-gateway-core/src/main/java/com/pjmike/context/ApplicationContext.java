package com.pjmike.context;

import com.google.common.collect.Maps;
import com.pjmike.execute.GatewayExecutor;
import com.pjmike.filter.FilterRegistry;
import com.pjmike.filter.GatewayFilter;
import com.pjmike.filter.handle.FilterWebHandler;
import com.pjmike.route.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/01/22
 */
@Data
public class ApplicationContext {
    private GatewayExecutor gatewayExecutor;
    private CompositeRouteLocator compositeRouteLocator;
    private List<GatewayFilter> gatewayFilters;

    public void initializeBean() {
        compositeRouteLocator = initRouteLocator();
        gatewayFilters = getAllGlobalFilters();
        gatewayExecutor = new GatewayExecutor(compositeRouteLocator, FilterWebHandler.getInstance(),gatewayFilters);
    }

    private static final ApplicationContext instance = new ApplicationContext();

    public ApplicationContext() {
        initializeBean();
    }
    public static ApplicationContext getInstance() {
        return instance;
    }
    public CompositeRouteLocator initRouteLocator() {
        List<RouteLocator> routeLocators = new ArrayList<>();
        routeLocators.add(new PropertiesRouteLocator());
        routeLocators.add(new DiscoveryClientRouteLocator());
        routeLocators.add(new AnnotationRouteLocator());
        CompositeRouteLocator compositeRouteLocator = new CompositeRouteLocator(routeLocators);
        return compositeRouteLocator;
    }
    public List<GatewayFilter> getAllGlobalFilters() {
        return FilterRegistry.getInstance().loadGlobalFilters();
    }
}
