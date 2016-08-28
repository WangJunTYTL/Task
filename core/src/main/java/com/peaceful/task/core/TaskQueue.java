package com.peaceful.task.core;

/**
 * 用于存放任务的队列服务
 *
 * @author WangJun
 * @version 1.0 16/3/29
 */
public interface TaskQueue {

    /**
     * 写入任务
     *
     * @param name   任务所在队列的名称
     * @param object 任务调用描述对象
     * @return true 表示写入成功 否则 return false
     */
    boolean push(String name, String object);

    /**
     * 从队列中取出一条任务
     *
     * @param name 任务所在队列的名称
     * @return 加入存在任务返回任务对象，否则返回null
     */
    String pop(String name);

    /**
     * 获取指定任务队列中含有任务的个数
     *
     * @param name 任务队列的名称
     * @return
     */
    long size(String name);
}
