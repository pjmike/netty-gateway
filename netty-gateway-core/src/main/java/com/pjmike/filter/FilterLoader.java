package com.pjmike.filter;

import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/02/25
 */
public class FilterLoader {
    private List<GatewayFilter> gatewayFilters;

    public FilterLoader() {
        this.gatewayFilters = loadFilters();
    }

    public List<GatewayFilter> getFiltersByType(String filterType) {
        List<GatewayFilter> list = new ArrayList<>();
        for (GatewayFilter filter : this.gatewayFilters) {
            if (filter.filterType().equals(filterType)) {
                list.add(filter);
            }
        }
        Collections.sort(list);
        return list;
    }

    private List<GatewayFilter> loadFilters() {
        List<GlobalFilter> filters = FilterRegistry.getInstance().getAllFilters();
        return filters.stream()
                .map(GatewayFilterAdapter::new).collect(Collectors.toList());
    }

    public void addFilters(List<GatewayFilter> gatewayFilters) {
        this.gatewayFilters.addAll(gatewayFilters);
    }
}
