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
     * @throws Exception
     */
    T execute(Object... args) throws Exception;

}
