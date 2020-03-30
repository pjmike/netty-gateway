package com.pjmike.route;

import cn.hutool.core.util.ClassLoaderUtil;
import com.pjmike.commons.annotation.Bean;
import com.pjmike.route.config.RouteConfig;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 通过在RouteConfig通过注解的形式定义Route
 * @author: pjmike
 * @create: 2019/12/18
 */
@Slf4j
public class AnnotationRouteLocator extends AbstractRouteLocator {
    @Override
    public List<Route> getRoutes() throws Exception {
        return loadRouteWithAnnotation();
    }
    /**
     * 加载用户通过编码方式自定义的Route
     *
     * @return
     */
    private List<Route> loadRouteWithAnnotation() {
        List<Route> routes = new ArrayList<>();
        try {
            Class<?> aClass = ClassLoaderUtil.loadClass("com.pjmike.route.config.RouteConfig");
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            RouteConfig routeConfig = (RouteConfig) constructor.newInstance();
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Bean.class)) {
                    Route route = (Route)method.invoke(routeConfig);
                    routes.add(route);
                }
            }
        } catch (Exception e) {
            log.warn("load user-defined route failed, {}", e);
            e.printStackTrace();
        }
        return routes;
    }
}
