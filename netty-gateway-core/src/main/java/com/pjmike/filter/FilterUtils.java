package com.pjmike.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.ClassScaner;
import com.pjmike.annotation.Order;
import com.pjmike.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: pjmike
 * @create: 2019/12/18
 */
@Slf4j
public class FilterUtils {
    public static final FilterUtils INSTANCE = new FilterUtils();

    public FilterUtils getInstance() {
        return INSTANCE;
    }
    public List<GatewayFilter> getGlobalFilters() {
        List<GatewayFilter> gatewayFilters = new ArrayList<>();
        try {
            Set<Class<?>> classes = ClassScaner.scanPackageBySuper(CommonConstants.FILTER_SCAN_PACKAGE, GatewayFilter.class);
            if (CollectionUtil.isNotEmpty(classes)) {
                List<FilterOrderWrapper> orderWrappers = new ArrayList<>();
                for (Class<?> cls : classes) {
                    if (!cls.isInterface() && GatewayFilter.class.isAssignableFrom(cls)) {
                        Constructor<?> constructor = cls.getDeclaredConstructor();
                        //取消安全检查
                        constructor.setAccessible(true);
                        GatewayFilter filter = (GatewayFilter) constructor.newInstance();
                        int order = resolveOrderAnnotation(filter);
                        orderWrappers.add(new FilterOrderWrapper(filter, order));
                    }
                }
                //排序
                orderWrappers = sort(orderWrappers);
                //转换
                gatewayFilters = orderWrappers.stream().map(FilterOrderWrapper::getFilter).collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.warn("load filters failed, ", e);
            e.printStackTrace();
        }
        return gatewayFilters;
    }

    private class FilterOrderWrapper {
        private GatewayFilter filter;
        private int order;

        public FilterOrderWrapper(GatewayFilter filter, int order) {
            this.filter = filter;
            this.order = order;
        }

        public GatewayFilter getFilter() {
            return filter;
        }

        public int getOrder() {
            return order;
        }
    }

    private int resolveOrderAnnotation(GatewayFilter filter) {
        if (!filter.getClass().isAnnotationPresent(Order.class)) {
            return Order.LOWEST_PRECEDENCE;
        } else {
            return filter.getClass().getAnnotation(Order.class).value();
        }
    }

    private List<FilterOrderWrapper> sort(List<FilterOrderWrapper> filterOrderWrappers) {
        return filterOrderWrappers.stream()
                .sorted(Comparator.comparing(FilterOrderWrapper::getOrder)).collect(Collectors.toList());
    }
}
