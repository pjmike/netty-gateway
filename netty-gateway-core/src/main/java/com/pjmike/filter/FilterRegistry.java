package com.pjmike.filter;


import com.pjmike.filter.post.NettyErrorFilter;
import com.pjmike.filter.post.NettyWriteResponseFilter;
import com.pjmike.filter.pre.AntiSpiderFilter;
import com.pjmike.filter.pre.AuthTokenFilter;
import com.pjmike.filter.pre.RateLimitFilter;
import com.pjmike.filter.route.NettyRoutingFilter;
import com.pjmike.filter.pre.SentinelFilter;
import lombok.extern.slf4j.Slf4j;


import java.util.*;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/18
 */
@Slf4j
public class FilterRegistry {
    private static FilterRegistry INSTANCE = new FilterRegistry();
    private List<GlobalFilter> filters = new ArrayList<>();

    private FilterRegistry() {
        //预先加载Filters
        add(new AntiSpiderFilter())
                .add(new AuthTokenFilter())
                .add(new SentinelFilter())
                .add(new RateLimitFilter())
                .add(new NettyRoutingFilter())
                .add(new NettyWriteResponseFilter())
                .add(new NettyErrorFilter());

    }

    public FilterRegistry add(GlobalFilter filter) {
        this.filters.add(filter);
        return this;
    }


    public int size() {
        return this.filters.size();
    }

    public List<GlobalFilter> getAllFilters() {
        return this.filters;
    }

    public static FilterRegistry getInstance() {
        return INSTANCE;
    }

}
