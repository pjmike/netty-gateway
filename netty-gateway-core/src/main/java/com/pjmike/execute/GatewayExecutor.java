package com.pjmike.execute;

import com.pjmike.context.ChannelContextUtil;
import com.pjmike.exception.GatewayException;
import com.pjmike.filter.handle.WebHandler;
import com.pjmike.route.Route;
import com.pjmike.route.RouteLocator;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpResponseStatus;

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
        Channel channel = (Channel)args[0];
        //find route
        Route route = this.routeLocator.lookupRoute(channel);
        if (Objects.isNull(route)) {
            throw new GatewayException(HttpResponseStatus.NOT_FOUND,"no available route");
        }
        //bind channel and route
        ChannelContextUtil.setRoute(channel,route);
        // execute filter
        this.webHandler.handle(channel);
        return null;
    }
}
