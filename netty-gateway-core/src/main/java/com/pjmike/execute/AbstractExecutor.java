package com.pjmike.execute;

/**
 * @author: pjmike
 * @create: 2019/12/06
 */
public abstract class AbstractExecutor<T> implements Executor<T> {

    @Override
    public T execute(Object... args) throws Exception {
        return doExecute(args);
    }

    /**
     * 执行具体的任务
     *
     * @param args
     * @return
     * @throws Exception
     */
    protected abstract T doExecute(Object... args) throws Exception;

}
