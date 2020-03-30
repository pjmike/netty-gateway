package com.pjmike.filter.pre;

import com.pjmike.commons.context.ChannelContext;
import com.pjmike.filter.GlobalFilter;
import com.pjmike.route.Route;
import io.netty.channel.Channel;


/**
 * @description:
 * @author: pjmike
 * @create: 2020/03/16
 */
public class PreFilter extends GlobalFilter {
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
        String protocol = route.getUri().getScheme();
        //set rpc type
        ChannelContext.setRpcType(channel, protocol);
    }
}
