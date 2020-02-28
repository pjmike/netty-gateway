package com.pjmike.context;

import com.pjmike.execute.GatewayExecutor;
import com.pjmike.filter.FilterRegistry;
import com.pjmike.filter.AbstractFilter;
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

    public void initializeBean() {
        this.compositeRouteLocator = initRouteLocator();
        this.gatewayExecutor = new GatewayExecutor(this.compositeRouteLocator, FilterWebHandler.getInstance());
    }

    private static final ApplicationContext INSTANCE = new ApplicationContext();

    public ApplicationContext() {
        initializeBean();
    }
    public static ApplicationContext getInstance() {
        return INSTANCE;
    }
    public CompositeRouteLocator initRouteLocator() {
        List<RouteLocator> routeLocators = new ArrayList<>();
        routeLocators.add(new PropertiesRouteLocator());
        routeLocators.add(new DiscoveryClientRouteLocator());
        routeLocators.add(new AnnotationRouteLocator());
        CompositeRouteLocator compositeRouteLocator = new CompositeRouteLocator(routeLocators);
        return compositeRouteLocator;
    }
}
