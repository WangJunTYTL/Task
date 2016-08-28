package com.peaceful.task.core.dispatch.executor;


import com.peaceful.task.core.TaskExecutor;

/**
 * Created by wangjun on 16/1/13.
 */
public class SimpleTaskExecutor implements TaskExecutor {

    @Override
    public void execute(Runnable task) {
        task.run();
    }
}
