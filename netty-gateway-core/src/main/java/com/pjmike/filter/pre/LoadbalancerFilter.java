package com.pjmike.filter.pre;

import com.pjmike.common.constants.CommonConstants;
import com.pjmike.common.context.ChannelContext;
import com.pjmike.filter.GlobalFilter;
import com.pjmike.common.lb.LoadBalance;
import com.pjmike.route.Route;
import com.pjmike.utils.PropertiesUtil;
import io.netty.channel.Channel;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

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
        List<String> targetServer = getTargetServer(route.getUri());
        String targetUrl = loadBalance.choose(targetServer, route.getUri());
        route.setUri(URI.create(targetUrl));
    }

    private List<String> getTargetServer(URI uri) {
        String services = uri.toASCIIString().substring(5);
        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance(CommonConstants.PROPERTIES_PATH);
        return Arrays.asList(propertiesUtil.getStringArray(services));
    }

}
