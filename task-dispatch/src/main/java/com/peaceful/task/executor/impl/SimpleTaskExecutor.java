package com.peaceful.task.executor.impl;


import com.peaceful.task.context.TaskExecutor;

/**
 * Created by wangjun on 16/1/13.
 */
public class SimpleTaskExecutor implements TaskExecutor {

    @Override
    public void execute(Runnable task) {
        task.run();
    }
}
