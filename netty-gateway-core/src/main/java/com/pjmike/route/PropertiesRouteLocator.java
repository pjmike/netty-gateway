package com.pjmike.route;

import com.pjmike.constants.CommonConstants;
import com.pjmike.handler.predicate.PredicateUtils;
import com.pjmike.utils.PropertiesUtil;
import io.netty.channel.Channel;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/05
 */
public class PropertiesRouteLocator extends AbstractRouteLocator{
    private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance(CommonConstants.PROPERTIES_PATH);
    private static String routeId;
    private static String routeUri;
    private static String text;
    static {
        routeId = propertiesUtil.getString(CommonConstants.PROPERTIES_ROUTE_ID);
        routeUri = propertiesUtil.getString(CommonConstants.PROPERTIES_ROUTE_URI);
        text = propertiesUtil.getString(CommonConstants.PROPERTIES_ROUTE_PREDICATE_PATH);
    }
    @Override
    public List<Route> getRoutes() throws Exception {
        Predicate<Channel> predicate = PredicateUtils.path(text);
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(routeId, new URI(routeUri), predicate));
        return routes;
    }
}
