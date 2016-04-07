package com.peaceful.task.context;

/**
 * 用于存放任务的队列服务
 *
 * @author WangJun
 * @version 1.0 16/3/29
 */
public interface TaskQueue<T> {

    boolean push(String name, T object);

    T pop(String name);

    long size(String key);
}
