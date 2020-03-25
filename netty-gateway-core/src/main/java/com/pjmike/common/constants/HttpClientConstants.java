package com.pjmike.common.constants;

/**
 * <p>
 *  定义HttpClient所需的常量
 * </p>
 * @author: pjmike
 * @create: 2019/12/05
 */
public class HttpClientConstants {
    public static final String ENCODING = "UTF-8";
    /**
     * 建立连接时间
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 6000;
    /**
     * 等待数据的超时时间
     */
    public static final int DEFAULT_SOCKET_TIMEOUT = 6000;
    /**
     * 从连接池中获取连接的超时时间
     */
    public static final int DEFAULT_TIMEOUT = 6000;

    /**
     * 最大支持的连接数
     */
    public static final int DEFAULT_MAX_TOTAL = 512;
    /**
     * 针对某个域名的最大连接数
     */
    public static final int DEFAULT_MAX_PER_ROUTE = 64;
}
