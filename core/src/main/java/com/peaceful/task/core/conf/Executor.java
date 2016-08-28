package com.peaceful.task.core.conf;

/**
 * 任务执行器
 *
 * Created by wangjun on 16/1/12.
 */
public class Executor {

    // 执行器的名称
    public String name;
    // 执行器的完整类名称
    public String implementation;
    // 执行器对应的Class对象
    public java.lang.Class<?> Class;
}
