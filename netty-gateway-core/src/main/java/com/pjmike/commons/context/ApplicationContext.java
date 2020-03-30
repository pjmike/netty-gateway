package com.pjmike.commons.context;

import com.pjmike.execute.GatewayExecutor;

import com.pjmike.filter.handle.FilterWebHandler;
import com.pjmike.route.*;
import com.pjmike.route.discovery.DiscoverClientFactory;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/01/22
 */
@Data
public class ApplicationContext {
    private GatewayExecutor gatewayExecutor;
    private CompositeRouteLocator compositeRouteLocator;
    private Map<String, String> tokenMap = new ConcurrentHashMap<>();

    public void initializeBean() {
        this.compositeRouteLocator = initRouteLocator();
        this.gatewayExecutor = new GatewayExecutor(this.compositeRouteLocator, FilterWebHandler.getInstance());
        setUrlTokenMap();
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
        routeLocators.add(new DiscoveryClientRouteLocator(DiscoverClientFactory.getDiscoverClient("")));
        routeLocators.add(new AnnotationRouteLocator());
        CompositeRouteLocator compositeRouteLocator = new CompositeRouteLocator(routeLocators);
        return compositeRouteLocator;
    }

    private void setUrlTokenMap() {
        //需要鉴权的URL以及提前设置好的Token
        put("/login", "abcd_1234_user");
    }

    private void put(String key, String value) {
        this.tokenMap.put(key, value);
    }
}
