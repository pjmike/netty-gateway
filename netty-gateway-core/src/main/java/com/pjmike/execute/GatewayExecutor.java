package com.pjmike.execute;

import com.pjmike.common.context.ChannelContext;
import com.pjmike.filter.handle.WebHandler;
import com.pjmike.route.Route;
import com.pjmike.route.RouteLocator;
import io.netty.channel.Channel;

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
        if (args == null || args.length <= 0) {
            return null;
        }
        Channel channel = (Channel)args[0];
        //find route
        Route route = this.routeLocator.lookupRoute(channel);

        //bind channel and route
        ChannelContext.setRoute(channel,route);
        // execute filter
        this.webHandler.handle(channel);
        return null;
    }
}
