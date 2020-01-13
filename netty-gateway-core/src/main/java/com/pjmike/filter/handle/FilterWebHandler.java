package com.pjmike.filter.handle;

import com.pjmike.attribute.Attributes;
import com.pjmike.filter.GatewayFilter;
import com.pjmike.filter.GatewayFilterChain;
import com.pjmike.filter.handle.WebHandler;
import com.pjmike.route.Route;
import io.netty.channel.Channel;

import java.util.List;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/28
 */
public class FilterWebHandler implements WebHandler {
    private static final FilterWebHandler INSTANCE = new FilterWebHandler();

    public static FilterWebHandler getInstance() {
        return INSTANCE;
    }
    @Override
    public void handle(Channel channel) throws Exception {
        Route route = channel.attr(Attributes.GATEWAY_ROUTE_ATTR).get();
        List<GatewayFilter> gatewayFilters = route.getGatewayFilters();

        GatewayFilterChain filterChain = new GatewayFilterChain(gatewayFilters);
        //执行过滤逻辑
        filterChain.filter(channel);
    }
}
