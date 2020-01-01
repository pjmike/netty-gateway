package com.pjmike.execute;

import com.pjmike.constants.CommonConstants;
import com.pjmike.context.RequestContextUtil;
import com.pjmike.filter.GatewayFilter;
import com.pjmike.filter.handle.WebHandler;
import com.pjmike.route.Route;
import com.pjmike.route.RouteLocator;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/26
 */
public class GatewayExecutor extends AbstractExecutor<Void> {
    private RouteLocator routeLocator;
    private WebHandler webHandler;
    public GatewayExecutor(RouteLocator routeLocator, WebHandler webHandler) {
        this.routeLocator = routeLocator;
        this.webHandler = webHandler;
    }
    @Override
    protected Void doExecute(Object... args) throws Exception {
        final List<GatewayFilter> filterList = (List<GatewayFilter>) InitExecutor.gatewayConfig.get(CommonConstants.GLOBAL_FILTER_NAME);
        Channel channel = (Channel)args[0];
        //find route
        Route route = routeLocator.lookupRoute(channel);
        if (Objects.isNull(route)) {
            throw new RuntimeException("no available route");
        }
        //set filter
        route.setGatewayFilters(filterList);
        //bind channel and route
        RequestContextUtil.setRoute(channel,route);
        // execute filter chain
        webHandler.handle(channel);
        return null;
    }
}
