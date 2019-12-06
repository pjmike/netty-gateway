package com.pjmike.execute;


import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;



/**
 * @author: pjmike
 * @create: 2019/12/06
 */
public interface Executor<T> {
    /**
     * 执行任务
     *
     * @param args
     * @return
     */
    T execute(Object... args) throws Exception;

    /**
     * 异步执行任务获取 Future
     *
     * @param args 请求对象
     * @return 异步结果
     */
    Future<T> asyncExecute(Promise<T> promise, Object... args) throws Exception;
}
