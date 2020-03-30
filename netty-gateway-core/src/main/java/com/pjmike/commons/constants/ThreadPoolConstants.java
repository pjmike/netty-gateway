package com.pjmike.commons.constants;

/**
 * 线程池常量参数
 *
 * @author: pjmike
 * @create: 2019/12/06
 */
public class ThreadPoolConstants {

    /**
     * 业务线程池核心线程数
     */
    public static final int EVENT_EXECUTOR_POOL_CORE_SIZE =10;

    /**
     * 业务线程池最大线程数
     */
    public static final int EVENT_EXECUTOR_POOL_MAX_SIZE = 20;

    /**
     * 业务线程池临时线程存活时间，单位：s
     */
    public static final int EVENT_EXECUTOR_POOL_KEEP_ALIVE_SECONDS = 10;

    /**
     * 业务线程池阻塞队列
     */
    public static final int EVENT_EXECUTOR_POOL_BLOCKING_QUEUE_SIZE = 10;
}
