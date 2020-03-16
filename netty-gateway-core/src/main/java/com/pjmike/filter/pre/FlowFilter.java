package com.pjmike.filter.pre;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pjmike.context.ChannelContextUtil;
import com.pjmike.enums.RpcTypeEnum;
import com.pjmike.exception.GatewayException;
import com.pjmike.filter.GlobalFilter;
import com.pjmike.filter.handle.FilterWebHandler;
import com.pjmike.filter.route.DubboExecutor;
import com.pjmike.route.Route;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 按照协议进行分流
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
        String rpcType = ChannelContextUtil.getRpcType(channel);
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
