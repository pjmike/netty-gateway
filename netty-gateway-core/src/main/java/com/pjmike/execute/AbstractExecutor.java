package com.pjmike.execute;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.pjmike.constants.ThreadPoolConstants.*;
/**
 * @author: pjmike
 * @create: 2019/12/06
 */
public abstract class AbstractExecutor<T> implements Executor<T> {
    private java.util.concurrent.Executor eventExecutor;

    public AbstractExecutor() {
        this(null);
    }

    public AbstractExecutor(java.util.concurrent.Executor eventExecutor) {
        this.eventExecutor = eventExecutor == null ? ExecutorHolder.executor : eventExecutor;
    }

    @Override
    public T execute(Object... args) throws Exception {
        return doExecute(args);
    }

    /**
     * 执行具体的任务
     *
     * @param args
     * @return
     */
    protected abstract T doExecute(Object... args) throws Exception;

    @Override
    public Future<T> asyncExecute(Promise<T> promise,Object... args) {
        CompletableFuture.runAsync(() -> {
            T response = null;
            try {
                response = doExecute(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            promise.setSuccess(response);
        });
        return promise;
    }

    private static final class ExecutorHolder {
        private static java.util.concurrent.Executor executor = new ThreadPoolExecutor(
                EVENT_EXECUTOR_POOL_CORE_SIZE,
                EVENT_EXECUTOR_POOL_MAX_SIZE,
                EVENT_EXECUTOR_POOL_KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(EVENT_EXECUTOR_POOL_BLOCKING_QUEUE_SIZE),
                new ThreadFactoryBuilder().setNameFormat("event_thread_%d").build()
        );
    }
}
