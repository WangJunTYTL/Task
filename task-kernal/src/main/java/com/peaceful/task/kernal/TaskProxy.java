package com.peaceful.task.kernal;

/**
 * Task任务的提交代理
 *
 * @author WangJun
 * @version 1.0 16/3/29
 */
public interface TaskProxy {

    /**
     * 获取代理类实例
     *
     * @param zclass
     * @param <T>
     * @return
     */
    <T> T getProxyInstance(Class<T> zclass);

}
