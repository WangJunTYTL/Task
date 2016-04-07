package com.peaceful.task.context;

/**
 * Task任务的提交代理
 *
 * @author WangJun
 * @version 1.0 16/3/29
 */
public interface TaskClientProxy {

    /**
     * 获取代理类实例
     *
     * @param zclass
     * @param <T>
     * @return
     */
    <T> T enhancer(Class<T> zclass);

}
