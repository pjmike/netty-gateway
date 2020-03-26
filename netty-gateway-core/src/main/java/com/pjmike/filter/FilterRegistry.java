package com.pjmike.filter;


import com.pjmike.common.lb.LoadBalanceFactory;
import com.pjmike.filter.error.NettyErrorFilter;
import com.pjmike.filter.post.NettyWriteResponseFilter;
import com.pjmike.filter.pre.*;
import com.pjmike.filter.route.NettyRoutingFilter;
import lombok.extern.slf4j.Slf4j;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/18
 */
@Slf4j
public class FilterRegistry {
    private static FilterRegistry INSTANCE = new FilterRegistry();
    /**
     * 注册表
     */
    private Map<String, GlobalFilter> filterMap = new ConcurrentHashMap<>();

    private FilterRegistry() {
        initFilters();
    }

    private void initFilters() {
        put(RateLimitFilter.class.getName(), new RateLimitFilter());
        put(PreFilter.class.getName(), new PreFilter());
        put(FlowFilter.class.getName(), new FlowFilter());
        put(LoadbalancerFilter.class.getName(), new LoadbalancerFilter(LoadBalanceFactory.getLoadBalance()));
        put(AuthTokenFilter.class.getName(), new AuthTokenFilter());
        put(AntiSpiderFilter.class.getName(), new AntiSpiderFilter());
        put(NettyRoutingFilter.class.getName(), new NettyRoutingFilter());
        put(NettyErrorFilter.class.getName(), new NettyErrorFilter());
        put(NettyWriteResponseFilter.class.getName(), new NettyWriteResponseFilter());
    }

    public void put(String name, GlobalFilter filter) {
        this.filterMap.put(name, filter);
    }

    public int size() {
        return this.filterMap.values().size();
    }

    public Collection<GlobalFilter> getAllFilters() {
        return this.filterMap.values();
    }

    public static FilterRegistry getInstance() {
        return INSTANCE;
    }
}
