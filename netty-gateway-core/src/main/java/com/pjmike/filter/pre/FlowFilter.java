package com.pjmike.filter.pre;

import com.pjmike.common.context.ChannelContext;
import com.pjmike.enums.RpcTypeEnum;
import com.pjmike.exception.GatewayException;
import com.pjmike.filter.GlobalFilter;
import com.pjmike.filter.handle.FilterWebHandler;
import com.pjmike.filter.route.DubboExecutor;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;


/**
 * @description: 按照RPC协议进行分流
 * @author: pjmike
 * @create: 2020/03/16
 */
@Slf4j
public class FlowFilter extends GlobalFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public void filter(Channel channel) throws Exception {
        String rpcType = ChannelContext.getRpcType(channel);
        try {
            if (StringUtils.isNotBlank(rpcType) && RpcTypeEnum.DUBBO.getName().equals(rpcType)) {
                DubboExecutor.getInstance().execute(channel);
            } else {
                FilterWebHandler.getInstance().routeAction(channel);
            }
        } catch (GatewayException e) {
            log.error(e.getMessage(), e);
            FilterWebHandler.getInstance().errorAction(channel, e);
        }
    }
}
