package com.pjmike.filter.pre;

import com.pjmike.common.context.ChannelContext;
import com.pjmike.filter.GlobalFilter;
import com.pjmike.common.lb.LoadBalance;
import com.pjmike.route.Route;
import io.netty.channel.Channel;

import java.net.URI;

/**
 * @description: 负载均衡
 * @author: pjmike
 * @create: 2020/03/25
 */
public class LoadbalancerFilter extends GlobalFilter {
    private final LoadBalance loadBalance;

    public LoadbalancerFilter(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return -100;
    }

    @Override
    public void filter(Channel channel) throws Exception {
        Route route = ChannelContext.getRoute(channel);
        if (!route.getUri().getScheme().equals("lb")) {
            return;
        }
        String proxyUrl = loadBalance.choose(route.getUri().getHost());
        route.setUri(URI.create(proxyUrl));
    }
}
