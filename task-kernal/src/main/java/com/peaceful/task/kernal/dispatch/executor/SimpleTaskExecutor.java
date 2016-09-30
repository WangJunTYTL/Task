package com.peaceful.task.kernal.dispatch.executor;


import com.peaceful.task.kernal.TaskExecutor;

/**
 * Created by wangjun on 16/1/13.
 */
public class SimpleTaskExecutor implements TaskExecutor {

    @Override
    public void execute(Runnable task) {
        task.run();
    }
}
