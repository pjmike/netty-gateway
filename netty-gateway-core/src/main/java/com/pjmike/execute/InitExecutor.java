package com.pjmike.execute;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.pjmike.constants.CommonConstants;
import com.pjmike.constants.SentinelConstants;
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

    public static void initDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule();
        rule.setResource(SentinelConstants.KEY);
        // set limit exception ratio to 0.1
        rule.setCount(0.1);
        rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
        rule.setTimeWindow(10);
        rule.setMinRequestAmount(20);
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }
}
