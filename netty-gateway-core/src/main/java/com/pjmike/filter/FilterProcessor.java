package com.pjmike.filter;

import com.pjmike.exception.GatewayException;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @description:
 * @author: pjmike
 * @create: 2020/02/25
 */
@Slf4j
public class FilterProcessor {
    private FilterLoader filterLoader = new FilterLoader();

    /**
     *  runs all "pre" filters
     *
     * @param channel
     * @throws GatewayException
     */
    public void preRoute(Channel channel) throws GatewayException {
        try {
            runFilters("pre", channel);
        } catch (GatewayException e) {
            throw e;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new GatewayException(throwable,HttpResponseStatus.INTERNAL_SERVER_ERROR, "UNCAUGHT_EXCEPTION_IN_PRE_FILTER_" + throwable.getClass().getName());
        }
    }
    public void postRoute(Channel channel) throws GatewayException {
        try {
            runFilters("post",channel);
        } catch (GatewayException e) {
            throw e;
        } catch (Throwable e) {
            throw new GatewayException(e,HttpResponseStatus.INTERNAL_SERVER_ERROR,"UNCAUGHT_EXCEPTION_IN_POST_FILTER_" + e.getClass().getName());
        }
    }

    /**
     * runs all "error" filters.
     */
    public void error(Channel channel) {
        try {
            runFilters("error",channel);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Runs all "route" filters.
     *
     */
    public void route(Channel channel) throws GatewayException {
        try {
            runFilters("route",channel);
        } catch (GatewayException e) {
            throw e;
        } catch (Throwable e) {
            throw new GatewayException(e,HttpResponseStatus.INTERNAL_SERVER_ERROR, "UNCAUGHT_EXCEPTION_IN_ROUTE_FILTER_" + e.getClass().getName());        }
    }

    public void runFilters(String filterType,Channel channel) throws Throwable {
        List<GatewayFilter> gatewayFilters = this.filterLoader.getFiltersByType(filterType);

        if (gatewayFilters != null) {
            for (GatewayFilter filter : gatewayFilters) {
                filter.filter(channel);
            }
        }
    }


    public void setFilterLoaderGatewayFilters(List<GatewayFilter> gatewayFilters) {
        if (!CollectionUtils.isEmpty(gatewayFilters)) {
            this.filterLoader.addFilters(gatewayFilters);
        }
    }
}
