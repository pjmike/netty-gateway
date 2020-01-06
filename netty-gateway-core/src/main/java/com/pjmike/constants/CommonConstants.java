package com.pjmike.constants;

/**
 * @description: 公共常量
 * @author: pjmike
 * @create: 2019/12/08
 */
public class CommonConstants {
    /** ---------------------------配置文件--------------------------------**/
    public static final String PROPERTIES_PATH = "/netty-gateway.properties";

    public static final String PROPERTIES_ROUTE_URI = "netty.gateway.routes.uri";

    public static final String PROPERTIES_ROUTE_ID = "netty.gateway.routes.id";

    public static final String PROPERTIES_ROUTE_PREDICATE_PATH = "netty.gateway.routes.predicates.path";

    /** ---------------------------文件路径------------------------------**/

    public static final String FILTER_SCAN_PACKAGE = "com.pjmike.filter";


    /** ---------------------------HTTP & HTTPS------------------------------**/
    public static final String HTTP = "HTTP";

    public static final String HTTPS = "HTTPS";

    /**
     * ---------------------------GatewayConfig的Key----------------------
     **/

    public static final String GATEWAY_EXECUTOR_NAME = "gateway_executor";

    public static final String GLOBAL_FILTER_NAME = "global_filter_name";


    /**
     * ---------------------------公共锁------------------------------
     **/
    public static final Object OBJECT = new Object();
    /**
     * 令牌桶算法：Guava的RateLimiter每秒发出的令牌数
     */
    public static final Integer PERMIT_SPER_SECOND = 10;
}
