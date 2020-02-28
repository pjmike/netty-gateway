package com.pjmike.filter;



/**
 * @author: pjmike
 * @create: 2019/11/28
 */
public abstract class AbstractFilter implements Filter,Comparable<AbstractFilter>{
    /**
     * 定义过滤器类型：pre、route、post、error
     * @return
     */
    abstract public String filterType();

    /**
     * 过滤器顺序
     *
     * @return
     */
    abstract public int filterOrder();

    @Override
    public int compareTo(AbstractFilter filter) {
        return Integer.compare(this.filterOrder(), filter.filterOrder());
    }
}