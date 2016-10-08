package com.peaceful.task.kernal.dispatch.executor;

import com.peaceful.task.kernal.TaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangjun on 16/1/12.
 */
public class JUCTaskExecutor implements TaskExecutor {

    Logger logger = LoggerFactory.getLogger(getClass());
    ExecutorService executorService = Executors.newFixedThreadPool(8);

    @Override
    public void execute(Runnable task) {
        executorService.submit(task);
    }
}
