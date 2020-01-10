package com.pjmike.filter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.pjmike.annotation.Order;
import com.pjmike.http.NettyHttpResponseUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/01/10
 */
@Order(-1)
@Slf4j
public class SentinelGatewayFilter implements GatewayFilter{
    static {
        initDegradeRule();
    }
    public static final String GATEWAY_CONTEXT_ROUTE_PREFIX = "sentinel_gateway_context";
    public static final String TOTAL_URL_REQUEST = "total-url-request";

    @Override
    public void filter(Channel channel, GatewayFilterChain filterChain) throws Exception{
        Entry entry = null;
        try {
            ContextUtil.enter(GATEWAY_CONTEXT_ROUTE_PREFIX);
            entry = SphU.entry(TOTAL_URL_REQUEST);
            filterChain.filter(channel);
        } catch (Exception e) {
            fallbackHandler(channel);
        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    private void fallbackHandler(Channel channel) {
        channel.writeAndFlush(NettyHttpResponseUtil.buildFailResponse("熔断降级"))
                .addListener(ChannelFutureListener.CLOSE);
    }

    public static void initDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule();
        rule.setResource(TOTAL_URL_REQUEST);
        rule.setCount(10);
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        rule.setTimeWindow(10);
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }
}
