package com.pjmike.execute;

import com.pjmike.context.RequestContextUtil;
import com.pjmike.filter.handle.WebHandler;
import com.pjmike.route.Route;
import com.pjmike.route.RouteLocator;
import io.netty.channel.Channel;

import java.util.Objects;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/11/26
 */
public class GatewayExecutor extends AbstractExecutor {
    private final RouteLocator routeLocator;
    private final WebHandler webHandler;
    public GatewayExecutor(RouteLocator routeLocator, WebHandler webHandler) {
        this.routeLocator = routeLocator;
        this.webHandler = webHandler;
    }


    /**
     * TODO 将下游响应数据进行写回
     *
     * @param channel
     */
    protected void postResponse(Channel channel) {
        //TODO
    }

    @Override
    protected Object doExecute(Object... args) {
        Channel channel = (Channel)args[0];
        //第一步：找出所有的Routes
        //第二步：遍历所有的Routes，利用Predicate判断是否满足路由，找出符合条件的路由
        Route route = routeLocator.lookupRoute(channel);
        if (Objects.isNull(route)) {
        }
        //第三步: 将Route与Channel进行相应的绑定
        RequestContextUtil.setRoute(channel,route);
        //第四步：在该Channel的Route中，利用Route中的gatewayFilters进行过滤处理，需要经过pre+post
        webHandler.handle(channel);
        return null;
    }
}
