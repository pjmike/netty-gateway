package com.pjmike.route;

import com.pjmike.filter.GatewayFilter;
import io.netty.channel.Channel;
import lombok.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * @description: 路由信息
 * @author: pjmike
 * @create: 2019/11/28
 */
@Getter
@Setter
public class Route {
    /**
     * 路由id
     */
    private  String id;
    /**
     * 路由向的URI
     */
    private  URI uri;
    /**
     * 过滤器数组
     */
    private  List<GatewayFilter> gatewayFilters;
    /**
     * 断言
     */
    private  Predicate<Channel> predicate;

    public Route() {
    }

    public Route(String id, URI uri) {
        this(id, uri, null);
    }

    public Route(String id, URI uri, Predicate<Channel> predicate) {
        this(id, uri, new ArrayList<>(),predicate);
    }

    public Route(String id, URI uri, List<GatewayFilter> gatewayFilters, Predicate<Channel> predicate) {
        this.id = id;
        this.uri = uri;
        this.gatewayFilters = gatewayFilters;
        this.predicate = predicate;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String id;
        private URI uri;
        private Predicate<Channel> predicate;
        private List<GatewayFilter> gatewayFilters = new ArrayList<>();
        public Builder(){}

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = URI.create(uri);
            return this;
        }

        public Builder predicate(Predicate<Channel> predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder gatewayFilters(List<GatewayFilter> gatewayFilters) {
            this.gatewayFilters = gatewayFilters;
            return this;
        }

        public Builder add(GatewayFilter gatewayFilter) {
            this.gatewayFilters.add(gatewayFilter);
            return this;
        }

        public Builder addAll(Collection<GatewayFilter> gatewayFilters) {
            this.gatewayFilters.addAll(gatewayFilters);
            return this;
        }

        public Route build() {
            return new Route(this.id, this.uri, this.gatewayFilters, this.predicate);
        }
    }
    @Override
    public String toString() {
        return "Route{" +
                "id='" + id + '\'' +
                ", uri=" + uri +
                ", gatewayFilters=" + gatewayFilters +
                ", predicate=" + predicate +
                '}';
    }
}
